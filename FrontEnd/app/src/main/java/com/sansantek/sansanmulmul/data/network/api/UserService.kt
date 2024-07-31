package com.sansantek.sansanmulmul.data.network.api

import com.sansantek.sansanmulmul.data.model.KakaoLoginUser
import com.sansantek.sansanmulmul.data.model.UserToken
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("user/signup")
    suspend fun registerUser(@Body kakaoLoginUser: KakaoLoginUser): Response<UserToken>

}