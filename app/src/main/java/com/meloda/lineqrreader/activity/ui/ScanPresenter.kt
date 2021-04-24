package com.meloda.lineqrreader.activity.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.view.KeyEvent
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.meloda.lineqrreader.R
import com.meloda.lineqrreader.activity.ScanActivity
import com.meloda.lineqrreader.adapter.SimpleItemAdapter
import com.meloda.lineqrreader.base.adapter.OnItemLongClickListener
import com.meloda.lineqrreader.common.AppGlobal
import com.meloda.lineqrreader.extensions.LiveDataExtensions.removeAll
import com.meloda.lineqrreader.extensions.LiveDataExtensions.requireValue
import com.meloda.lineqrreader.listener.ScannerResultListener
import com.meloda.lineqrreader.model.SimpleItem
import com.meloda.lineqrreader.scanner.ScannerUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.MvpPresenter
import moxy.presenterScope

class ScanPresenter(
    private var context: Context
) : MvpPresenter<ScanView>(), OnItemLongClickListener {

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 1

        private val TAG = ScanPresenter::class.java.name
    }

    private var scanUtil: ScannerUtil? = null

    private lateinit var adapter: SimpleItemAdapter

    private val database = AppGlobal.database.itemsDao

    private var isButtonPressed = false

    private var lastId = 0

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        checkCameraPermission()

        viewState.prepareViews()
        viewState.setRecyclerViewAdapter(initAdapter())

        loadCachedItems()
    }

    private fun initAdapter(): SimpleItemAdapter {
        return SimpleItemAdapter(context).also {
            it.itemLongClickListener = this
            adapter = it
        }
    }

    private fun checkCameraPermission() {
        if (context.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            (context as ScanActivity).requestPermissions(
                arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION
            )
        }
    }

    private fun loadCachedItems() = presenterScope.launch {
        val cachedItems = database.getAll()

        if (cachedItems.isNotEmpty())
            lastId = cachedItems[cachedItems.size - 1].id

        updateItems(cachedItems)
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "camera permission granted")
            } else {
                Log.d(TAG, "camera permission denied")
                checkCameraPermission()
            }
        }
    }

    fun initScanner() {
        scanUtil = ScannerUtil(context, object : ScannerResultListener {
            override fun onResult(sym: String, content: String) {
                presenterScope.launch { addItem(content) }
            }
        })

        scanUtil?.init()
    }

    fun releaseScanner() {
        scanUtil?.release()
    }

    val itemCount get() = adapter.itemCount

    fun onKeyDown(keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_F11 ||
            keyCode == KeyEvent.KEYCODE_SYSTEM_NAVIGATION_UP ||
            keyCode == KeyEvent.KEYCODE_SYSTEM_NAVIGATION_DOWN
        ) {
            if (isButtonPressed) return true

            scanUtil?.startDecoding()
            isButtonPressed = true
            return true
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            (context as ScanActivity).onBackPressed()
            return true
        }

        return false
    }

    fun onKeyUp(keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_F11 ||
            keyCode == KeyEvent.KEYCODE_SYSTEM_NAVIGATION_UP ||
            keyCode == KeyEvent.KEYCODE_SYSTEM_NAVIGATION_DOWN
        ) {
            if (!isButtonPressed) return true

            scanUtil?.stopDecoding()
            isButtonPressed = false
            return true
        }

        return false
    }

    fun toggleAdapterItemSelection(position: Int) {
        adapter.toggleSelection(position)
        viewState.setMenuDeleteItemVisible(adapter.selectedItems.isNotEmpty())
    }

    private suspend fun addItem(content: String) {
        val item = SimpleItem()

        lastId += 1

        item.content = content
        item.id = lastId

        database.insert(item)

        adapter.add(item)

        withContext(Dispatchers.Main) {
            viewState.invalidateTitleCounter()
            adapter.notifyDataSetChanged()
        }
    }

    private suspend fun removeItemsFromAdapter(items: MutableList<SimpleItem>) {
        adapter.values.removeAll(items)

        withContext(Dispatchers.Main) {
            viewState.setMenuDeleteItemVisible(false)
            viewState.invalidateTitleCounter()
            adapter.notifyDataSetChanged()
        }
    }

    private suspend fun updateItems(items: List<SimpleItem>) {
        if (items.isEmpty()) return

        adapter.updateValues(items)

        withContext(Dispatchers.Main) {
            viewState.invalidateTitleCounter()
            adapter.notifyDataSetChanged()
        }
    }

    fun setDeleteMenuItemClickListener(item: MenuItem) {
        item.setOnMenuItemClickListener {
            AlertDialog.Builder(context)
                .setTitle(R.string.warning)
                .setMessage(R.string.delete_scans_message)
                .setPositiveButton(R.string.yes) { _, _ ->
                    val items = adapter.selectedItems
                    presenterScope.launch {
                        database.delete(items)

                        removeItemsFromAdapter(items)
                    }
                }
                .setNegativeButton(R.string.no, null)
                .show()
            true
        }
    }

    private fun showDeleteAllDialog() {
        AlertDialog.Builder(context)
            .setTitle(R.string.warning)
            .setMessage(R.string.delete_all_scans_message)
            .setPositiveButton(R.string.yes) { _, _ ->
                presenterScope.launch {
                    database.clear()
                    removeItemsFromAdapter(adapter.values.requireValue())
                }
            }
            .setNegativeButton(R.string.no, null)
            .show()
    }

    override fun onItemLongClick(position: Int) {
        showDeleteAllDialog()
    }

}