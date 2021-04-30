package com.meloda.lineqrreader.activity.ui

import com.meloda.lineqrreader.adapter.InventoryAdapter
import com.meloda.lineqrreader.listener.OnCompleteListener
import moxy.MvpView
import moxy.viewstate.strategy.alias.OneExecution

interface InventoryView : MvpView {

    @OneExecution
    fun prepareViews()

    @OneExecution
    fun createAdapter(listener: OnCompleteListener<InventoryAdapter>)

}