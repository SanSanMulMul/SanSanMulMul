package com.sansantek.sansanmulmul.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sansantek.sansanmulmul.data.model.CourseDetail
import com.sansantek.sansanmulmul.data.repository.CourseRepository
import kotlinx.coroutines.launch

private const val TAG = "싸피_CourseDetailViewModel"

class CourseDetailViewModel : ViewModel() {
  private val repository = CourseRepository()

  private val _mountainID = MutableLiveData<Int>()
  val mountainID: LiveData<Int> get() = _mountainID

  private val _courseID = MutableLiveData<Long>()
  val courseID: LiveData<Long> get() = _courseID

  private val _courseDetail = MutableLiveData<CourseDetail?>()
  val courseDetail: LiveData<CourseDetail?> get() = _courseDetail

  private val _courseDetails = MutableLiveData<List<CourseDetail>>()
  val courseDetails: LiveData<List<CourseDetail>> get() = _courseDetails

  private val _error = MutableLiveData<String>()
  val error: LiveData<String> get() = _error

  fun setMountainID(mountainID: Int) {
    _mountainID.value = mountainID
  }

  fun setCourseID(courseID: Long) {
    _courseID.value = courseID
  }

  fun fetchCourseDetail(mountainId: Int, courseIds: List<Long>) {
    viewModelScope.launch {
      try {
        val courseDetailsList = mutableListOf<CourseDetail>()

        courseIds.forEach { courseId ->
          val response = repository.getCourseDetail(mountainId, courseId)
          if (response != null) {
            Log.d(TAG, "fetchCourseDetail: $response")
            courseDetailsList.add(response)
          } else {
            _error.postValue("데이터를 불러오는 데 실패했습니다!ㅠ.ㅠ")
          }
        }
        _courseDetails.postValue(courseDetailsList)

      } catch (e: Exception) {
        _error.postValue("Error: ${e.message}")
      }
    }
  }
}