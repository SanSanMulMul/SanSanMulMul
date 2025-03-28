package com.sansantek.sansanmulmul.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sansantek.sansanmulmul.data.model.Mountain
import com.sansantek.sansanmulmul.data.model.MountainCourse
import com.sansantek.sansanmulmul.data.model.MountainSunriseSunset
import com.sansantek.sansanmulmul.data.model.MountainWeather
import com.sansantek.sansanmulmul.data.repository.MountainRepository
import kotlinx.coroutines.launch

class MountainDetailViewModel : ViewModel() {
    private val repository = MountainRepository()

    private val _prevTab = MutableLiveData<Int>()
    val prevTab: LiveData<Int> get() = _prevTab

    private val _mountainID = MutableLiveData<Int>()
    val mountainID: LiveData<Int> get() = _mountainID

    private val _mountainDetail = MutableLiveData<Mountain?>()
    val mountainDetail: LiveData<Mountain?> get() = _mountainDetail

    private val _mountainSunriseSunset = MutableLiveData<List<MountainSunriseSunset>?>()
    val mountainSunriseSunset: LiveData<List<MountainSunriseSunset>?> get() = _mountainSunriseSunset

    private val _mountainWeather = MutableLiveData<List<MountainWeather>?>()
    val mountainWeather: LiveData<List<MountainWeather>?> get() = _mountainWeather

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    private val _mountainCourse = MutableLiveData<MountainCourse?>()
    val mountainCourse: LiveData<MountainCourse?> get() = _mountainCourse

    fun setPrevTab(prevTab: Int) {
        _prevTab.value = prevTab
    }

    fun setMountainID(mountainID: Int) {
        _mountainID.value = mountainID
    }

    // 산 상세 정보 조회 (이름, 이미지, 설명, 고도, 위/경도, 추천 계절, 스팟 리스트)
    fun fetchMountainDetail(mountainId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getMountainDetail(mountainId)
                if (response != null) {
                    _mountainDetail.postValue(response)
                } else {
                    _error.postValue("데이터를 불러오는 데 실패했습니다!ㅠ.ㅠ")
                }
            } catch (e: Exception) {
                _error.postValue("Error: ${e.message}")
            }
        }
    }

    // 산 일출, 일몰 시간 조회
    fun fetchMountainSunriseSunset(mountainId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getMountainSunriseSunset(mountainId)
                if (response != null) {
                    _mountainSunriseSunset.postValue(response)
                } else {
                    _error.postValue("데이터를 불러오는 데 실패했습니다!ㅠ.ㅠ")
                }
            } catch (e: Exception) {
                _error.postValue("Error: ${e.message}")
            }
        }
    }

    // 산 일출, 일몰 시간 조회
    fun fetchMountainWeather(mountainId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getMountainWeather(mountainId)
                if (response != null) {
                    _mountainWeather.postValue(response)
                } else {
                    _error.postValue("데이터를 불러오는 데 실패했습니다!ㅠ.ㅠ")
                }
            } catch (e: Exception) {
                _error.postValue("Error: ${e.message}")
            }
        }
    }

    fun fetchMountainCourse(mountainId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getMountainCourse(mountainId)
                if (response != null) {
                    _mountainCourse.postValue(response)
                } else {
//          _mountainCourse.postValue(MountainCourse(emptyList(), 0, emptyList()))
                    _error.postValue("코스 데이터가 없습니다.")
                }
            } catch (e: Exception) {
                _error.postValue("Error: ${e.message}")
            }
        }
    }
}