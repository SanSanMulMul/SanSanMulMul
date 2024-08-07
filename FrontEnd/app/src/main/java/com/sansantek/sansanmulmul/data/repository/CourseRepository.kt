package com.sansantek.sansanmulmul.data.repository

import com.sansantek.sansanmulmul.data.model.CourseDetail
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil

class CourseRepository {
  suspend fun getCourseDetail(mountainId: Int, courseId: Long): CourseDetail? {
    return try {
      val response = RetrofiltUtil.courseService.getCourseDetailByID(mountainId, courseId)
      if (response.isSuccessful) {
        response.body()
      } else {
        null
      }
    } catch (e: Exception) {
      null
    }
  }
}