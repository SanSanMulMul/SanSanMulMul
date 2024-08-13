package com.sansantek.sansanmulmul.data.network.api

import android.content.Intent
import android.os.IBinder
import com.google.gson.annotations.SerializedName
import com.sansantek.sansanmulmul.data.model.KakaoLoginToken
import com.sansantek.sansanmulmul.data.model.KakaoLoginUser
import com.sansantek.sansanmulmul.data.model.News
import com.sansantek.sansanmulmul.data.model.User
import com.sansantek.sansanmulmul.data.model.UserToken
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface NewsService{
    @GET("mountain/news/{keyword}")
    suspend fun getNewsKeyword(
        @Path("keyword") keyword: String
    ): MutableList<News>
    @GET("mountain/news/random")
    suspend fun getRandomNewsKeyword(): MutableList<News>
}