package com.meloda.lineqrreader.extensions

import moxy.MvpDelegateHolder
import moxy.MvpPresenter
import moxy.ktx.MoxyKtxDelegate

object MoxyExtensions {

    inline fun <reified T : MvpPresenter<*>> MvpDelegateHolder.viewPresenter(
        name: String = "presenter",
    ): MoxyKtxDelegate<T> {
        return MoxyKtxDelegate(
            mvpDelegate,
            T::class.java.name + "." + name
        ) { (T::class.java).newInstance() }
    }

    inline fun <reified T : MvpPresenter<*>> MvpDelegateHolder.viewPresenter(
        name: String = "presenter",
        noinline factory: () -> T
    ): MoxyKtxDelegate<T> {
        return MoxyKtxDelegate(mvpDelegate, T::class.java.name + "." + name, factory)
    }


}