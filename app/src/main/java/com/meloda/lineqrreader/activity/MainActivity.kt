package com.meloda.lineqrreader.activity

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.viewbinding.library.activity.viewBinding
import com.meloda.lineqrreader.R
import com.meloda.lineqrreader.base.BaseActivity
import com.meloda.lineqrreader.databinding.ActivityMainBinding

class MainActivity : BaseActivity(R.layout.activity_main) {

    private val binding: ActivityMainBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(binding.toolbar)

        startActivity(Intent(this, AuthActivity::class.java))

        binding.openScanScreen.setOnClickListener {
            openScanScreen()
        }
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
        val intent = Intent(this, ScanActivity::class.java)

        startActivity(intent)
    }

}