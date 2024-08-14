package com.sansantek.sansanmulmul.data.network.api

import com.sansantek.sansanmulmul.data.model.MountainPeakStone
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface StoneService {

    @GET("stone/all")
    suspend fun searchAllStones(): Response<List<MountainPeakStone>>

    @GET("stone/{userId}")
    suspend fun getMemberStone(@Path("userId") id: Int): Response<List<MountainPeakStone>>

    @POST("stone/user")
    suspend fun addStone(
        @Header("Authorization") accessToken: String,
        @Query("stoneId") stoneId: Int
    ): Response<Boolean>
}