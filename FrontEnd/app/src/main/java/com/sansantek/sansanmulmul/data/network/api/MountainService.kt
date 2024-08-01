package com.sansantek.sansanmulmul.data.network.api

import com.sansantek.sansanmulmul.data.model.MountainDto
import com.sansantek.sansanmulmul.data.model.MountainExtra
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// 산 컨트롤러쪽의 API를 호출하기 위한 MountainService
interface MountainService {
    // 산 상세 조회를 보면 get 방법으로 mountain에 동적 경로로 mountain_id를 받는다
    // 어노테이션으로 GET 방법임을 명시한 후, 주소로 동일하게 쓰고
    // 함수의 인자로, @Path("mountain_id")로 지금 보내주는 함수의 인자가 mountain_id라는 동적 경로로 사용될 것임을 명시
    @GET("mountain/{mountain_id}")
    suspend fun getAllMountainList(@Path("mountain_id") id: Int): MountainExtra

    @GET("mountain/search")
    suspend fun searchMountainList(@Query("name") name: String): List<MountainDto>
}