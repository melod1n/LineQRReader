package com.meloda.lineqrreader.dialog

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.meloda.lineqrreader.R
import com.meloda.lineqrreader.activity.CollectingActivity
import com.meloda.lineqrreader.adapter.InventoryAdapter
import com.meloda.lineqrreader.databinding.DialogBarcodeBinding
import com.meloda.lineqrreader.extensions.TextViewExtensions.isEmpty
import com.meloda.lineqrreader.extensions.TextViewExtensions.string

class BarcodeDialog(
    private val adapter: InventoryAdapter,
    private val position: Int,
    private val showCount: Boolean = false
) : DialogFragment(R.layout.dialog_barcode) {

    private val binding: DialogBarcodeBinding by viewBinding()

    var onDoneListener: OnDoneListener? = null

    private var isRemovingText: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NORMAL, R.style.Theme_FullScreen_Translucent_Dialog)
    }

    private val onKeyListener = View.OnKeyListener { v, keyCode, event ->
        isRemovingText = keyCode == KeyEvent.KEYCODE_DEL
        false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.barcode.showSoftInputOnFocus = false

        binding.cancel.setOnClickListener { dismiss() }

        binding.countText.isVisible = showCount
        binding.count.isVisible = showCount
        binding.count.showSoftInputOnFocus = false

        binding.barcode.setOnKeyListener(onKeyListener)

        binding.barcode.addTextChangedListener {
            binding.done.isEnabled =
                binding.count.string().isNotEmpty() && binding.barcode.string().isNotEmpty()

            if (it.toString().length == 1 || it.toString().length == 8) {
                if (isRemovingText) return@addTextChangedListener
                binding.barcode.setText(it.toString() + " ")
                binding.barcode.setSelection(binding.barcode.text.toString().length)
            }
        }

        binding.done.isEnabled = false

        binding.count.addTextChangedListener {
            binding.done.isEnabled =
                binding.count.string().isNotEmpty() && binding.barcode.string().isNotEmpty()
        }

        binding.done.setOnClickListener {
            if (binding.barcode.isEmpty() || binding.count.isEmpty()) return@setOnClickListener

            if (position == -1) {
                onDoneListener?.onDone(binding.barcode.string())
            } else {
                val item = adapter[position]
                with(requireActivity() as CollectingActivity) {
                    addElement(item.content)
                    removeItems(arrayListOf(item))
                }
            }

            dismiss()
        }
    }

    interface OnDoneListener {
        fun onDone(result: String)
    }

}