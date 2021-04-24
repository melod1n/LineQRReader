package com.meloda.lineqrreader.activity

import android.os.Bundle
import com.meloda.lineqrreader.R
import com.meloda.lineqrreader.activity.ui.AuthPresenter
import com.meloda.lineqrreader.activity.ui.AuthView
import com.meloda.lineqrreader.base.BaseActivity
import com.meloda.lineqrreader.extensions.Extensions.withAnimations
import com.meloda.lineqrreader.extensions.MoxyExtensions.moxyPresenter
import com.meloda.lineqrreader.fragment.InputCodeAuthFragment
import com.meloda.lineqrreader.fragment.InputNumberAuthFragment

class AuthActivity : BaseActivity(R.layout.activity_auth), AuthView {

    val presenter: AuthPresenter by moxyPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter.state.observe({ lifecycleRegistry }) {
            when (it) {
                AuthPresenter.State.INPUT_NUMBER -> showInputNumberScreen()
                AuthPresenter.State.INPUT_CODE -> showInputCodeScreen()
            }
        }
    }

    private fun showInputNumberScreen() {
        supportFragmentManager.beginTransaction().withAnimations()
            .replace(R.id.fragmentContainer, InputNumberAuthFragment())
            .commit()
    }

    private fun showInputCodeScreen() {
        val transaction = supportFragmentManager.beginTransaction().withAnimations()
            .replace(R.id.fragmentContainer, InputCodeAuthFragment())

        if (supportFragmentManager.fragments.isNotEmpty())
            transaction.addToBackStack(null)

        transaction.commit()
    }
}