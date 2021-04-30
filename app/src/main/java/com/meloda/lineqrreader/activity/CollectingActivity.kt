package com.meloda.lineqrreader.activity

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.animation.Animation
import android.view.animation.Transformation
import android.viewbinding.library.activity.viewBinding
import android.widget.ProgressBar
import androidx.lifecycle.lifecycleScope
import com.meloda.lineqrreader.R
import com.meloda.lineqrreader.base.BaseActivity
import com.meloda.lineqrreader.base.BaseFragment
import com.meloda.lineqrreader.databinding.ActivityCollectingBinding
import com.meloda.lineqrreader.extensions.BooleanExtensions.value
import com.meloda.lineqrreader.extensions.Extensions.withAnimations
import com.meloda.lineqrreader.fragment.CollectingAssembledFragment
import com.meloda.lineqrreader.fragment.CollectingUnassembledFragment
import com.meloda.lineqrreader.model.InventoryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.schedule


class CollectingActivity : BaseActivity(R.layout.activity_collecting) {

    private var selectedFragment: BaseFragment? = null

    private val binding: ActivityCollectingBinding by viewBinding()

    private var selectedColor: Int = 0
    private var unselectedColor: Int = 0

    private lateinit var unassembledCollectingFragment: CollectingUnassembledFragment
    private lateinit var assembledCollectingFragment: CollectingAssembledFragment

    private var currentElements = 0
    private var maxElements = 7

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        selectedColor = Color.parseColor("#f2f2f2")
        unselectedColor = Color.WHITE

        unassembledCollectingFragment = CollectingUnassembledFragment()
        assembledCollectingFragment = CollectingAssembledFragment()

        supportFragmentManager.beginTransaction()
            .withAnimations()
            .add(
                R.id.fragmentContainer,
                unassembledCollectingFragment,
                unassembledCollectingFragment.javaClass.name
            )
            .add(
                R.id.fragmentContainer,
                assembledCollectingFragment,
                assembledCollectingFragment.javaClass.name
            )
            .hide(assembledCollectingFragment)
            .commitNowAllowingStateLoss()

        binding.unassembled.setOnClickListener { replaceFragment(unassembledCollectingFragment) }
        binding.assembled.setOnClickListener { replaceFragment(assembledCollectingFragment) }

        binding.remainPositions.progress = currentElements
        binding.remainPositions.max = maxElements * 100

        setupTimers()
    }

    private fun setupTimers() {
        val currentTime = System.currentTimeMillis()

        assembledCollectingFragment.startCollectingTime = currentTime

        val futureTime = currentTime + 300000

        val zeroCalendar = Calendar.getInstance().apply {
            timeInMillis = 0
            this[Calendar.MINUTE] = 0
            this[Calendar.SECOND] = 0
        }

        Timer().schedule(0, 1000) {
            val now = System.currentTimeMillis()

            zeroCalendar.timeInMillis += 1000

            val change = futureTime - now
            val calendar = Calendar.getInstance().apply {
                timeInMillis = change
            }

            lifecycleScope.launch(Dispatchers.Main) {
                unassembledCollectingFragment.updateTimer(calendar)
                assembledCollectingFragment.updateTimer(zeroCalendar)
            }
        }
    }

    fun addElement(content: String) {
        assembledCollectingFragment.addItem(content)

        currentElements += 1

        val from = binding.remainPositions.progress.toFloat()
        val to = currentElements * 100f

        binding.remainPositionsText.text = getString(
            R.string.remain_positions_with_numbers,
            currentElements,
            maxElements
        )

        binding.remainPositions.startAnimation(
            ProgressBarAnimation(
                binding.remainPositions, from, to
            ).also { it.duration = 500 }
        )

        if (currentElements == maxElements) {
            replaceFragment(assembledCollectingFragment)
            return
        }
    }

    fun removeItems(items: ArrayList<InventoryItem>) {
        unassembledCollectingFragment.removeItems(items)
    }

    fun setItemsSize(size: Int) {
        assembledCollectingFragment.maxElements = size
        maxElements = size

        binding.remainPositions.progress = currentElements
        binding.remainPositions.max = maxElements * 100

        binding.remainPositionsText.text = getString(
            R.string.remain_positions_with_numbers,
            currentElements,
            maxElements
        )

    }

    class ProgressBarAnimation(
        private val progressBar: ProgressBar,
        private val from: Float,
        private val to: Float
    ) : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            super.applyTransformation(interpolatedTime, t)
            val value = from + (to - from) * interpolatedTime
            progressBar.progress = value.toInt()
        }
    }

    private fun setBackgrounds() {
        when (selectedFragment) {
            is CollectingUnassembledFragment -> {
                binding.assembled.backgroundTintList = ColorStateList.valueOf(unselectedColor)
                binding.unassembled.backgroundTintList = ColorStateList.valueOf(selectedColor)
            }

            is CollectingAssembledFragment -> {
                binding.assembled.backgroundTintList = ColorStateList.valueOf(selectedColor)
                binding.unassembled.backgroundTintList = ColorStateList.valueOf(unselectedColor)
            }
        }
    }

    private fun replaceFragment(fragment: BaseFragment) {
        if (selectedFragment == fragment) return
        selectedFragment = fragment

        val fragmentToHide =
            if (fragment == unassembledCollectingFragment) assembledCollectingFragment
            else unassembledCollectingFragment

        supportFragmentManager.beginTransaction()
            .withAnimations()
            .hide(fragmentToHide)
            .show(fragment)
            .commitNowAllowingStateLoss()

        setBackgrounds()
    }

    override fun onResume() {
        super.onResume()
        unassembledCollectingFragment.initScanner()
    }

    override fun onPause() {
        super.onPause()
        unassembledCollectingFragment.releaseScanner()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (unassembledCollectingFragment.onKeyDown(keyCode).value()) return true
        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (unassembledCollectingFragment.onKeyUp(keyCode).value()) return true
        return super.onKeyUp(keyCode, event)
    }


}