package com.meloda.lineqrreader.fragment

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.meloda.lineqrreader.R
import com.meloda.lineqrreader.adapter.InventoryAdapter
import com.meloda.lineqrreader.base.BaseFragment
import com.meloda.lineqrreader.base.adapter.OnItemClickListener
import com.meloda.lineqrreader.databinding.FragmentCollectingUnassembledBinding
import com.meloda.lineqrreader.extensions.MoxyExtensions.viewPresenter
import com.meloda.lineqrreader.extensions.StringExtensions.lowerCase
import com.meloda.lineqrreader.fragment.ui.CollectingUnassembledPresenter
import com.meloda.lineqrreader.fragment.ui.CollectingUnassembledView
import com.meloda.lineqrreader.listener.OnCompleteListener
import com.meloda.lineqrreader.model.InventoryItem
import com.meloda.lineqrreader.util.Utils
import com.meloda.lineqrreader.view.DividerItemDecoration
import java.util.*

class CollectingUnassembledFragment :
    BaseFragment(R.layout.fragment_collecting_unassembled),
    CollectingUnassembledView,
    OnItemClickListener,
    InventoryAdapter.OnSuggestDeleteListener {

    private val binding: FragmentCollectingUnassembledBinding by viewBinding()
    private val presenter: CollectingUnassembledPresenter by viewPresenter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cellCount.text = getString(R.string.count_short).lowerCase()
        binding.cellNumber.text = getString(R.string.cell_num)
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
        presenter.showBarcodeDialog(position)
    }

    override fun onSuggest(position: Int) {
        presenter.showDeleteItemDialog(position)
    }

    fun initScanner() = presenter.initScanner()
    fun releaseScanner() = presenter.releaseScanner()

    fun onKeyDown(keyCode: Int) = presenter.onKeyDown(keyCode)
    fun onKeyUp(keyCode: Int) = presenter.onKeyUp(keyCode)

    fun updateTimer(calendar: Calendar) {
        binding.time.text = Utils.getLocalizedTime(
            requireContext(),
            calendar[Calendar.MINUTE],
            calendar[Calendar.SECOND]
        )
    }

    fun removeItems(items: ArrayList<InventoryItem>) {
        presenter.removeItemsFromAdapter(items)
    }

}