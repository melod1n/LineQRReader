package com.meloda.lineqrreader.activity

import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.meloda.lineqrreader.R
import com.meloda.lineqrreader.activity.ui.presenter.AuthPresenter
import com.meloda.lineqrreader.activity.ui.view.AuthView
import com.meloda.lineqrreader.base.BaseActivity
import com.meloda.lineqrreader.databinding.ActivityAuthBinding
import kotlinx.coroutines.launch
import moxy.ktx.moxyPresenter

class AuthActivity : BaseActivity(R.layout.activity_auth), AuthView {

    private val binding: ActivityAuthBinding by viewBinding()

    private val presenter by moxyPresenter { AuthPresenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.number.setSelection(2)

        val def = "+7"

        binding.number.addTextChangedListener {
            with(it.toString().trim()) {
                if (isEmpty() || length == 1 || substring(0, 2) != def) {
                    binding.number.setText(def)
                    binding.number.setSelection(2)
                }
//                else {
//
//                }
            }
        }

        presenter.authAnswer.observe({ lifecycleRegistry }, {
            when (it?.status) {
                AuthPresenter.AuthAnswer.Status.SUCCESS -> {
                }
                AuthPresenter.AuthAnswer.Status.LOADING -> {
                }
                else -> {
                }
            }
        })

        requireRootView().setOnClickListener {
            lifecycleScope.launch { presenter.sendPhoneNumber() }
        }
    }

}