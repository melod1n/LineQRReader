package com.meloda.lineqrreader.fragment

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.viewbinding.library.fragment.viewBinding
import androidx.core.view.isVisible
import com.meloda.lineqrreader.R
import com.meloda.lineqrreader.base.BaseFragment
import com.meloda.lineqrreader.base.answer.Status
import com.meloda.lineqrreader.databinding.FragmentAuthInputNumberBinding
import com.meloda.lineqrreader.extensions.TextViewExtensions.string
import com.meloda.lineqrreader.fragment.ui.InputNumberPresenter
import com.meloda.lineqrreader.fragment.ui.InputNumberView
import com.meloda.lineqrreader.util.KeyboardUtils
import com.meloda.lineqrreader.util.ViewUtils
import moxy.ktx.moxyPresenter

class AuthInputNumberFragment : BaseFragment(R.layout.fragment_auth_input_number), InputNumberView {

    private val binding: FragmentAuthInputNumberBinding by viewBinding()
    private val presenter: InputNumberPresenter by moxyPresenter { InputNumberPresenter(this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.answer.observe(viewLifecycleOwner, {
            when (it?.status) {
                Status.SUCCESS -> {
                    binding.content.isVisible = true
                    binding.progressBar.isVisible = false

                    errorSnackbar?.dismiss()
                }
                Status.LOADING -> {
                    binding.content.isVisible = false
                    binding.progressBar.isVisible = true
                    errorSnackbar?.dismiss()
                }
                else -> {
                    binding.content.isVisible = true
                    binding.progressBar.isVisible = false

                    ViewUtils.createErrorSnackbar(this, it?.message).show()
                }
            }
        })

        binding.number.showSoftInputOnFocus = false

        binding.number.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_GO) binding.next.performClick()
            false
        }
        binding.number.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && (keyCode == KeyEvent.KEYCODE_ENTER)) binding.next.performClick()
            false
        }

        binding.next.setOnClickListener {
            presenter.sendPhoneNumber(binding.number.string())
        }
    }

    override fun hideKeyboard() {
        KeyboardUtils.hideKeyboard(binding.number)
    }

}