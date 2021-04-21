package com.meloda.lineqrreader.common

import android.app.Application
import android.content.res.Resources
import android.hardware.camera2.CameraManager
import androidx.room.Room
import com.meloda.lineqrreader.database.AppDatabase

class AppGlobal : Application() {

    companion object {
        lateinit var cameraManager: CameraManager
        lateinit var database: AppDatabase
        lateinit var resources: Resources
    }

    override fun onCreate() {
        super.onCreate()

        Companion.resources = resources

        database = Room.databaseBuilder(this, AppDatabase::class.java, "cache")
            .fallbackToDestructiveMigration()
            .build()

        cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
    }

}