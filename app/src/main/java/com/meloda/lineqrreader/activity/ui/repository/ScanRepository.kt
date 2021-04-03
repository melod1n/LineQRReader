package com.meloda.lineqrreader.activity.ui.repository

import com.meloda.lineqrreader.common.AppGlobal
import com.meloda.lineqrreader.model.SimpleItem
import com.meloda.mvp.MvpRepository

class ScanRepository : MvpRepository<Any>() {

    suspend fun getCachedItems(): List<SimpleItem> {
        return AppGlobal.database.itemsDao.getAll()
    }

}