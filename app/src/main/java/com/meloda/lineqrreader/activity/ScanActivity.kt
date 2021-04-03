package com.meloda.lineqrreader.activity

import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.viewbinding.library.activity.viewBinding
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.meloda.lineqrreader.activity.ui.presenter.ScanPresenter
import com.meloda.lineqrreader.activity.ui.view.ScanView
import com.meloda.lineqrreader.adapter.SimpleItemAdapter
import com.meloda.lineqrreader.base.BaseAdapter
import com.meloda.lineqrreader.databinding.ActivityScanBinding

class ScanActivity : AppCompatActivity(), BaseAdapter.ItemClickListener, ScanView {

    private val binding: ActivityScanBinding by viewBinding()

    private lateinit var presenter: ScanPresenter

    private var isMenuDeleteItemVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        presenter = ScanPresenter(this)
        presenter.onCreate(this, savedInstanceState)
    }

    override fun prepareViews() {
        prepareRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
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

    override fun setRecyclerViewAdapter(adapter: SimpleItemAdapter) {
        binding.recyclerView.adapter = adapter.also { it.itemClickListener = this }
    }

    override fun setMenuDeleteItemVisible(isVisible: Boolean) {
        isMenuDeleteItemVisible = isVisible
        invalidateOptionsMenu()
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
        val item = menu.add("Delete")
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val item = menu.getItem(0)

        item.isVisible = isMenuDeleteItemVisible

        presenter.setDeleteMenuItemClickListener(item)

        return super.onPrepareOptionsMenu(menu)
    }

    //default MvpView methods
    override fun hideErrorView() {
        TODO("Not yet implemented")
    }

    override fun hideNoInternetView() {
        TODO("Not yet implemented")
    }

    override fun hideNoItemsView() {
        TODO("Not yet implemented")
    }

    override fun hideProgressBar() {
        TODO("Not yet implemented")
    }

    override fun hideRefreshLayout() {
        TODO("Not yet implemented")
    }

    override fun prepareErrorView() {
        TODO("Not yet implemented")
    }

    override fun prepareNoInternetView() {
        TODO("Not yet implemented")
    }

    override fun prepareNoItemsView() {
        TODO("Not yet implemented")
    }

    override fun showErrorSnackbar(t: Throwable) {
        TODO("Not yet implemented")
    }

    override fun showErrorView() {
        TODO("Not yet implemented")
    }

    override fun showNoInternetView() {
        TODO("Not yet implemented")
    }

    override fun showNoItemsView() {
        TODO("Not yet implemented")
    }

    override fun showProgressBar() {
        TODO("Not yet implemented")
    }

    override fun showRefreshLayout() {
        TODO("Not yet implemented")
    }

}