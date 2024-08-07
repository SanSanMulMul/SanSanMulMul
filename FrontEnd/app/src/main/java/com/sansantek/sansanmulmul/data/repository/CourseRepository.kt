package com.sansantek.sansanmulmul.data.repository

import com.sansantek.sansanmulmul.data.model.Course
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil

class CourseRepository {
    suspend fun getCourseDetail(mountainId: Int): List<Course>? {
        return try {
            val response = RetrofiltUtil.courseService.getCourseInfoByID(mountainId)
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