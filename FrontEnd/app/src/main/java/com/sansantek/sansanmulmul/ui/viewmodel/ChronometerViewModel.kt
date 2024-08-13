package com.sansantek.sansanmulmul.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sansantek.sansanmulmul.config.ApplicationClass
import com.sansantek.sansanmulmul.config.ApplicationClass.Companion.sharedPreferencesUtil

class ChronometerViewModel() : ViewModel() {
    private var _baseTime =
        MutableLiveData<Long>(ApplicationClass.sharedPreferencesUtil.getHikingRecordingBaseTime())
    val baseTime: LiveData<Long>
        get() = _baseTime

    fun setBaseTime(baseTime: Long) {
        _baseTime.value = baseTime
    }
}