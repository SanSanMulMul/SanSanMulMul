package com.sansantek.sansanmulmul.data.network.api

import com.sansantek.sansanmulmul.data.model.MountainHistory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Response

interface RecordService {
    @GET("record/all")
    suspend fun getAllHikingRecord()

    @GET("record/{userId}/all")
    suspend fun getAllMemberHikingRecord(@Path("userId") id: Int) : Response<List<MountainHistory>>
}