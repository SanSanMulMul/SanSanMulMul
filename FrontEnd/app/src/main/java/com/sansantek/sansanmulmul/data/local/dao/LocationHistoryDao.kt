package com.sansantek.sansanmulmul.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.sansantek.sansanmulmul.data.local.entity.LocationHistory
import com.sansantek.sansanmulmul.data.local.entity.StepCount

@Dao
interface LocationHistoryDao {
    @Query("select * from LocationHistory where crewId = (:crewId)")
    suspend fun getRecordHistory(crewId: Int): List<LocationHistory>?

    @Insert
    suspend fun insertLocationHistory(locationHistory: LocationHistory)
}
