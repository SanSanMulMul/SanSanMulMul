package com.sansantek.sansanmulmul.data.network.api

import com.google.gson.annotations.SerializedName
import com.sansantek.sansanmulmul.data.model.AvailableNickNameResponse
import com.sansantek.sansanmulmul.data.model.KakaoLoginToken
import com.sansantek.sansanmulmul.data.model.KakaoLoginUser
import com.sansantek.sansanmulmul.data.model.User
import com.sansantek.sansanmulmul.data.model.UserToken
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService {
    @POST("user/signup")
    suspend fun registerUser(@Body kakaoLoginUser: KakaoLoginUser): Response<UserToken>
    @GET("user/login/{userProviderId}")
    suspend fun loginUser(@Path("userProviderId") id: String) : Response<KakaoLoginToken>
    @GET("user/nickname")
    suspend fun isAvailableNickName(@Query("userNickname") nickName: String) : Response<AvailableNickNameResponse>
    @GET("user/info")
    suspend fun loadUserProfile(@Header("Authorization") accessToken: String) : Response<User>
    @GET("user/token")
    suspend fun refreshToken(@Header("Authorization") accessToken: String) : Response<KakaoLoginToken>
}