package com.sansantek.sansanmulmul.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sansantek.sansanmulmul.data.model.Mountain
import com.sansantek.sansanmulmul.data.model.MountainCourse
import com.sansantek.sansanmulmul.data.repository.MountainRepository
import kotlinx.coroutines.launch

class MountainSearchViewModel : ViewModel() {
    private val repository = MountainRepository()

    private val _searchKeyword = MutableLiveData<String>()
    val searchKeyword: LiveData<String> get() = _searchKeyword

    private val _mountainList = MutableLiveData<List<Mountain>?>()
    val mountain: LiveData<List<Mountain>?> get() = _mountainList

    private val _mountainCourse = MutableLiveData<MountainCourse?>()
    val mountainCourse: LiveData<MountainCourse?> get() = _mountainCourse

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun setSearchKeyword(keyword: String) {
        _searchKeyword.value = keyword
    }

    fun searchMountainList(searchKeyword: String) {
        viewModelScope.launch {
            try {
                val response = repository.searchMountainList(searchKeyword)
                if (response != null) {
                    _mountainList.postValue(response)
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
                    _error.postValue("데이터를 불러오는 데 실패했습니다!ㅠ.ㅠ")
                }
            } catch (e: Exception) {
                _error.postValue("Error: ${e.message}")
            }
        }
    }

}