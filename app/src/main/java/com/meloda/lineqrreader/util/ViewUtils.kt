package com.meloda.lineqrreader.util

import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.meloda.lineqrreader.R
import com.meloda.lineqrreader.common.AppGlobal

object ViewUtils {
    fun showErrorSnackbar(view: View, message: String?) {
        Snackbar.make(
            view,
            message ?: AppGlobal.resources.getString(R.string.error_default_text),
            Snackbar.LENGTH_LONG
        ).show()
    }

    fun showErrorSnackbar(view: View, t: Throwable) {
        Snackbar.make(
            view,
            Utils.getLocalizedThrowable(t),
            Snackbar.LENGTH_LONG
        ).show()
    }
}