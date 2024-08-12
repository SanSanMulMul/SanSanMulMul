package com.sansantek.sansanmulmul.data.network.api

import android.content.Intent
import android.os.IBinder
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface StoneService {

    @GET("stone/{userId}")
    suspend fun getMemberStone(@Path("userId") id: Int)

}