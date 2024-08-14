package com.sansantek.sansanmulmul.data.network.api

import com.sansantek.sansanmulmul.data.model.MountainHistory
import com.sansantek.sansanmulmul.data.model.MountainHistoryDetail
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Query

interface RecordService {
    @GET("record/all")
    suspend fun getMyHikingRecord(@Header("Authorization") token: String)

    @GET("record/{userId}/all")
    suspend fun getUserHikingRecord(@Path("userId") id: Int) : Response<List<MountainHistory>>

    @GET("record/detail")
    suspend fun getMountainDetailRecord(@Header("Authorization") token: String, @Query("recordId") recordId: Int) : Response<MountainHistoryDetail>

    @GET("record/{crewId}/alarm")
    suspend fun notifyIsolation(@Path("crewId")crewId: Int): Response<Boolean>
}