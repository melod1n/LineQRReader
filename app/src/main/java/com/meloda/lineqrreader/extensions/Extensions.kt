package com.meloda.lineqrreader.extensions

import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.fragment.app.FragmentTransaction
import com.meloda.lineqrreader.R

object Extensions {

    fun FragmentTransaction.withAnimations(
        @AnimatorRes @AnimRes enter: Int = R.anim.activity_open_enter,
        @AnimatorRes @AnimRes exit: Int = R.anim.activity_close_exit,
        @AnimatorRes @AnimRes popEnter: Int = R.anim.activity_open_enter,
        @AnimatorRes @AnimRes popExit: Int = R.anim.activity_close_exit
    ): FragmentTransaction {
        setCustomAnimations(enter, exit, popEnter, popExit)
        return this
    }

}