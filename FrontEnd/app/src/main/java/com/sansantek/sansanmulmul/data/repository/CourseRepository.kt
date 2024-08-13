package com.sansantek.sansanmulmul.data.repository

import android.util.Log
import com.sansantek.sansanmulmul.data.model.CourseDetail
import com.sansantek.sansanmulmul.ui.util.RetrofiltUtil

private const val TAG = "싸피_CourseRepository"

class CourseRepository {
    suspend fun getCourseDetail(mountainId: Int, courseId: Long): CourseDetail? {
        return try {
            val response = RetrofiltUtil.courseService.getCourseDetailByID(mountainId, courseId)
            if (response.isSuccessful) {
                response.body()
            } else {
                Log.d(TAG, "getCourseDetail: coursedetail is null")
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}