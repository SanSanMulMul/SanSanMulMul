package com.sansantek.sansanmulmul.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sansantek.sansanmulmul.config.Const.Companion.BEFORE_HIKING
import com.sansantek.sansanmulmul.data.local.SharedPreferencesUtil

class HikingRecordingTabViewModel(private val sharedPreferencesUtil: SharedPreferencesUtil) :
    ViewModel() {
    private val _state = MutableLiveData<Int>()
    val state: LiveData<Int> get() = _state

    init {
        _state.value = sharedPreferencesUtil.getHikingRecordingState()
    }

    fun setState(newState: Int) {
        _state.value = newState
        sharedPreferencesUtil.saveHikingRecordingState(newState)
    }

    fun getState(): Int {
        return _state.value ?: BEFORE_HIKING
    }

    fun deleteState() {
        sharedPreferencesUtil.deleteHikingRecordingState()
    }
}