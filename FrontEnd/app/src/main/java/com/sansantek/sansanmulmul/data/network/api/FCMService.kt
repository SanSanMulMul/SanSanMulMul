package com.sansantek.sansanmulmul.data.network.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface FCMService {
    @POST("user/fcm-token")
    suspend fun initFcmToken(@Header("Authorization") accessToken: String, @Body fcmtoken: String): Response<Boolean>
}