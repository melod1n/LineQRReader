package com.meloda.lineqrreader.database.dao

import androidx.room.*
import com.meloda.lineqrreader.model.SimpleItem

@Dao
interface SimpleItemDao {

    @Query("SELECT * FROM items")
    suspend fun getAll(): List<SimpleItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: SimpleItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<SimpleItem>)

    @Delete
    suspend fun delete(item: SimpleItem)

    @Delete
    suspend fun delete(items: List<SimpleItem>)

    @Query("DELETE FROM items WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM items")
    suspend fun clear()

}