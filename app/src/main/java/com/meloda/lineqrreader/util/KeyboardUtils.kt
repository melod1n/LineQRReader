package com.meloda.lineqrreader.util

import android.view.View
import com.meloda.lineqrreader.common.AppGlobal

object KeyboardUtils {

    fun hideKeyboard(focusedView: View? = null) {
        AppGlobal.inputMethodManager.hideSoftInputFromWindow(focusedView?.windowToken, 0)
    }

}