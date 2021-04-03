package com.meloda.lineqrreader.common

import android.app.Application
import android.hardware.camera2.CameraManager
import androidx.room.Room
import com.meloda.lineqrreader.database.AppDatabase

class AppGlobal : Application() {

    companion object {
        lateinit var cameraManager: CameraManager
        lateinit var database: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(this, AppDatabase::class.java, "cache")
            .fallbackToDestructiveMigration()
            .build()

        cameraManager = getSystemService(CAMERA_SERVICE) as CameraManager
    }

}