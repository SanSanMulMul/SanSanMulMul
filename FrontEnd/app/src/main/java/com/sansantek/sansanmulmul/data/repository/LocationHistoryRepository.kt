package com.sansantek.sansanmulmul.data.repository

import android.content.Context
import android.util.Log
import com.sansantek.sansanmulmul.data.db.LocationHistoryDatabase
import com.sansantek.sansanmulmul.data.db.StepCountDatabase
import com.sansantek.sansanmulmul.data.local.entity.LocationHistory
import com.sansantek.sansanmulmul.data.local.entity.StepCount

private const val TAG = "LocationHistoryRepository_싸피"
class LocationHistoryRepository private constructor(context: Context) {

    private val database : LocationHistoryDatabase = LocationHistoryDatabase.getDatabase(context)

    private val locationHistoryDao = database.locationHistoryDao()

    suspend fun getLocationHistory(crewId: Int): List<LocationHistory>? {
        return locationHistoryDao.getRecordHistory(crewId)
    }

    suspend fun insertLocationHistory(locationHistory: LocationHistory){
        locationHistoryDao.insertLocationHistory(locationHistory)
    }

    companion object{
        private var INSTANCE : LocationHistoryRepository? =null

        fun initialize(context: Context){
            if(INSTANCE == null){
                Log.d(TAG, "initialize: 레포지토리 이니셜")
                INSTANCE = LocationHistoryRepository(context)
            }
        }

        fun get() : LocationHistoryRepository {
            return INSTANCE ?:
            throw IllegalStateException("StepRepository must be initialized")
        }
    }
}