package com.meloda.lineqrreader.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
class SimpleItem : BaseSelectionItem() {

    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    var content: String = ""

}