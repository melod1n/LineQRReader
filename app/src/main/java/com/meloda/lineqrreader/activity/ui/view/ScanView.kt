package com.meloda.lineqrreader.activity.ui.view

import com.meloda.lineqrreader.adapter.SimpleItemAdapter
import com.meloda.mvp.MvpView

interface ScanView : MvpView {

    fun prepareViews()

    fun setRecyclerViewAdapter(adapter: SimpleItemAdapter)

    fun setMenuDeleteItemVisible(isVisible: Boolean)

    fun invalidateTitleCounter()

}