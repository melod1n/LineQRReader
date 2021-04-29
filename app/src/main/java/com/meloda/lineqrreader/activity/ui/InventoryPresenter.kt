package com.meloda.lineqrreader.activity.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import android.view.KeyEvent
import androidx.appcompat.app.AlertDialog
import com.meloda.lineqrreader.R
import com.meloda.lineqrreader.activity.InventoryActivity
import com.meloda.lineqrreader.adapter.InventoryAdapter
import com.meloda.lineqrreader.base.adapter.OnItemLongClickListener
import com.meloda.lineqrreader.common.AppGlobal
import com.meloda.lineqrreader.extensions.LiveDataExtensions.removeAll
import com.meloda.lineqrreader.extensions.LiveDataExtensions.requireValue
import com.meloda.lineqrreader.listener.OnCompleteListener
import com.meloda.lineqrreader.listener.ScannerResultListener
import com.meloda.lineqrreader.model.InventoryItem
import com.meloda.lineqrreader.scanner.ScannerUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.MvpPresenter
import moxy.presenterScope
import kotlin.random.Random

class InventoryPresenter() : MvpPresenter<InventoryView>(), OnItemLongClickListener {

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 1

        private val TAG = InventoryPresenter::class.java.name
    }

    private lateinit var adapter: InventoryAdapter
    private lateinit var context: Context

    private var scanUtil: ScannerUtil? = null
    private var isButtonPressed = false
    private var lastId = 0

    private val database = AppGlobal.database.inventory

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        viewState.prepareViews()

        viewState.createAdapter(object : OnCompleteListener<InventoryAdapter> {
            override fun onComplete(item: InventoryAdapter) {
                context = item.context

                checkCameraPermission()

                initAdapter(item)

                loadCachedItems()
            }
        })
    }

    private fun initAdapter(adapter: InventoryAdapter) {
        this.adapter = adapter.also {
            it.itemLongClickListener = this
        }
    }


    private fun checkCameraPermission() {
        if (context.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            (context as InventoryActivity).requestPermissions(
                arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION
            )
        }
    }

    private fun loadCachedItems() = presenterScope.launch {
        val cachedItems = database.getAll()

        if (cachedItems.isNotEmpty()) {
            lastId = cachedItems[cachedItems.size - 1].id

            cachedItems.forEach {
                it.count = Random.nextInt(2, 100)
                it.number = "A${Random.nextInt(1000, 9999)}"
            }
        }

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
            (context as InventoryActivity).onBackPressed()
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

    fun setAdapterError(position: Int, errorText: String) {
        adapter.setError(position, errorText)
    }

    private suspend fun addItem(content: String) {
        val item = InventoryItem()

        lastId += 1

        item.content = content
        item.id = lastId

        database.insert(item)

        adapter.add(item)

        withContext(Dispatchers.Main) {
            adapter.notifyDataSetChanged()
        }
    }

    private suspend fun removeItemsFromAdapter(items: MutableList<InventoryItem>) {
        adapter.values.removeAll(items)

        withContext(Dispatchers.Main) {
            adapter.notifyDataSetChanged()
        }
    }

    private suspend fun updateItems(items: List<InventoryItem>) {
        if (items.isEmpty()) return

        adapter.updateValues(items)

        withContext(Dispatchers.Main) {
            adapter.notifyDataSetChanged()
        }
    }

    fun showDeleteItemDialog(position: Int) {
        AlertDialog.Builder(context)
            .setTitle(R.string.warning)
            .setMessage(R.string.delete_scans_message)
            .setPositiveButton(R.string.yes) { _, _ ->
                val item = adapter[position]
                presenterScope.launch {
                    database.delete(item)

                    removeItemsFromAdapter(arrayListOf(item))
                }
            }
            .setNegativeButton(R.string.no, null)
            .show()
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