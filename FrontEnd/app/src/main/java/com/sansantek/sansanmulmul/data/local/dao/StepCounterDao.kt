package com.sansantek.sansanmulmul.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.sansantek.sansanmulmul.data.local.entity.StepCount

@Dao
interface StepCounterDao {
    @Query("select * from StepCounter where crewId = (:crewId)")
    suspend fun getStep(crewId: Int): StepCount?

    @Insert
    suspend fun insertStep(stepCount: StepCount)

    @Update
    suspend fun updateStep(stepCount: StepCount)
}
