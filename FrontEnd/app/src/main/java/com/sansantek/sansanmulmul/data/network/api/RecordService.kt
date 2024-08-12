package com.sansantek.sansanmulmul.data.network.api

import retrofit2.http.GET

interface RecordService {
    @GET("record/all")
    suspend fun getAllHikingRecord()
}