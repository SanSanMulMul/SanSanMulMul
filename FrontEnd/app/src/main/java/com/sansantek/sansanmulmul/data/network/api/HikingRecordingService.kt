package com.sansantek.sansanmulmul.data.network.api

import com.sansantek.sansanmulmul.data.model.HikingRecordingCoord
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface HikingRecordingService {
  @POST("record/coord")
  suspend fun saveMyCoord(
    @Header("Authorization") accessToken: String,
    @Body coordRequest: HikingRecordingCoord
  ): Response<String>

  @GET("record/chk")
  suspend fun amILeader(
    @Header("Authorization") accessToken: String,
    @Query("crewId") crewId: Int
  ): Response<Boolean>
}