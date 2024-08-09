package com.sansantek.sansanmulmul.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sansantek.sansanmulmul.data.model.Mountain
import com.sansantek.sansanmulmul.data.model.MountainWithCourseCnt
import com.sansantek.sansanmulmul.data.repository.MountainRepository
import kotlinx.coroutines.launch

class MountainSearchViewModel : ViewModel() {
    private val repository = MountainRepository()

    private val _searchKeyword = MutableLiveData<String>()
    val searchKeyword: LiveData<String> get() = _searchKeyword

    private val _mountainList = MutableLiveData<List<Mountain>?>()
    val mountain: LiveData<List<Mountain>?> get() = _mountainList

    private val _mountainListWithCourses = MutableLiveData<List<MountainWithCourseCnt>>()
    val mountainListWithCourses: LiveData<List<MountainWithCourseCnt>> get() = _mountainListWithCourses


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
                    val mountainList = response.map { MountainWithCourseCnt(it) }
                    fetchMountainCourse(mountainList)
                    _mountainList.postValue(response)
                } else {
                    _error.postValue("데이터를 불러오는 데 실패했습니다!ㅠ.ㅠ")
                }
            } catch (e: Exception) {
                _error.postValue("Error: ${e.message}")
            }
        }
    }

    fun fetchMountainCourse(mountains: List<MountainWithCourseCnt>) {
        viewModelScope.launch {
            try {
                mountains.forEach { mountainWithCourseCount ->
                    val response =
                        repository.getMountainCourse(mountainWithCourseCount.mountain.mountainId)
                    response?.let {
                        mountainWithCourseCount.courseCount = it.courseCount
                    }
                }
                _mountainListWithCourses.postValue(mountains)
            } catch (e: Exception) {
                _error.postValue("Error: ${e.message}")
            }
        }
    }

}