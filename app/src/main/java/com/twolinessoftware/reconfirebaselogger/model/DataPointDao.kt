package com.twolinessoftware.reconfirebaselogger.model

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert

@Dao
interface DataPointDao {

    @Insert
    fun insert(point: DataPoint)
}