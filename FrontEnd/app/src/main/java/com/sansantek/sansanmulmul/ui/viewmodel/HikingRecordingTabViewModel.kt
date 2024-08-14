package com.sansantek.sansanmulmul.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naver.maps.map.overlay.Marker
import com.sansantek.sansanmulmul.config.ApplicationClass
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

    private val _memberMarkerList : MutableLiveData<MutableList<Marker>> = MutableLiveData(mutableListOf())
    val memberMarkerList: LiveData<MutableList<Marker>>
        get() = _memberMarkerList

    fun setMemberMarkerList(memberLocationList: MutableList<Marker>){
        _memberMarkerList.value = memberLocationList
    }

    private val _memberList : MutableLiveData<MutableList<MemberLocation>> = MutableLiveData(mutableListOf())
    val memberList: LiveData<MutableList<MemberLocation>>
        get() = _memberList

    fun setMemberList(memberList: MutableList<MemberLocation>){
        _memberList.value = memberList
    }

    fun setIsAlertShow(isAlertShow: Boolean){
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
        ApplicationClass.sharedPreferencesUtil.deleteHikingRecordingOnGoingCrewId()
    }

    fun setIsQRScanned(isQRScanned: Boolean) {
        _isQRScanned.value = isQRScanned
        ApplicationClass.sharedPreferencesUtil.saveIsQRScanned(isQRScanned)
    }

    fun deleteIsQRScanned() {
        ApplicationClass.sharedPreferencesUtil.deleteIsQRScanned()
    }

    fun setIsQRCompleted(isQRCompleted: Boolean) {
        _isQRCompleted.value = isQRCompleted
        ApplicationClass.sharedPreferencesUtil.saveIsQRCompleted(isQRCompleted)
    }

    fun deleteIsQRCompleted() {
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

}