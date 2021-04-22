package com.meloda.lineqrreader.base

import androidx.annotation.LayoutRes
import moxy.MvpAppCompatFragment

class BaseFragment : MvpAppCompatFragment {

    constructor() : super()

    constructor(@LayoutRes resId: Int) : super(resId)

}