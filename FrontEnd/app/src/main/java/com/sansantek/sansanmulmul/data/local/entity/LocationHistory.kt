package com.sansantek.sansanmulmul.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.time.LocalDateTime

@Entity(tableName = "LocationHistory", primaryKeys = ["crewId", "time"])
data class LocationHistory(var crewId: Int = -1, var time: String, @ColumnInfo(name = "latitude")var latitude: Double = -1.0, @ColumnInfo(name = "longitude")var longitude: Double = -1.0, @ColumnInfo(name = "altitude")var altitude: Double = -1.0)
