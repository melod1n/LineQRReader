package com.meloda.lineqrreader.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.meloda.lineqrreader.R
import com.meloda.lineqrreader.base.adapter.BaseAdapter
import com.meloda.lineqrreader.base.adapter.BaseHolder
import com.meloda.lineqrreader.extensions.LiveDataExtensions.iterator
import com.meloda.lineqrreader.model.SimpleItem

class SimpleItemAdapter(context: Context, items: ArrayList<SimpleItem> = arrayListOf()) :
    BaseAdapter<SimpleItem, SimpleItemAdapter.ViewHolder>(context, items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(view(R.layout.item, parent))
    }

    val selectedItems
        get() = ArrayList<SimpleItem>().apply {
            for (item in values) if (item.isSelected) add(item)
        }

    fun toggleSelection(position: Int) {
        val item = getItem(position)
        item.isSelected = item.isSelected.not()
        notifyItemChanged(position)
    }

    inner class ViewHolder(v: View) : BaseHolder(v) {
        private val textView: TextView = v.findViewById(R.id.textView)
        private val checkBox: CheckBox = v.findViewById(R.id.checkBox)

        override fun bind(position: Int) {
            val item = getItem(position)

            textView.text = item.content
            checkBox.isChecked = item.isSelected
        }
    }

}
