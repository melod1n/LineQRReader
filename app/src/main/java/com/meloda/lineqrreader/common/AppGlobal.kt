package com.meloda.lineqrreader.common

import android.app.Application
import android.content.res.Resources
import android.hardware.camera2.CameraManager
import android.view.inputmethod.InputMethodManager
import androidx.room.Room
import com.meloda.lineqrreader.database.AppDatabase

class AppGlobal : Application() {

    companion object {
        lateinit var database: AppDatabase
        lateinit var resources: Resources

        //services
        lateinit var cameraManager: CameraManager
        lateinit var inputMethodManager: InputMethodManager
    }

    override fun onCreate() {
        super.onCreate()

        Companion.resources = resources

        database = Room.databaseBuilder(this, AppDatabase::class.java, "cache")
            .fallbackToDestructiveMigration()
            .build()

        //init services
        cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
        inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    }

}