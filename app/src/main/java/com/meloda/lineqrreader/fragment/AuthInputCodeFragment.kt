package com.meloda.lineqrreader.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import com.meloda.lineqrreader.R
import com.meloda.lineqrreader.activity.MainActivity
import com.meloda.lineqrreader.base.BaseFragment
import com.meloda.lineqrreader.common.AppGlobal
import com.meloda.lineqrreader.databinding.FragmentAuthInputCodeBinding
import com.meloda.lineqrreader.fragment.ui.InputCodeView

class AuthInputCodeFragment : BaseFragment(R.layout.fragment_auth_input_code), InputCodeView {

    private val binding: FragmentAuthInputCodeBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.send.setOnClickListener {
            AppGlobal.preferences.edit().putBoolean("isFirstLaunch", false).apply()
            with(requireActivity()) {
                finish()
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }

}