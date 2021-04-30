package com.meloda.lineqrreader.activity

import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.viewbinding.library.activity.viewBinding
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.meloda.lineqrreader.R
import com.meloda.lineqrreader.activity.ui.InventoryPresenter
import com.meloda.lineqrreader.activity.ui.InventoryView
import com.meloda.lineqrreader.adapter.InventoryAdapter
import com.meloda.lineqrreader.base.BaseActivity
import com.meloda.lineqrreader.base.adapter.OnItemClickListener
import com.meloda.lineqrreader.databinding.ActivityInventoryBinding
import com.meloda.lineqrreader.dialog.BarcodeDialog
import com.meloda.lineqrreader.extensions.MoxyExtensions.viewPresenter
import com.meloda.lineqrreader.extensions.StringExtensions.lowerCase
import com.meloda.lineqrreader.listener.OnCompleteListener
import com.meloda.lineqrreader.view.DividerItemDecoration
import kotlinx.coroutines.launch

class InventoryActivity() :
    BaseActivity(R.layout.activity_inventory),
    OnItemClickListener,
    InventoryView,
    InventoryAdapter.OnSuggestDeleteListener {

    private val binding: ActivityInventoryBinding by viewBinding()
    private val presenter: InventoryPresenter by viewPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = "Nikolaev D.S."

        binding.cellCount.text = getString(R.string.count_short).lowerCase()
        binding.cellNumber.text = getString(R.string.cell_num)

        binding.barcode.setOnClickListener {
            BarcodeDialog(presenter.adapter, -1, true).apply {
                onDoneListener = object : BarcodeDialog.OnDoneListener {
                    override fun onDone(result: String) {
                        lifecycleScope.launch { presenter.addItem(result) }
                    }
                }
            }.show(
                supportFragmentManager,
                "barcode_dialog"
            )
        }

        binding.end.setOnClickListener {
            lifecycleScope.launch { presenter.saveAllToDatabase() }
            finish()
        }
    }

    fun setCell(cell: String) {
        title = "Nikolaev D.S. " + if (cell.isNotEmpty()) "($cell)" else ""
    }

    override fun prepareViews() {
        prepareRecyclerView()
        prepareToolbar()
    }

    override fun onResume() {
        super.onResume()
        presenter.initScanner()
    }

    override fun onPause() {
        super.onPause()
        presenter.releaseScanner()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (presenter.onKeyDown(keyCode)) return true
        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (presenter.onKeyUp(keyCode)) return true
        return super.onKeyUp(keyCode, event)
    }

    override fun onItemClick(position: Int) {
        presenter.setAdapterError(position, "Some error")
    }

    private fun prepareRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        binding.recyclerView.addItemDecoration(DividerItemDecoration(this, R.drawable.ic_divider))
    }

    private fun prepareToolbar() {
        with(binding.toolbar) {
            setNavigationIconTint(Color.WHITE)
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) onBackPressed()
        return super.onOptionsItemSelected(item)
    }

    override fun createAdapter(listener: OnCompleteListener<InventoryAdapter>) {
        val adapter = InventoryAdapter(this).also {
            it.itemClickListener = this
            it.onSuggestDeleteListener = this
        }
        binding.recyclerView.adapter = adapter

        listener.onComplete(adapter)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        presenter.onRequestPermissionsResult(requestCode, grantResults)
    }

    override fun onSuggest(position: Int) {
        presenter.showDeleteItemDialog(position)
    }

    override fun onBackPressed() {
        if (!presenter.isFirstScan) presenter.removeCell()
        else super.onBackPressed()
    }

}