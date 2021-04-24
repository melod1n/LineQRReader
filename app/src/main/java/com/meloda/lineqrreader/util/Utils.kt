package com.meloda.lineqrreader.util

import com.meloda.lineqrreader.R
import com.meloda.lineqrreader.common.AppGlobal

object Utils {

    const val PHONE_NUMBER_PATTERN = "^(\\+7|7|8)?[\\s\\-]?\\(?[489][0-9]{2}\\)?[\\s\\-]?[0-9]{3}[\\s\\-]?[0-9]{2}[\\s\\-]?[0-9]{2}\$"

    fun getLocalizedThrowable(t: Throwable): String {
        return AppGlobal.resources.getString(
            R.string.error_with_message,
            t.message.toString()
        )
    }

}