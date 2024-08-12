package com.sansantek.sansanmulmul.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.io.Serializable

@Entity(tableName = "StepCounter", primaryKeys = ["crewId"])
data class StepCount(var crewId: Int = -1, @ColumnInfo(name = "stepCount")var stepCount: Int = 0, @ColumnInfo(name = "elevation")var elevation: Double = -1.0):Serializable
