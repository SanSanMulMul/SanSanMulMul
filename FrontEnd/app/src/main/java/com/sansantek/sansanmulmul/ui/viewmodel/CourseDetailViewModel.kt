package com.sansantek.sansanmulmul.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sansantek.sansanmulmul.data.model.Course
import com.sansantek.sansanmulmul.data.repository.CourseRepository
import kotlinx.coroutines.launch

class CourseDetailViewModel : ViewModel() {
    private val repository = CourseRepository()

    private val _mountainID = MutableLiveData<Int>()
    val mountainID: LiveData<Int> get() = _mountainID

    private val _courseDetail = MutableLiveData<List<Course>?>()
    val courseDetail: LiveData<List<Course>?> get() = _courseDetail

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun setMountainID(mountainID: Int) {
        _mountainID.value = mountainID
    }

    fun fetchCourseDetail(mountainId: Int) {
        viewModelScope.launch {
            try {
                val response = repository.getCourseDetail(mountainId)
                if (response != null) {
                    _courseDetail.postValue(response)
                } else {
                    _error.postValue("데이터를 불러오는 데 실패했습니다!ㅠ.ㅠ")
                }
            } catch (e: Exception) {
                _error.postValue("Error: ${e.message}")
            }
        }
    }
}