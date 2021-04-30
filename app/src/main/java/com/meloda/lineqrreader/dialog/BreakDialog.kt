package com.meloda.lineqrreader.dialog

import android.os.Bundle
import android.view.*
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.lifecycleScope
import com.meloda.lineqrreader.R
import com.meloda.lineqrreader.databinding.DialogBreakBinding
import com.meloda.lineqrreader.util.Utils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.schedule

//TODO: extract colors
class BreakDialog : DialogFragment(R.layout.dialog_break) {

    private var isDestroyed = false

    companion object {
        fun show(fragmentManager: FragmentManager) = BreakDialog().apply {
            show(fragmentManager, "break_dialog")
        }
    }

    private val binding: DialogBreakBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NORMAL, R.style.Theme_FullScreen_Translucent_Dialog)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentTime = System.currentTimeMillis()
        val futureTime = currentTime + 900000

        binding.endBreak.text = getString(R.string.end_early)

        val timer = Timer()
        timer.schedule(0, 1000) {
            val now = System.currentTimeMillis()

            val change = futureTime - now

            val calendar = Calendar.getInstance().apply { timeInMillis = change }

            val minutes = calendar[Calendar.MINUTE]
            val seconds = calendar[Calendar.SECOND]

            lifecycleScope.launch(Dispatchers.Main) {
                if (isDestroyed) return@launch

                binding.breakEndTime.text = Utils.getLocalizedTime(
                    requireContext(), minutes, seconds
                )

                if (minutes == 0 && seconds == 0) {
                    binding.endBreak.text = getString(R.string.main_menu)
                    timer.cancel()
                }
            }
        }

        binding.endBreak.setOnClickListener { dismiss() }
    }

    override fun onDestroy() {
        isDestroyed = true
        super.onDestroy()
    }
}