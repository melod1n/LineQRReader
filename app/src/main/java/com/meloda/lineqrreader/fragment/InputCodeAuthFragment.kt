package com.meloda.lineqrreader.fragment

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import com.meloda.lineqrreader.R
import com.meloda.lineqrreader.base.BaseFragment
import com.meloda.lineqrreader.databinding.FragmentAuthInputCodeBinding
import com.meloda.lineqrreader.fragment.ui.InputCodeView

class InputCodeAuthFragment : BaseFragment(R.layout.fragment_auth_input_code), InputCodeView {

    private val binding: FragmentAuthInputCodeBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.send.setOnClickListener {
            requireActivity().finish()
        }
    }

}