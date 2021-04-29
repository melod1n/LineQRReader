package com.meloda.lineqrreader.base

import androidx.annotation.LayoutRes
import com.google.android.material.snackbar.Snackbar
import moxy.MvpAppCompatFragment

abstract class BaseFragment : MvpAppCompatFragment {

    constructor() : super()

    constructor(@LayoutRes resId: Int) : super(resId)

    var errorSnackbar: Snackbar? = null



}