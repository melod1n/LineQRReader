package com.meloda.lineqrreader.listener

interface OnCompleteListener<T> {

    fun onComplete(item: T)

}

interface OnCompleteArrayListener<T> {

    fun onCompleteArray(items: ArrayList<T>)

}