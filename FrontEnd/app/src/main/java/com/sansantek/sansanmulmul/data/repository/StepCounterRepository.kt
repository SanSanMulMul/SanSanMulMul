package com.sansantek.sansanmulmul.data.repository

import android.content.Context
import android.util.Log
import com.sansantek.sansanmulmul.BuildConfig
import com.sansantek.sansanmulmul.data.db.StepCountDatabase
import com.sansantek.sansanmulmul.data.local.entity.StepCount

private const val TAG = "StepCounterRepository_싸피"


class StepCounterRepository private constructor(context: Context) {

    private val database : StepCountDatabase = StepCountDatabase.getDatabase(context)

    private val stepCounterDao = database.stepCounterDao()

    suspend fun getStepCount(year: Int, month: Int, day: Int): StepCount? {
        return stepCounterDao.getStep(year, month, day)
    }

    suspend fun insertStepCount(stepCount: StepCount){
        stepCounterDao.insertStep(stepCount)
    }

    suspend fun updateStepCount(stepCount: StepCount){
        stepCounterDao.updateStep(stepCount)
    }


    companion object{
        private var INSTANCE : StepCounterRepository? =null

        fun initialize(context: Context){
            if(INSTANCE == null){
                Log.d(TAG, "initialize: 레포지토리 이니셜")
                INSTANCE = StepCounterRepository(context)
            }
        }

        fun get() : StepCounterRepository {
            return INSTANCE ?:
            throw IllegalStateException("StepRepository must be initialized")
        }
    }
}