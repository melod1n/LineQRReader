package com.meloda.lineqrreader.activity

import android.os.Bundle
import android.viewbinding.library.activity.viewBinding
import com.meloda.lineqrreader.R
import com.meloda.lineqrreader.base.BaseActivity
import com.meloda.lineqrreader.databinding.ActivityOrderCollectedBinding
import com.meloda.lineqrreader.util.Utils
import java.util.*
import kotlin.random.Random

class OrderCollectedActivity : BaseActivity(R.layout.activity_order_collected) {

    private val binding: ActivityOrderCollectedBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val collectingTime = intent.getLongExtra("collectingTime", 0)

        val then = collectingTime
        val now = System.currentTimeMillis()

        val change = now - then

        val calendar = Calendar.getInstance().apply { timeInMillis = change }

        binding.orderCode.text =
            getString(R.string.order_number_collected, Random.nextInt(1, Int.MAX_VALUE))

        binding.orderTimeValue.text = Utils.getLocalizedTime(
            this,
            calendar[Calendar.MINUTE],
            calendar[Calendar.SECOND]
        )

        binding.gaveOrder.setOnClickListener { finish() }

    }

}