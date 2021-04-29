package com.meloda.lineqrreader.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.meloda.lineqrreader.database.dao.CollectingDao
import com.meloda.lineqrreader.database.dao.InventoryDao
import com.meloda.lineqrreader.model.CollectionItem
import com.meloda.lineqrreader.model.InventoryItem

@Database(
    entities =
    [InventoryItem::class, CollectionItem::class],
    version = 3,
    exportSchema = false,
//    autoMigrations = [
//        AutoMigration(from = 1, to = 2)
//    ]
)
abstract class AppDatabase : RoomDatabase() {

    abstract val inventory: InventoryDao
    abstract val collecting: CollectingDao

}