package com.meloda.lineqrreader.extensions

import java.util.*

object StringExtensions {

    fun String?.lowerCase() = this?.toLowerCase(Locale.getDefault()) ?: ""

    fun String?.upperCase() = this?.toUpperCase(Locale.getDefault()) ?: ""

}