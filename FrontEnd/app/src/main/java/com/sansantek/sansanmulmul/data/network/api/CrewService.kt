package com.sansantek.sansanmulmul.data.network.api

import com.sansantek.sansanmulmul.data.model.CreateCrew
import com.sansantek.sansanmulmul.data.model.Crew
import com.sansantek.sansanmulmul.data.model.CrewInfo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface CrewService {
    @GET("crew/all")
    suspend fun getAllActivatedList(@Header("Authorization") accessToken: String): Response<List<Crew>>
    @GET("crew/ing")
    suspend fun getMyScheduledCrew(@Header("Authorization") accessToken: String): Response<List<Crew>>
    @GET("crew/complete")
    suspend fun getMyCompletedCrew(@Header("Authorization") accessToken: String): Response<List<Crew>>

    @POST("crew/{crewId}/join")
    suspend fun registerCrew(@Header("Authorization") accessToken: String, @Path("crewId") crewId: Int): Response<String>

    @POST("crew")
    suspend fun createCrew(@Header("Authorization") accessToken: String, @Body crewCreateRequest: CreateCrew): Response<String>

    @GET("crew/detail/{crewId}/info")
    suspend fun getCrewInfo(@Path("crewId")crewId: Int):Response<CrewInfo>

}