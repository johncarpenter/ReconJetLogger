package com.twolinessoftware.reconfirebaselogger.model

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = arrayOf(DataPoint::class), version = 1)
abstract class Database : RoomDatabase() {
    abstract fun dataPointDao(): DataPointDao
}
