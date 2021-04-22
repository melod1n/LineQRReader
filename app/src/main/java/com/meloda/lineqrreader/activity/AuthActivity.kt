package com.meloda.lineqrreader.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

        binding.number.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                with(s.toString().trim()) {
                    if (isEmpty() || length == 1 || substring(0, 2) != def) {
                        binding.number.setText(def)
                        binding.number.setSelection(2)
                    } else {
                        if (length == 3) {
                            val i = substring(2, 3)

                            if (i.toIntOrNull() != null) {
                                val text = binding.number.text.toString()

                                //todo
                                val newText =
                                    text.substring(0, 2) + "(" + text.substring(2, 3) + ")"

                                binding.number.setText(newText)
                                binding.number.setSelection(newText.length)
                            }
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.number.addTextChangedListener {

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