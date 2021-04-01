package com.meloda.lineqrreader.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.viewbinding.library.activity.viewBinding
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.meloda.lineqrreader.adapter.SimpleItemAdapter
import com.meloda.lineqrreader.base.BaseAdapter
import com.meloda.lineqrreader.common.AppGlobal
import com.meloda.lineqrreader.databinding.ActivityScanBinding
import com.meloda.lineqrreader.listener.ScannerResultListener
import com.meloda.lineqrreader.model.SimpleItem
import com.meloda.lineqrreader.util.ScannerUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ScanActivity : AppCompatActivity(), BaseAdapter.ItemClickListener {

    private val binding: ActivityScanBinding by viewBinding()

    private lateinit var adapter: SimpleItemAdapter
    private lateinit var scanHandler: Handler

    private var scanUtil: ScannerUtil? = null

    private var isButtonPressed = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initScanHandler()

        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 0)
        }

        prepareRecyclerView()
        createEmptyAdapter()

        loadCachedItems()
    }

    override fun onResume() {
        super.onResume()

        scanUtil = ScannerUtil(this, object : ScannerResultListener {
            override fun onResult(sym: String, content: String) {
                scanHandler.sendMessage(Message().apply { obj = content })
            }
        })

        scanUtil?.init()
    }

    override fun onPause() {
        super.onPause()

        scanUtil?.release()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_F11 || keyCode == 280 || keyCode == 281) {
            if (isButtonPressed) return true

            scanUtil?.startDecoding()
            isButtonPressed = true
            return true
        }

        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            onBackPressed()
            return true
        }

        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_F11 || keyCode == 280 || keyCode == 281) {
            if (!isButtonPressed) return true

            scanUtil?.stopDecoding()
            isButtonPressed = false
            return true
        }

        return super.onKeyUp(keyCode, event)
    }

    override fun onBackPressed() {
        scanUtil?.release()
        super.onBackPressed()
    }

    override fun onItemClick(position: Int) {
        adapter.toggleSelection(position)
        invalidateOptionsMenu()
    }

    private fun prepareRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    private fun createEmptyAdapter() {
        adapter = SimpleItemAdapter(this).also { it.itemClickListener = this }
        binding.recyclerView.adapter = adapter
    }

    @SuppressLint("HandlerLeak")
    private fun initScanHandler() {
        scanHandler = object : Handler(mainLooper) {
            override fun handleMessage(msg: Message) {
                msg.obj?.let { message ->
                    Log.d("SCANNER", "add data: $message")

                    val item = SimpleItem()
                    item.content = msg.obj as String

                    adapter.add(item)
                    adapter.notifyDataSetChanged()

                    GlobalScope.launch(Dispatchers.IO) {
                        AppGlobal.database.itemsDao.insert(item)
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(this, "Permission is required for application", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }


    private fun loadCachedItems() {
        GlobalScope.launch(Dispatchers.IO) {
            val cachedItems = AppGlobal.database.itemsDao.getAll()

            withContext(Dispatchers.Main) {
                adapter.updateValues(cachedItems)
                adapter.notifyDataSetChanged()
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val item = menu.add("Delete")
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val item = menu[0]

        val selectedItems = adapter.getSelectedItems()

        item.isVisible = selectedItems.isNotEmpty()

        item.setOnMenuItemClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                AppGlobal.database.itemsDao.delete(selectedItems)

                adapter.values.removeAll(selectedItems)

                withContext(Dispatchers.Main) {
                    adapter.notifyDataSetChanged()
                }
            }
            true
        }

        return super.onPrepareOptionsMenu(menu)
    }

    operator fun Menu.get(index: Int): MenuItem {
        return getItem(index)
    }

}