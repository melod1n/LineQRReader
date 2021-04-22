package com.meloda.lineqrreader.activity

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import com.meloda.lineqrreader.base.BaseActivity

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        openScanScreen()
        finish()
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