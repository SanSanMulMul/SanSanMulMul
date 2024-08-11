package com.sansantek.sansanmulmul.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sansantek.sansanmulmul.config.ApplicationClass.Companion.sharedPreferencesUtil
import com.sansantek.sansanmulmul.data.repository.HikingRecordingRepository
import kotlinx.coroutines.launch


private const val TAG = "HikingRecordingTabViewModel_싸피"

class HikingRecordingTabViewModel :
  ViewModel() {
  private val repository = HikingRecordingRepository()
  private val _recordingStatus =
    MutableLiveData(sharedPreferencesUtil.getHikingRecordingState())
  val recordingStatus: LiveData<Int> get() = _recordingStatus

  private val _onGoingCrewId =
    MutableLiveData(sharedPreferencesUtil.getHikingRecordingState())
  val onGoingCrewId: LiveData<Int> get() = _onGoingCrewId

  private val _isQRScanned =
    MutableLiveData(sharedPreferencesUtil.getIsQRScanned())
  val isQRScanned: LiveData<Boolean> get() = _isQRScanned

  private val _amILeader = MutableLiveData<Boolean?>()
  val amILeader: LiveData<Boolean?> get() = _amILeader

  private val _error = MutableLiveData<String>()
  val error: LiveData<String> get() = _error

  fun setRecordingStatus(recordingStatus: Int) {
    _recordingStatus.value = recordingStatus
    sharedPreferencesUtil.saveHikingRecordingState(recordingStatus)
  }

  fun deleteState() {
    sharedPreferencesUtil.deleteHikingRecordingState()
  }

  fun setOnGoingCrewId(crewId: Int) {
    _onGoingCrewId.value = crewId
    sharedPreferencesUtil.saveHikingRecordingOnGoingCrewId(crewId)
  }

  fun deleteOnGoingCrewId() {
    sharedPreferencesUtil.deleteHikingRecordingOnGoingCrewId()
  }

  fun amILeader(accessToken: String, crewId: Int) {
    viewModelScope.launch {
      try {
        val response = repository.amILeader(accessToken, crewId)
        if (response != null) {
          _amILeader.postValue(response)
        } else {
          _error.postValue("데이터를 불러오는 데 실패했습니다!ㅠ.ㅠ")
        }
      } catch (e: Exception) {
        _error.postValue("Error: ${e.message}")
      }
    }
  }

}