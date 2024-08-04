package com.sansantek.sansanmulmul.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sansantek.sansanmulmul.config.ApplicationClass.Companion.sharedPreferencesUtil
import com.sansantek.sansanmulmul.config.Const.Companion.BANNED
import com.sansantek.sansanmulmul.config.Const.Companion.BEFORE_HIKING


private const val TAG = "HikingRecordingTabViewModel_싸피"
class HikingRecordingTabViewModel() :
    ViewModel() {
    private val _recordingStatus = MutableLiveData<Int>(sharedPreferencesUtil.getHikingRecordingState())
    val recordingStatus: LiveData<Int> get() = _recordingStatus

    fun setRecordingStatus(recordingStatus: Int) {
        _recordingStatus.value = recordingStatus
        sharedPreferencesUtil.saveHikingRecordingState(recordingStatus)
    }

    fun deleteState() {
        sharedPreferencesUtil.deleteHikingRecordingState()
    }
}