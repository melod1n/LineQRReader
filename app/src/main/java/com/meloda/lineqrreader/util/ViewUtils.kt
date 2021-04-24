package com.meloda.lineqrreader.util

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.meloda.lineqrreader.R
import com.meloda.lineqrreader.base.BaseActivity
import com.meloda.lineqrreader.base.BaseFragment
import com.meloda.lineqrreader.common.AppGlobal

object ViewUtils {

    fun createErrorSnackbar(context: Fragment, message: String?): Snackbar {
        val snackbar = Snackbar.make(
            context.requireView(),
            message ?: AppGlobal.resources.getString(R.string.error_default_text),
            Snackbar.LENGTH_LONG
        )

        if (context is BaseFragment) context.errorSnackbar = snackbar

        return snackbar
    }

    fun createErrorSnackbar(context: Activity, message: String?): Snackbar {
        val snackbar = Snackbar.make(
            context.findViewById(android.R.id.content),
            message ?: AppGlobal.resources.getString(R.string.error_default_text),
            Snackbar.LENGTH_LONG
        )

        if (context is BaseActivity) context.errorSnackbar = snackbar

        return snackbar
    }

    fun showErrorSnackbar(view: View, message: String?): Snackbar {
        val snackbar = Snackbar.make(
            view,
            message ?: AppGlobal.resources.getString(R.string.error_default_text),
            Snackbar.LENGTH_LONG
        )
        snackbar.show()
        return snackbar
    }

    fun showErrorSnackbar(view: View, t: Throwable): Snackbar {
        val snackbar = Snackbar.make(
            view,
            Utils.getLocalizedThrowable(t),
            Snackbar.LENGTH_LONG
        )
        snackbar.show()
        return snackbar
    }
}