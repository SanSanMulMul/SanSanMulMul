package com.sansantek.sansanmulmul.data.network.api

import com.sansantek.sansanmulmul.data.model.CreateCrew
import com.sansantek.sansanmulmul.data.model.Crew
import com.sansantek.sansanmulmul.data.model.CrewGallery
import com.sansantek.sansanmulmul.data.model.CrewInfo
import com.sansantek.sansanmulmul.data.model.ProfileUpdateData
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

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

    @GET("crew/detail/{crewId}/gallery")
    suspend fun getCrewGalleryList(@Header("Authorization") accessToken: String, @Path("crewId") crewId: Int): Response<List<CrewGallery>>

    @Multipart
    @POST("crew/detail/{crewId}/gallery")
    suspend fun postPictureToGallery(@Header("Authorization") accessToken: String, @Path("crewId")crewId: Int, @Part image: MultipartBody.Part) : Response<Boolean>
}