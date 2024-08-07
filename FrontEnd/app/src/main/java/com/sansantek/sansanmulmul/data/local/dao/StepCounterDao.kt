package com.sansantek.sansanmulmul.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.sansantek.sansanmulmul.data.local.entity.StepCount

@Dao
interface StepCounterDao {
    @Query("select * from StepCounter where day = (:day) and month = (:month) and year = (:year) and status = (:status)")
    suspend fun getStep(year: Int, month: Int, day: Int, status: String): StepCount?

    @Insert
    suspend fun insertStep(stepCount: StepCount)

    @Update
    suspend fun updateStep(stepCount: StepCount)
}
