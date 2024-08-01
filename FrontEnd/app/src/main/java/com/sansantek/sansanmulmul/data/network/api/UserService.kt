package com.sansantek.sansanmulmul.data.network.api

import com.sansantek.sansanmulmul.data.model.KakaoLoginUser
import com.sansantek.sansanmulmul.data.model.UserToken
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface UserService {
    @POST("user/signup")
    suspend fun registerUser(@Body kakaoLoginUser: KakaoLoginUser): Response<UserToken>
    @GET("user/login")
    suspend fun loginUser(@Query("userProviderId") id: Int)
}