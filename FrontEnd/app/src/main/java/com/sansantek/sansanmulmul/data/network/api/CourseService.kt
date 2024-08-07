package com.sansantek.sansanmulmul.data.network.api

import com.sansantek.sansanmulmul.data.model.Course
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CourseService {
    @GET("/mountain/{mountainId}/course")
    suspend fun getCourseInfoByID(@Path("mountain_id") id: Int): Response<List<Course>>
}