package com.meloda.lineqrreader.listener

interface ScannerResultListener {

    fun onResult(sym: String, content: String)

}