package com.sansantek.sansanmulmul.data.network.api

import com.sansantek.sansanmulmul.data.model.FollowUser
import com.sansantek.sansanmulmul.data.model.KakaoLoginToken
import com.sansantek.sansanmulmul.data.model.KakaoLoginUser
import com.sansantek.sansanmulmul.data.model.MyPageInfo
import com.sansantek.sansanmulmul.data.model.ProfileUpdateData
import com.sansantek.sansanmulmul.data.model.User
import com.sansantek.sansanmulmul.data.model.UserToken
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService {
    @POST("user/signup")
    suspend fun registerUser(@Body kakaoLoginUser: KakaoLoginUser): Response<UserToken>
    @GET("user/login/{userProviderId}")
    suspend fun loginUser(@Path("userProviderId") id: String) : Response<KakaoLoginToken>
    @GET("user/nickname")
    suspend fun isAvailableNickName(@Query("userNickname") nickName: String) : Response<Boolean>
    @GET("user/info")
    suspend fun loadUserProfile(@Header("Authorization") accessToken: String) : Response<User>
    @GET("user/token")
    suspend fun refreshToken(@Header("Authorization") accessToken: String) : Response<KakaoLoginToken>
    @GET("user/style")
    suspend fun getHikingStyle(@Header("Authorization") accessToken: String): List<Int>
    @GET("user/followings")
    suspend fun getUserFollowing(@Header("Authorization") accessToken: String): List<FollowUser>
    @GET("user/followers")
    suspend fun getUserFollower(@Header("Authorization") accessToken: String): List<FollowUser>
    @GET("user/mypage")
    suspend fun getMyPageInfo(@Header("Authorization") accessToken: String): MyPageInfo
    @GET("user/badge")
    suspend fun getMyBadgeList(@Header("Authorization") accessToken: String) : List<String>
    @PATCH("user/info")
    suspend fun updateUserProfile(@Header("Authorization") accessToken: String, @Body profileUpdateData: ProfileUpdateData) : Boolean
}