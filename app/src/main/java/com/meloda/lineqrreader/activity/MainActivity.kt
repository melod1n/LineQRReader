package com.meloda.lineqrreader.activity

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.viewbinding.library.activity.viewBinding
import com.meloda.lineqrreader.R
import com.meloda.lineqrreader.activity.ui.MainPresenter
import com.meloda.lineqrreader.activity.ui.MainView
import com.meloda.lineqrreader.base.BaseActivity
import com.meloda.lineqrreader.databinding.ActivityMainBinding
import com.meloda.lineqrreader.extensions.Extensions.withAnimations
import com.meloda.lineqrreader.extensions.MoxyExtensions.viewPresenter
import com.meloda.lineqrreader.fragment.MainFragment

class MainActivity : BaseActivity(), MainView {

    private val binding: ActivityMainBinding by viewBinding()
    private val presenter: MainPresenter by viewPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.logout.setOnClickListener {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }

        supportFragmentManager.beginTransaction()
            .withAnimations()
            .replace(R.id.fragmentContainer, MainFragment())
            .commit()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP ||
            keyCode == KeyEvent.KEYCODE_F11 ||
            keyCode == KeyEvent.KEYCODE_SYSTEM_NAVIGATION_UP ||
            keyCode == KeyEvent.KEYCODE_SYSTEM_NAVIGATION_DOWN
        ) {
            openScanScreen()
            return true
        }

        return super.onKeyDown(keyCode, event)
    }

    private fun openScanScreen() {
        val intent = Intent(this, InventoryActivity::class.java)

        startActivity(intent)
    }

}