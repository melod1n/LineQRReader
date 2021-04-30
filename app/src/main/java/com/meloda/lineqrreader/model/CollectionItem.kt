package com.meloda.lineqrreader.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "collection")
class CollectionItem : BaseSelectionItem() {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    var content: String = ""
    var count: Int = 0
    var number: String = ""

    @Ignore
    var error: String = ""

    fun isError() = error.isNotEmpty()

}