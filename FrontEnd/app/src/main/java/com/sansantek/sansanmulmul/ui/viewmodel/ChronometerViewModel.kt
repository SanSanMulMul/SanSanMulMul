package com.sansantek.sansanmulmul.ui.viewmodel

import android.os.SystemClock
import androidx.lifecycle.ViewModel
import com.sansantek.sansanmulmul.data.local.SharedPreferencesUtil

class ChronometerViewModel(private val sharedPreferencesUtil: SharedPreferencesUtil) : ViewModel() {
    private var spendTime: Long = 0L
    private var isRunning: Boolean = false

    init {
        spendTime = sharedPreferencesUtil.getHikingRecordingTime()
        isRunning = sharedPreferencesUtil.getHikingRecordingTimeIsRunning()
    }

    fun startChronometer(baseTime: Long) {
        if (!isRunning) {
            spendTime = baseTime
            isRunning = true

            sharedPreferencesUtil.saveHikingRecordingTime(spendTime)
            sharedPreferencesUtil.saveHikingRecordingTimeIsRunning(isRunning)
        }
    }

    fun stopChronometer() {
        isRunning = false

        sharedPreferencesUtil.saveHikingRecordingTime(spendTime)
        sharedPreferencesUtil.saveHikingRecordingTimeIsRunning(isRunning)
    }

    fun getSpendTime(): Long {
        return spendTime
    }

    fun isRunning(): Boolean {
        return isRunning
    }


    private fun chronometerToMilliSeconds(chronometerTime: Long): Long {
        return (SystemClock.elapsedRealtime() - chronometerTime) / 1000
    }
}
