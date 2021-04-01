package com.meloda.lineqrreader.activity

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.viewbinding.library.activity.viewBinding
import androidx.appcompat.app.AppCompatActivity
import com.meloda.lineqrreader.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.openScanScreen.setOnClickListener {
            openScanScreen()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP ||
            keyCode == KeyEvent.KEYCODE_F11 ||
            keyCode == 280 ||
            keyCode == 281
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