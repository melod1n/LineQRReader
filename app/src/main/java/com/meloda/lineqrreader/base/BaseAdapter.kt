package com.meloda.lineqrreader.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView


@Suppress("UNCHECKED_CAST")
abstract class BaseAdapter<Item, VH : BaseHolder>(
    var context: Context,
    var values: ArrayList<Item> = arrayListOf()
) : RecyclerView.Adapter<VH>() {

    companion object {
        private const val P_ITEMS = "BaseAdapter.values"
    }

    private var cleanValues: ArrayList<Item>? = null

    private var inflater: LayoutInflater = LayoutInflater.from(context)

    var itemClickListener: ItemClickListener? = null
    var itemLongClickListener: ItemLongClickListener? = null

    open fun destroy() {
        itemClickListener = null
        itemLongClickListener = null
    }

    open fun getItem(position: Int): Item {
        return values[position]
    }

    fun add(position: Int, item: Item) {
        values.add(position, item)
        cleanValues?.add(position, item)
    }

    fun add(item: Item) {
        values.add(item)
        cleanValues?.add(item)
    }

    fun addAll(items: List<Item>) {
        values.addAll(items)
        cleanValues?.addAll(items)
    }

    fun addAll(position: Int, items: List<Item>) {
        values.addAll(position, items)
        cleanValues?.addAll(position, items)
    }

    operator fun set(position: Int, item: Item) {
        values[position] = item
        cleanValues?.set(position, item)
    }

    open fun notifyChanges(oldList: List<Item>, newList: List<Item> = values) {}

    fun isEmpty() = values.isNullOrEmpty()

    fun isNotEmpty() = !isEmpty()

    fun view(resId: Int, viewGroup: ViewGroup, attachToRoot: Boolean = false): View {
        return inflater.inflate(resId, viewGroup, attachToRoot)
    }

    fun updateValues(arrayList: ArrayList<Item>) {
        values.clear()
        values.addAll(arrayList)
    }

    fun updateValues(list: List<Item>) = updateValues(ArrayList(list))

    override fun onBindViewHolder(holder: VH, position: Int) {
        onBindItemViewHolder(holder, position)
    }

    protected fun initListeners(itemView: View, position: Int) {
        if (itemView is AdapterView<*>) return

        itemView.setOnClickListener {
            itemClickListener?.onItemClick(position)
        }

        itemView.setOnLongClickListener {
            itemLongClickListener?.onItemLongClick(position)
            return@setOnLongClickListener itemClickListener == null
        }
    }

    override fun getItemCount(): Int {
        return values.size
    }

    private fun onBindItemViewHolder(holder: VH, position: Int) {
        initListeners(holder.itemView, position)
        holder.bind(position)
    }

    interface ItemClickListener {
        fun onItemClick(position: Int)
    }

    interface ItemLongClickListener {
        fun onItemLongClick(position: Int)
    }

}