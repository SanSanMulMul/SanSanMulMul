package com.sansantek.sansanmulmul.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naver.maps.map.overlay.Marker
import com.sansantek.sansanmulmul.config.ApplicationClass
import com.sansantek.sansanmulmul.data.model.HikingHistory
import com.sansantek.sansanmulmul.data.model.MemberLocation
import com.sansantek.sansanmulmul.data.repository.HikingRecordingRepository
import kotlinx.coroutines.launch

class HikingRecordingTabViewModel :
  ViewModel() {
  private var _isAlertShow: Boolean = false
  val isAlertShow: Boolean
    get() = _isAlertShow
  private val repository = HikingRecordingRepository()

  private val _recordingStatus =
    MutableLiveData(ApplicationClass.sharedPreferencesUtil.getHikingRecordingState())
  val recordingStatus: LiveData<Int> get() = _recordingStatus

  private val _onGoingCrewId =
    MutableLiveData(ApplicationClass.sharedPreferencesUtil.getHikingRecordingState())
  val onGoingCrewId: LiveData<Int> get() = _onGoingCrewId

  private val _isQRScanned =
    MutableLiveData(ApplicationClass.sharedPreferencesUtil.getIsQRScanned())
  val isQRScanned: LiveData<Boolean> get() = _isQRScanned

  private val _isQRCompleted =
    MutableLiveData(ApplicationClass.sharedPreferencesUtil.getIsQRCompleted())
  val isQRCompleted: LiveData<Boolean> get() = _isQRCompleted

  private val _amILeader = MutableLiveData<Boolean?>()
  val amILeader: LiveData<Boolean?> get() = _amILeader

  private val _error = MutableLiveData<String>()
  val error: LiveData<String> get() = _error

  private val _memberMarkerList: MutableLiveData<MutableList<Marker>> =
    MutableLiveData(mutableListOf())
  val memberMarkerList: LiveData<MutableList<Marker>>
    get() = _memberMarkerList

  private var _isTracking: MutableLiveData<Boolean> = MutableLiveData(true)
  val isTracking: LiveData<Boolean>
    get() = _isTracking

  private val _hikingStartTime = MutableLiveData<String?>()
  val hikingStartTime: LiveData<String?> get() = _hikingStartTime
  fun setHikingStartTime(time: String) {
    _hikingStartTime.value = time
  }

  private val _hikingEndTime = MutableLiveData<String?>()
  val hikingEndTime: LiveData<String?> get() = _hikingEndTime
  fun setHikingEndTime(time: String) {
    _hikingEndTime.value = time
  }

  private val _hikingHistory = MutableLiveData<HikingHistory?>()
  val hikingHistory: LiveData<HikingHistory?> get() = _hikingHistory
  fun setHikingHistory(history: HikingHistory) {
    _hikingHistory.value = history
  }

  private val _isHikingHistoryAddSuccess = MutableLiveData<Boolean?>()
  val isHikingHistoryAddSuccess: LiveData<Boolean?> get() = _isHikingHistoryAddSuccess

  fun setIsTracking(isTracking: Boolean) {
    _isTracking.value = isTracking
  }

  fun setMemberMarkerList(memberLocationList: MutableList<Marker>) {
    _memberMarkerList.value = memberLocationList
  }

  private val _memberList: MutableLiveData<MutableList<MemberLocation>> =
    MutableLiveData(mutableListOf())
  val memberList: LiveData<MutableList<MemberLocation>>
    get() = _memberList

  fun setMemberList(memberList: MutableList<MemberLocation>) {
    _memberList.value = memberList
  }

  fun setIsAlertShow(isAlertShow: Boolean) {
    _isAlertShow = isAlertShow
  }

  fun setRecordingStatus(recordingStatus: Int) {
    _recordingStatus.value = recordingStatus
    ApplicationClass.sharedPreferencesUtil.saveHikingRecordingState(recordingStatus)
  }

  fun deleteState() {
    ApplicationClass.sharedPreferencesUtil.deleteHikingRecordingState()
  }

  fun setOnGoingCrewId(crewId: Int) {
    _onGoingCrewId.value = crewId
    ApplicationClass.sharedPreferencesUtil.saveHikingRecordingOnGoingCrewId(crewId)
  }

  fun deleteOnGoingCrewId() {
    setOnGoingCrewId(-1)
    ApplicationClass.sharedPreferencesUtil.deleteHikingRecordingOnGoingCrewId()
  }

  fun setIsQRScanned(isQRScanned: Boolean) {
    _isQRScanned.value = isQRScanned
    ApplicationClass.sharedPreferencesUtil.saveIsQRScanned(isQRScanned)
  }

  fun deleteIsQRScanned() {
    setIsQRScanned(false)
    ApplicationClass.sharedPreferencesUtil.deleteIsQRScanned()
  }

  fun setIsQRCompleted(isQRCompleted: Boolean) {
    _isQRCompleted.value = isQRCompleted
    ApplicationClass.sharedPreferencesUtil.saveIsQRCompleted(isQRCompleted)
  }

  fun deleteIsQRCompleted() {
    setIsQRCompleted(false)
    ApplicationClass.sharedPreferencesUtil.deleteIsQRCompleted()
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

  fun addHikingHistory(accessToken: String, history: HikingHistory) {
    viewModelScope.launch {
      try {
        val response = repository.addHikingHistory(accessToken, history)
        if (response != null) {
          _isHikingHistoryAddSuccess.postValue(response)
        } else {
          _error.postValue("데이터를 불러오는 데 실패했습니다!ㅠ.ㅠ")
        }
      } catch (e: Exception) {
        _error.postValue("Error: ${e.message}")
      }
    }
  }

}