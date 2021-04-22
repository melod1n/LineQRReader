package com.meloda.lineqrreader.activity.ui.view

import com.meloda.lineqrreader.adapter.SimpleItemAdapter
import moxy.MvpView
import moxy.viewstate.strategy.alias.OneExecution
import moxy.viewstate.strategy.alias.Skip

interface ScanView : MvpView {

    @OneExecution
    fun prepareViews()

    @OneExecution
    fun setRecyclerViewAdapter(adapter: SimpleItemAdapter)

    @Skip
    fun setMenuDeleteItemVisible(isVisible: Boolean)

    @Skip
    fun invalidateTitleCounter()

}