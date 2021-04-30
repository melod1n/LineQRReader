package com.meloda.lineqrreader.database.dao

import androidx.room.*
import com.meloda.lineqrreader.model.CollectionItem

@Dao
interface CollectingDao {

    @Query("SELECT * FROM collection")
    suspend fun getAll(): List<CollectionItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CollectionItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<CollectionItem>)

    @Delete
    suspend fun delete(item: CollectionItem)

    @Delete
    suspend fun delete(items: List<CollectionItem>)

    @Query("DELETE FROM collection WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM collection")
    suspend fun clear()

}