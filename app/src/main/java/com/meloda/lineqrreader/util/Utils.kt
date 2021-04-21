package com.meloda.lineqrreader.util

import com.meloda.lineqrreader.R
import com.meloda.lineqrreader.common.AppGlobal

object Utils {

    fun getLocalizedThrowable(t: Throwable): String {
        return AppGlobal.resources.getString(
            R.string.error_with_message,
            t.message.toString()
        )
    }

}