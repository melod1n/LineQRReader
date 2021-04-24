package com.meloda.lineqrreader.fragment.ui

import moxy.MvpView
import moxy.viewstate.strategy.alias.Skip

interface InputNumberView : MvpView {

    @Skip
    fun hideKeyboard()

}