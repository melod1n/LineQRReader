package com.meloda.lineqrreader.fragment.ui

import android.content.Context
import android.view.KeyEvent
import androidx.appcompat.app.AlertDialog
import com.meloda.lineqrreader.R
import com.meloda.lineqrreader.activity.CollectingActivity
import com.meloda.lineqrreader.activity.InventoryActivity
import com.meloda.lineqrreader.adapter.InventoryAdapter
import com.meloda.lineqrreader.base.adapter.OnItemLongClickListener
import com.meloda.lineqrreader.common.AppConstants
import com.meloda.lineqrreader.dialog.BarcodeDialog
import com.meloda.lineqrreader.dialog.DeleteItemDialog
import com.meloda.lineqrreader.extensions.LiveDataExtensions.removeAll
import com.meloda.lineqrreader.extensions.LiveDataExtensions.requireValue
import com.meloda.lineqrreader.listener.OnCompleteListener
import com.meloda.lineqrreader.listener.ScannerResultListener
import com.meloda.lineqrreader.model.InventoryItem
import com.meloda.lineqrreader.scanner.ScannerUtil
import com.meloda.lineqrreader.util.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import moxy.MvpPresenter
import moxy.presenterScope
import kotlin.random.Random

class CollectingUnassembledPresenter :
    MvpPresenter<CollectingUnassembledView>(),
    OnItemLongClickListener,
    InventoryAdapter.OnSuggestDeleteListener {

    companion object {
        private const val MAX_ITEMS = 5
    }

    private lateinit var adapter: InventoryAdapter
    private lateinit var context: Context

    private var scanUtil: ScannerUtil? = null
    private var isButtonPressed = false

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        viewState.prepareViews()

        viewState.createAdapter(object : OnCompleteListener<InventoryAdapter> {
            override fun onComplete(item: InventoryAdapter) {
                context = item.context

                initAdapter(item)
                createItems()
            }
        })
    }

    private fun initAdapter(adapter: InventoryAdapter) {
        this.adapter = adapter.also {
            it.itemLongClickListener = this
            it.onSuggestDeleteListener = this
        }
    }

    private fun createItems() {
        val items = arrayListOf<InventoryItem>()

        val titles = AppConstants.titles.split(",")

        for (i in 0..Random.nextInt(1, MAX_ITEMS)) {
            items.add(
                InventoryItem(
                    (i + 1),
                    titles[Random.nextInt(0, titles.size - 1)],
                    Random.nextInt(1, 10), Utils.generateCell()
                )
            )
        }

        (context as CollectingActivity).setItemsSize(items.size)

        adapter.addAll(items)
        adapter.notifyDataSetChanged()
    }

    fun initScanner() {
        scanUtil = ScannerUtil(context, object : ScannerResultListener {
            override fun onResult(sym: String, content: String) {
                presenterScope.launch(Dispatchers.Main) {
                    if (adapter.isEmpty()) return@launch

                    val index = if (adapter.size == 1) 0
                    else Random.nextInt(0, adapter.size - 1)
                    adapter.removeAt(index)
                    adapter.notifyDataSetChanged()

                    with(context as CollectingActivity) { addElement(content) }
                }
            }
        })

        scanUtil?.init()
    }

    fun releaseScanner() {
        scanUtil?.release()
    }

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

    fun removeItemsFromAdapter(items: MutableList<InventoryItem>) {
        adapter.values.removeAll(items)
        adapter.notifyDataSetChanged()
    }

    fun showDeleteItemDialog(position: Int) {
        AlertDialog.Builder(context)
            .setTitle(R.string.warning)
            .setMessage(R.string.delete_scans_message)
            .setPositiveButton(R.string.yes) { _, _ ->
                val item = adapter[position]
                removeItemsFromAdapter(arrayListOf(item))

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
                    removeItemsFromAdapter(adapter.values.requireValue())
                }
            }
            .setNegativeButton(R.string.no, null)
            .show()
    }

    override fun onItemLongClick(position: Int) {
        showDeleteAllDialog()
    }

    fun showBarcodeDialog(position: Int) {
        BarcodeDialog(adapter, position).show(
            (context as CollectingActivity).supportFragmentManager,
            "barcode_dialog"
        )
    }

    override fun onSuggest(position: Int) {
        DeleteItemDialog().apply {
            this.onDoneListener = object : DeleteItemDialog.OnDoneListener {
                override fun onDone(reason: String) {
                    removeItemsFromAdapter(arrayListOf(adapter[position]))
                }

            }
        }.show((context as CollectingActivity).supportFragmentManager, "delete_dialog")
    }
}