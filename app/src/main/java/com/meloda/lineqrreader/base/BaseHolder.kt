package com.meloda.lineqrreader.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseHolder(v: View) : RecyclerView.ViewHolder(v) {

    open fun bind(position: Int) {
        bind(position, mutableListOf())
    }

    open fun bind(position: Int, payloads: MutableList<Any>?) {}

}