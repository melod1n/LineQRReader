package com.meloda.lineqrreader.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.meloda.lineqrreader.R
import com.meloda.lineqrreader.base.adapter.BaseAdapter
import com.meloda.lineqrreader.base.adapter.BindingHolder
import com.meloda.lineqrreader.databinding.ItemInventoryBinding
import com.meloda.lineqrreader.model.InventoryItem
import com.meloda.lineqrreader.util.ColorUtils

class InventoryAdapter(context: Context, items: ArrayList<InventoryItem> = arrayListOf()) :
    BaseAdapter<InventoryItem, InventoryAdapter.ViewHolder>(context, items) {

    interface OnSuggestDeleteListener {
        fun onSuggest(position: Int)
    }

    var onSuggestDeleteListener: OnSuggestDeleteListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemInventoryBinding.inflate(inflater, parent, false))
    }

    fun setError(position: Int, errorText: String) {
        val item = getItem(position)
        item.error = if (item.isError()) "" else errorText
        notifyItemChanged(position)
    }

    inner class ViewHolder(binding: ItemInventoryBinding) :
        BindingHolder<ItemInventoryBinding>(binding) {

        private val errorColor = Color.parseColor("#EB5757")
        private val removeDefaultColor = Color.parseColor("#EB5757")
        private val removeErrorColor = Color.parseColor("#333333")
        private val alphaErrorColor = ColorUtils.alphaColor(errorColor, 0.42f)

        override fun bind(position: Int) {
            val item = getItem(position)

            val contentText = "%d. %s".format((position + 1), item.content)
            binding.content.text = contentText

            binding.remove.setOnClickListener {
                onSuggestDeleteListener?.onSuggest(position)
            }
            binding.removeIcon.imageTintList =
                ColorStateList.valueOf(
                    if (item.isError()) removeErrorColor else removeDefaultColor
                )

            binding.count.text = context.getString(R.string.count_num, item.count)
            binding.cellNumber.text = item.number

            binding.error.isVisible = item.isError()
            binding.error.text = if (item.isError()) item.error else ""

            binding.root.setBackgroundColor(if (item.isError()) alphaErrorColor else Color.WHITE)
        }
    }

}
