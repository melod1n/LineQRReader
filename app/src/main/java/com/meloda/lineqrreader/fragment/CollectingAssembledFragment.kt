package com.meloda.lineqrreader.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.meloda.lineqrreader.R
import com.meloda.lineqrreader.activity.OrderCollectedActivity
import com.meloda.lineqrreader.adapter.InventoryAdapter
import com.meloda.lineqrreader.base.BaseFragment
import com.meloda.lineqrreader.base.adapter.OnItemClickListener
import com.meloda.lineqrreader.databinding.FragmentCollectingAssembledBinding
import com.meloda.lineqrreader.extensions.MoxyExtensions.viewPresenter
import com.meloda.lineqrreader.extensions.StringExtensions.lowerCase
import com.meloda.lineqrreader.fragment.ui.CollectingAssembledPresenter
import com.meloda.lineqrreader.fragment.ui.CollectingAssembledView
import com.meloda.lineqrreader.listener.OnCompleteListener
import com.meloda.lineqrreader.util.Utils
import com.meloda.lineqrreader.view.DividerItemDecoration
import java.util.*

class CollectingAssembledFragment :
    BaseFragment(R.layout.fragment_collecting_assembled),
    CollectingAssembledView,
    OnItemClickListener,
    InventoryAdapter.OnSuggestDeleteListener {

    private val binding: FragmentCollectingAssembledBinding by viewBinding()
    private val presenter: CollectingAssembledPresenter by viewPresenter()

    var maxElements: Int = 0
    var currentItems: Int = 0

    var startCollectingTime = 0L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cellCount.text = getString(R.string.count_short).lowerCase()
        binding.cellNumber.text = getString(R.string.cell_num)

        binding.end.setOnClickListener {
            with(requireActivity()) {
                startActivity(
                    Intent(this, OrderCollectedActivity::class.java).putExtra(
                        "collectingTime", startCollectingTime
                    )
                )
                finish()
            }
        }
    }

    override fun prepareViews() {
        prepareRecyclerView()
        prepareToolbar()
    }

    private fun prepareRecyclerView() {
        binding.recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                R.drawable.ic_divider
            )
        )
    }

    private fun prepareToolbar() {
        binding.title.text = "Nikolaev D.S."
    }

    override fun createAdapter(listener: OnCompleteListener<InventoryAdapter>) {
        val adapter = InventoryAdapter(requireContext()).also {
            it.itemClickListener = this
            it.onSuggestDeleteListener = this
        }
        binding.recyclerView.adapter = adapter

        listener.onComplete(adapter)
    }

    override fun onItemClick(position: Int) {

    }

    override fun onSuggest(position: Int) {
        presenter.showDeleteItemDialog(position)
    }

    fun updateTimer(calendar: Calendar) {
        binding.time.text = Utils.getLocalizedTime(
            requireContext(),
            calendar[Calendar.MINUTE],
            calendar[Calendar.SECOND]
        )
    }

    fun addItem(content: String) {
        currentItems++
        presenter.addItem(content)

        if (currentItems == maxElements) {
            binding.end.isVisible = true
            return
        } else {
            binding.end.isVisible = false
        }
    }
}