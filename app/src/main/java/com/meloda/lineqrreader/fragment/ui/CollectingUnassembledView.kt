package com.meloda.lineqrreader.fragment.ui

import com.meloda.lineqrreader.adapter.InventoryAdapter
import com.meloda.lineqrreader.listener.OnCompleteListener
import moxy.MvpView
import moxy.viewstate.strategy.alias.OneExecution

interface CollectingUnassembledView : MvpView {

    @OneExecution
    fun prepareViews()

    @OneExecution
    fun createAdapter(listener: OnCompleteListener<InventoryAdapter>)

}