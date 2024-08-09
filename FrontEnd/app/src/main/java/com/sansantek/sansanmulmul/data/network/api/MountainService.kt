package com.sansantek.sansanmulmul.data.network.api


import com.sansantek.sansanmulmul.data.model.Mountain
import com.sansantek.sansanmulmul.data.model.MountainSunriseSunset
import com.sansantek.sansanmulmul.data.model.MountainCourse
import com.sansantek.sansanmulmul.data.model.MountainWeather
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
    suspend fun getMountainDetailByID(@Path("mountain_id") id: Int): Response<Mountain>

    @GET("mountain/sun/{mountain_id}")
    suspend fun getMountainSunriseAndSunSetTimeByID(@Path("mountain_id") id: Int): Response<List<MountainSunriseSunset>>

    @GET("mountain/weather/{mountain_id}")
    suspend fun getMountainWeatherByID(@Path("mountain_id") id: Int): Response<List<MountainWeather>>

    @GET("mountain/search")
    suspend fun searchMountainList(@Query("name") name: String): Response<List<Mountain>>

    // 전체 산 리스트
    @GET("mountain")
    suspend fun getMountainList(): List<Mountain>

    // 산 코스 조회
    @GET("mountain/{mountainId}/course")
    suspend fun getMountainCourse(@Path("mountainId") id: Int): Response<MountainCourse>

    // 산 추천 (봄)
    @GET("mountain/recommend/spring")
    suspend fun getMountainSpring(): List<Mountain>

    // 산 추천 (여름)
    @GET("mountain/recommend/summer")
    suspend fun getMountainSummer(): List<Mountain>

    // 산 추천 (가을)
    @GET("mountain/recommend/fall")
    suspend fun getMountainFall(): List<Mountain>

    // 산 추천 (겨울)
    @GET("mountain/recommend/winter")
    suspend fun getMountainWinter(): List<Mountain>
}