package com.meloda.lineqrreader.dialog

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.fragment.viewBinding
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.meloda.lineqrreader.R
import com.meloda.lineqrreader.base.adapter.BaseAdapter
import com.meloda.lineqrreader.base.adapter.BindingHolder
import com.meloda.lineqrreader.base.adapter.OnItemClickListener
import com.meloda.lineqrreader.databinding.DialogDeleteItemBinding
import com.meloda.lineqrreader.databinding.ItemDeleteReasonBinding
import com.meloda.lineqrreader.extensions.LiveDataExtensions.get
import com.meloda.lineqrreader.extensions.LiveDataExtensions.indices
import com.meloda.lineqrreader.extensions.TextViewExtensions.isEmpty
import com.meloda.lineqrreader.extensions.TextViewExtensions.string
import com.meloda.lineqrreader.model.BaseSelectionItem

class DeleteItemDialog :
    DialogFragment(R.layout.dialog_delete_item),
    OnItemClickListener {

    private val binding: DialogDeleteItemBinding by viewBinding()
    private val reasons: ArrayList<Item> = arrayListOf()

    var onDoneListener: OnDoneListener? = null

    private lateinit var adapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NORMAL, R.style.Theme_FullScreen_Translucent_Dialog)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareViews()

        fillItems()
        createAdapter()

        binding.cancel.setOnClickListener { dismiss() }

        binding.done.setOnClickListener {
            if (!adapter.isSelected) {
                dismiss()
                return@setOnClickListener
            }

            val selectedItem = adapter.getSelectedItem() ?: return@setOnClickListener

            if (selectedItem.content == getString(R.string.other_cause)) {
                showCustomReasonDialog()
                return@setOnClickListener
            }

            onDoneListener?.onDone(selectedItem.content)
            dismiss()
        }
    }

    private fun showCustomReasonDialog() {
        val editText = TextInputEditText(requireContext())
        editText.setHint(R.string.comment)

        AlertDialog.Builder(requireContext())
            .setTitle(R.string.other_cause)
            .setView(editText)
            .setPositiveButton(R.string.done) { _, _ ->
                if (editText.isEmpty()) return@setPositiveButton

                onDoneListener?.onDone(editText.string())
                dismiss()
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun createAdapter() {
        adapter = Adapter().also {
            it.itemClickListener = this
        }
        binding.recyclerView.adapter = adapter
    }

    private fun fillItems() {
        reasons.add(Item(getString(R.string.expiry_date)))
        reasons.add(Item(getString(R.string.damaged_packaging)))
        reasons.add(Item(getString(R.string.product_chomped_by_mouses)))
        reasons.add(Item(getString(R.string.lack_of_product)))
        reasons.add(Item(getString(R.string.other_cause)))
    }

    private fun prepareViews() {
        prepareRecyclerView()
    }

    private fun prepareRecyclerView() {
        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        }
    }


    inner class Adapter : BaseAdapter<Item, Adapter.ViewHolder>(requireContext(), reasons) {

        var isSelected = false

        inner class ViewHolder(binding: ItemDeleteReasonBinding) :
            BindingHolder<ItemDeleteReasonBinding>(binding) {

            override fun bind(position: Int) {
                val item = getItem(position)

                binding.reason.text = item.content
                binding.selectedIcon.isVisible = item.isSelected
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(ItemDeleteReasonBinding.inflate(inflater, parent, false))
        }

        fun setSelection(position: Int) {
            isSelected = true
            val item = this[position]
            if (item.isSelected) return

            for (i in values.indices) {
                values[i].isSelected = i == position
            }

            notifyDataSetChanged()
        }

        fun getSelectedItem(): Item? {
            for (i in values.indices) if (values[i].isSelected) return values[i]

            return null
        }
    }

    data class Item(val content: String) : BaseSelectionItem()

    override fun onItemClick(position: Int) {
        adapter.setSelection(position)
    }

    interface OnDoneListener {
        fun onDone(reason: String)
    }

}