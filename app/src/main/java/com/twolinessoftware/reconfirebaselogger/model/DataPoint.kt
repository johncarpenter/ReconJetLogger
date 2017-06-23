package com.twolinessoftware.reconfirebaselogger.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey



@Entity(tableName = "data_point")
class DataPoint {
        @PrimaryKey(autoGenerate = true)
        var uid: Long = 0

        var timestamp: Long = 0
        var latitude: Double = 0.0
        var longitude: Double = 0.0
        var speed: Float = -1.0f
        var hr: Int = -1
        var cadence: Int = -1
}