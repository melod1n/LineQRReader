package com.meloda.lineqrreader.util

import android.content.Context
import com.meloda.lineqrreader.R
import com.meloda.lineqrreader.common.AppConstants
import com.meloda.lineqrreader.common.AppGlobal
import com.meloda.lineqrreader.extensions.StringExtensions.lowerCase

object Utils {

    fun getLocalizedThrowable(t: Throwable): String {
        return AppGlobal.resources.getString(
            R.string.error_with_message,
            t.message.toString()
        )
    }

    fun getLocalizedTime(context: Context, minutes: Int, seconds: Int): String {
        return AppConstants.TIMER_TEXT_PATTERN.format(
            addZero(minutes),
            context.getString(R.string.minute_short).lowerCase(),
            addZero(seconds),
            context.getString(R.string.second_short).lowerCase()
        )
    }

    fun addZero(number: Int): String {
        return if (number == 0) "0"
        else if (number < 10) "0$number"
        else number.toString()
    }

    fun isNumberValid(string: String) = string.matches(Regex(AppConstants.PHONE_NUMBER_PATTERN))
}