package com.sansantek.sansanmulmul.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "StepCounter", primaryKeys = ["year", "month", "day", "status"])
data class StepCount(var year: Int = -1, var month: Int = -1, var day: Int = -1, var status: String = "", @ColumnInfo(name = "stepCount")var stepCount: Int = -1)
