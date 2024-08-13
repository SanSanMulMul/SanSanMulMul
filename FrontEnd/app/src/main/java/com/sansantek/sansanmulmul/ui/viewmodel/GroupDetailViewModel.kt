package com.sansantek.sansanmulmul.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sansantek.sansanmulmul.data.model.CrewGallery
import com.sansantek.sansanmulmul.data.model.CrewMountainDetail
import com.sansantek.sansanmulmul.data.repository.CrewRepository
import kotlinx.coroutines.launch

class GroupDetailViewModel : ViewModel() {
  private val _error = MutableLiveData<String>()
  val error: LiveData<String> get() = _error
  
  private var _pictureList: MutableLiveData<List<CrewGallery>> = MutableLiveData()
  val pictureList: MutableLiveData<List<CrewGallery>>
    get() = _pictureList

  fun setPictureList(pictureList: List<CrewGallery>) {
    _pictureList.value = pictureList
  }

  private var _position: MutableLiveData<Int> = MutableLiveData(-1)
  val position: LiveData<Int>
    get() = _position

  fun setPosition(position: Int) {
    _position.value = position
  }

  private val repository = CrewRepository()
  private val _crewMountainDetail = MutableLiveData<CrewMountainDetail?>()
  val crewMountainDetail: LiveData<CrewMountainDetail?> get() = _crewMountainDetail
  fun fetchCrewMountainDetail(crewId: Int) {
    viewModelScope.launch {
      try {
        val response = repository.getCrewMountainDetail(crewId)
        if (response != null) {
          _crewMountainDetail.postValue(response)
        } else {
          _error.postValue("데이터를 불러오는 데 실패했습니다!ㅠ.ㅠ")
        }
      } catch (e: Exception) {
        _error.postValue("Error: ${e.message}")
      }
    }
  }
}