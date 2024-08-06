package com.sansantek.sansanmulmul.ui.util

import com.sansantek.sansanmulmul.config.ApplicationClass.Companion.retrofit
import com.sansantek.sansanmulmul.data.network.api.MountainService
import com.sansantek.sansanmulmul.data.network.api.NewsService
import com.sansantek.sansanmulmul.data.network.api.UserService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofiltUtil {
    companion object{
        // API를 호출하기 위한 MountainService 인터페이스 내의 함수들을 기능하게 하는 service 생성
        val mountainService = retrofit.create(MountainService::class.java)
        val userService = retrofit.create(UserService::class.java)


        private const val BASE_URL = "https://i11d111.p.ssafy.io/"
        val newsService: NewsService by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NewsService::class.java)
        }
    }
}