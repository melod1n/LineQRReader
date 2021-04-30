package com.meloda.lineqrreader.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.google.android.material.snackbar.Snackbar
import moxy.MvpAppCompatActivity

abstract class BaseActivity : MvpAppCompatActivity, LifecycleOwner {

    constructor() : super()

    constructor(@LayoutRes resId: Int) : super(resId)

    protected lateinit var lifecycleRegistry: LifecycleRegistry

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleRegistry = LifecycleRegistry(this)
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
    }

    override fun onStart() {
        super.onStart()
        lifecycleRegistry.currentState = Lifecycle.State.STARTED
    }

    override fun onResume() {
        super.onResume()
        lifecycleRegistry.currentState = Lifecycle.State.RESUMED
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
    }

    val rootView: View? get() = findViewById(android.R.id.content)

    fun requireRootView() = rootView!!

    var errorSnackbar: Snackbar? = null

}