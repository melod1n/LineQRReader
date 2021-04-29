package com.meloda.lineqrreader.fragment.ui

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.meloda.lineqrreader.R
import com.meloda.lineqrreader.adapter.InventoryAdapter
import com.meloda.lineqrreader.base.adapter.OnItemLongClickListener
import com.meloda.lineqrreader.extensions.LiveDataExtensions.removeAll
import com.meloda.lineqrreader.listener.OnCompleteListener
import com.meloda.lineqrreader.model.InventoryItem
import moxy.MvpPresenter

class CollectingAssembledPresenter :
    MvpPresenter<CollectingAssembledView>(),
    OnItemLongClickListener {

    private lateinit var adapter: InventoryAdapter
    private lateinit var context: Context

    private var lastId: Int = 0

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        viewState.prepareViews()

        viewState.createAdapter(object : OnCompleteListener<InventoryAdapter> {
            override fun onComplete(item: InventoryAdapter) {
                context = item.context

                initAdapter(item)
            }
        })
    }

    private fun initAdapter(adapter: InventoryAdapter) {
        this.adapter = adapter.also {
            it.itemLongClickListener = this
        }
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

    fun addItem(content: String) {
        val item = InventoryItem()

        lastId += 1

        item.content = content
        item.id = lastId

        adapter.add(item)
        adapter.notifyDataSetChanged()
    }

    private fun removeItemsFromAdapter(items: MutableList<InventoryItem>) {
        adapter.values.removeAll(items)
        adapter.notifyDataSetChanged()
    }

    override fun onItemLongClick(position: Int) {
        TODO("Not yet implemented")
    }

}