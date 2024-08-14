package com.sansantek.sansanmulmul.data.network.api

import com.sansantek.sansanmulmul.data.model.MountainHistory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Response
import retrofit2.http.Header

interface RecordService {
    @GET("record/all")
    suspend fun getMyHikingRecord(@Header("Authorization") token: String)

    @GET("record/{userId}/all")
    suspend fun getUserHikingRecord(@Path("userId") id: Int) : Response<List<MountainHistory>>
}