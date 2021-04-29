package com.meloda.lineqrreader.database.dao

import androidx.room.*
import com.meloda.lineqrreader.model.InventoryItem

@Dao
interface InventoryDao {

    @Query("SELECT * FROM items")
    suspend fun getAll(): List<InventoryItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: InventoryItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<InventoryItem>)

    @Delete
    suspend fun delete(item: InventoryItem)

    @Delete
    suspend fun delete(items: List<InventoryItem>)

    @Query("DELETE FROM items WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM items")
    suspend fun clear()

}