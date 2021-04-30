package com.meloda.lineqrreader.common

import android.app.Application
import android.content.SharedPreferences
import android.content.res.Resources
import android.hardware.camera2.CameraManager
import android.view.inputmethod.InputMethodManager
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.meloda.lineqrreader.database.AppDatabase


class AppGlobal : Application() {

    companion object {
        lateinit var resources: Resources
        lateinit var database: AppDatabase
        lateinit var preferences: SharedPreferences

        //services
        lateinit var cameraManager: CameraManager
        lateinit var inputMethodManager: InputMethodManager

        //for testing on non-scanner devices
        const val forActualScanner = true
    }

    override fun onCreate() {
        super.onCreate()

        Companion.resources = resources

        database = Room.databaseBuilder(this, AppDatabase::class.java, "cache")
            .fallbackToDestructiveMigration()
            .build()

        preferences = PreferenceManager.getDefaultSharedPreferences(this)

        //init services
        cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
        inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    }

}