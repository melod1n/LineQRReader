package com.meloda.lineqrreader.extensions

import android.widget.TextView

object TextViewExtensions {

    fun TextView.isEmpty() = toString().trim().isEmpty()

}