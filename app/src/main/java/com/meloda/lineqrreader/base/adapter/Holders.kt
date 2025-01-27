package com.meloda.lineqrreader.base.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseHolder(v: View) : RecyclerView.ViewHolder(v) {

    open fun bind(position: Int) {
        bind(position, mutableListOf())
    }

    open fun bind(position: Int, payloads: MutableList<Any>?) {}

}

abstract class BindingHolder<B : ViewBinding>(protected val binding: B) : BaseHolder(binding.root)