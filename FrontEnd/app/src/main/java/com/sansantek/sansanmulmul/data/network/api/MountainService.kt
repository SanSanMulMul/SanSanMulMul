package com.sansantek.sansanmulmul.data.network.api

import com.sansantek.sansanmulmul.data.model.MountainDto
import com.sansantek.sansanmulmul.data.model.MountainWeather
import com.sansantek.sansanmulmul.data.model.SunriseSunsetTimes
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// 산 컨트롤러쪽의 API를 호출하기 위한 MountainService
interface MountainService {
    // 산 상세 조회를 보면 get 방법으로 mountain에 동적 경로로 mountain_id를 받는다
    // 어노테이션으로 GET 방법임을 명시한 후, 주소로 동일하게 쓰고
    // 함수의 인자로, @Path("mountain_id")로 지금 보내주는 함수의 인자가 mountain_id라는 동적 경로로 사용될 것임을 명시
    @GET("mountain/{mountain_id}")
    suspend fun getMountainDetailByID(@Path("mountain_id") id: Int): Response<MountainDto>

    @GET("mountain/sun/{mountain_id}")
    suspend fun getMountainSunriseAndSunSetTimeByID(@Path("mountain_id") id: Int): Response<List<SunriseSunsetTimes>>

    @GET("mountain/weather/{mountain_id}")
    suspend fun getMountainWeatherByID(@Path("mountain_id") id: Int): Response<List<MountainWeather>>

    @GET("mountain/search")
    suspend fun searchMountainList(@Query("name") name: String): List<MountainDto>

}