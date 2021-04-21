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

class AuthActivity : BaseActivity(R.layout.activity_auth), AuthView {

    private val binding: ActivityAuthBinding by viewBinding()
    private lateinit var presenter: AuthPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = AuthPresenter(this)
        presenter.onCreate(this, savedInstanceState)

        binding.number.setSelection(2)

        val def = "+7"

        binding.number.addTextChangedListener {
            with(it.toString().trim()) {
                if (isEmpty() || length == 1 || substring(0, 2) != def) {
                    binding.number.setText(def)
                    binding.number.setSelection(2)
                }
                else {

                }
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
            lifecycleScope.launch {
                presenter.sendPhoneNumber("a")
            }
        }
    }

    //default mvp methods
    override fun hideErrorView() {
        TODO("Not yet implemented")
    }

    override fun hideNoInternetView() {
        TODO("Not yet implemented")
    }

    override fun hideNoItemsView() {
        TODO("Not yet implemented")
    }

    override fun hideProgressBar() {
        TODO("Not yet implemented")
    }

    override fun hideRefreshLayout() {
        TODO("Not yet implemented")
    }

    override fun prepareErrorView() {
        TODO("Not yet implemented")
    }

    override fun prepareNoInternetView() {
        TODO("Not yet implemented")
    }

    override fun prepareNoItemsView() {
        TODO("Not yet implemented")
    }

    override fun showErrorSnackbar(t: Throwable) {
        TODO("Not yet implemented")
    }

    override fun showErrorView() {
        TODO("Not yet implemented")
    }

    override fun showNoInternetView() {
        TODO("Not yet implemented")
    }

    override fun showNoItemsView() {
        TODO("Not yet implemented")
    }

    override fun showProgressBar() {
        TODO("Not yet implemented")
    }

    override fun showRefreshLayout() {
        TODO("Not yet implemented")
    }

}