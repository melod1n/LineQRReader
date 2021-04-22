package com.meloda.lineqrreader.activity

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.viewbinding.library.activity.viewBinding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.meloda.lineqrreader.R
import com.meloda.lineqrreader.activity.ui.presenter.ScanPresenter
import com.meloda.lineqrreader.activity.ui.view.ScanView
import com.meloda.lineqrreader.adapter.SimpleItemAdapter
import com.meloda.lineqrreader.base.BaseActivity
import com.meloda.lineqrreader.base.adapter.OnItemClickListener
import com.meloda.lineqrreader.databinding.ActivityScanBinding
import moxy.ktx.moxyPresenter

class ScanActivity : BaseActivity(R.layout.activity_scan), OnItemClickListener, ScanView {

    private val binding: ActivityScanBinding by viewBinding()
    private val presenter: ScanPresenter by moxyPresenter { ScanPresenter(this) }

    private var isMenuDeleteItemVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startActivity(Intent(this, AuthActivity::class.java))
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
        presenter.toggleAdapterItemSelection(position)
    }

    private fun prepareRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    private fun prepareToolbar() {
        setSupportActionBar(binding.toolbar)
    }

    override fun setRecyclerViewAdapter(adapter: SimpleItemAdapter) {
        binding.recyclerView.adapter = adapter.also { it.itemClickListener = this }
    }

    override fun setMenuDeleteItemVisible(isVisible: Boolean) {
        isMenuDeleteItemVisible = isVisible
        invalidateOptionsMenu()
    }

    override fun invalidateTitleCounter() {
        title = if (presenter.itemCount == 0) getString(R.string.app_name)
        else "%s (%d)".format(getString(R.string.app_name), presenter.itemCount)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        presenter.onRequestPermissionsResult(requestCode, grantResults)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_scan, menu)

        val item = menu.findItem(R.id.menuDelete)
        presenter.setDeleteMenuItemClickListener(item)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val item = menu.findItem(R.id.menuDelete)

        item.isVisible = isMenuDeleteItemVisible

        return super.onPrepareOptionsMenu(menu)
    }

}