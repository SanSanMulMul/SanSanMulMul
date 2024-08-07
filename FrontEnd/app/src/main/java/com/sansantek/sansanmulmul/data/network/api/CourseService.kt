package com.sansantek.sansanmulmul.data.network.api

import com.sansantek.sansanmulmul.data.model.CourseDetail
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CourseService {
    @GET("/mountain/{mountainId}/course/{courseId}")
    suspend fun getCourseDetailID(
        @Path("mountain_id") mountainId: Int,
        @Path("course_id") courseId: Long
    ): Response<CourseDetail>
}