package com.sansantek.sansanmulmul.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.naver.maps.map.overlay.Marker
import com.sansantek.sansanmulmul.data.model.Mountain
import com.sansantek.sansanmulmul.data.model.MountainCourse
import com.sansantek.sansanmulmul.data.model.MountainSunriseSunset
import com.sansantek.sansanmulmul.data.model.MountainWeather
import com.sansantek.sansanmulmul.data.repository.MountainRepository
import kotlinx.coroutines.launch

class MapTapViewModel : ViewModel() {
    private var _isTracking = MutableLiveData<Boolean>(true)
    val isTracking: LiveData<Boolean> get() = _isTracking

    fun setTraking(isTracking: Boolean) {
        _isTracking.value = isTracking
    }

    private var _markerList = mutableListOf <Marker>()
    val markerList : List<Marker>
        get() = _markerList
    fun setMarkerList(markerList: List<Marker>){
        _markerList = markerList.toMutableList()
    }
    fun clearMarkerList(){
        _markerList.clear()
    }
    fun addMarkerList(marker: Marker){
        _markerList.add(marker)
    }

}