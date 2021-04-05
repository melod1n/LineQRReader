package com.meloda.lineqrreader.activity.ui.presenter

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.KeyEvent
import android.view.MenuItem
import com.meloda.lineqrreader.activity.ScanActivity
import com.meloda.lineqrreader.activity.ui.repository.ScanRepository
import com.meloda.lineqrreader.activity.ui.view.ScanView
import com.meloda.lineqrreader.adapter.SimpleItemAdapter
import com.meloda.lineqrreader.common.AppGlobal
import com.meloda.lineqrreader.listener.ScannerResultListener
import com.meloda.lineqrreader.model.SimpleItem
import com.meloda.lineqrreader.util.ScannerUtil
import com.meloda.mvp.MvpPresenter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ScanPresenter(viewState: ScanView) :
    MvpPresenter<Any, ScanRepository, ScanView>(
        viewState,
        ScanRepository::class.java.name
    ) {

    companion object {
        private const val REQUEST_CAMERA_PERMISSION = 1

        private val TAG = ScanPresenter::class.java.name
    }

    private var scanUtil: ScannerUtil? = null

    private lateinit var scanHandler: Handler

    private lateinit var adapter: SimpleItemAdapter

    private var isButtonPressed = false

    private var lastId = 0

    override fun onCreate(context: Context, bundle: Bundle?) {
        super.onCreate(context, bundle)

        init()
    }

    private fun init() {
        adapter = SimpleItemAdapter(requireContext())
        viewState.prepareViews()
        viewState.setRecyclerViewAdapter(adapter)

        scanHandler = object : Handler((requireContext() as ScanActivity).mainLooper) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                msg.obj ?: return

                scanUtil?.isContinuous = false
                val item = SimpleItem()

                lastId += 1

                item.content = msg.obj as String
                item.id = lastId

                GlobalScope.launch(Dispatchers.IO) {
                    AppGlobal.database.itemsDao.insert(item)

                    adapter.add(item)

                    withContext(Dispatchers.Main) {
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        }

        checkCameraPermission()

        loadCachedItems()
    }

    private fun checkCameraPermission() {
        if (requireContext().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            (requireContext() as ScanActivity).requestPermissions(
                arrayOf(Manifest.permission.CAMERA), REQUEST_CAMERA_PERMISSION
            )
        }
    }

    private fun loadCachedItems() {
        GlobalScope.launch(Dispatchers.IO) {
            val cachedItems = repository.getCachedItems()

            if (cachedItems.isNotEmpty())
                lastId = cachedItems[cachedItems.size - 1].id

            updateItems(cachedItems)
        }
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

    fun onResume() {
        scanUtil = ScannerUtil(requireContext(), object : ScannerResultListener {
            override fun onResult(sym: String, content: String) {
                scanHandler.sendMessage(Message().also { it.obj = content })
            }
        })

        scanUtil?.init()
    }

    fun onPause() {
        scanUtil?.release()
    }

    fun onKeyDown(keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_F11 || keyCode == 280 || keyCode == 281) {
            if (isButtonPressed) return true

            scanUtil?.startDecoding()
            isButtonPressed = true
            return true
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            (requireContext() as ScanActivity).onBackPressed()
            return true
        }

        return false
    }

    fun onKeyUp(keyCode: Int): Boolean {
        if (keyCode == KeyEvent.KEYCODE_F11 || keyCode == 280 || keyCode == 281) {
            if (!isButtonPressed) return true

            scanUtil?.stopDecoding()
            isButtonPressed = false
            return true
        }

        return false
    }

    fun toggleAdapterItemSelection(position: Int) {
        adapter.toggleSelection(position)
        viewState.setMenuDeleteItemVisible(getSelectedItems().isNotEmpty())
    }

    private fun getSelectedItems(): ArrayList<SimpleItem> {
        return adapter.getSelectedItems()
    }

    private suspend fun removeItemsFromAdapter(items: ArrayList<SimpleItem>) {
        adapter.values.removeAll(items)
        withContext(Dispatchers.Main) {
            viewState.setMenuDeleteItemVisible(false)
            adapter.notifyDataSetChanged()
        }
    }

    private suspend fun updateItems(items: List<SimpleItem>) {
        if (items.isEmpty()) return

        adapter.updateValues(items)
        withContext(Dispatchers.Main) { adapter.notifyDataSetChanged() }
    }

    fun setDeleteMenuItemClickListener(item: MenuItem) {
        item.setOnMenuItemClickListener {
            val selectedItems = adapter.getSelectedItems()
            GlobalScope.launch(Dispatchers.IO) {
                AppGlobal.database.itemsDao.delete(selectedItems)

                removeItemsFromAdapter(selectedItems)
            }
            true
        }
    }

}