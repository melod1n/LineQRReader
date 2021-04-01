package com.meloda.lineqrreader.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.meloda.lineqrreader.database.dao.SimpleItemDao
import com.meloda.lineqrreader.model.SimpleItem

@Database(
    entities = [SimpleItem::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract val itemsDao: SimpleItemDao

}