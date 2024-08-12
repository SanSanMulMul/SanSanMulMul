package com.sansantek.sansanmulmul.data.network.api

import com.sansantek.sansanmulmul.data.model.CreateCrew
import com.sansantek.sansanmulmul.data.model.Crew
import com.sansantek.sansanmulmul.data.model.CrewCommon
import com.sansantek.sansanmulmul.data.model.CrewGallery
import com.sansantek.sansanmulmul.data.model.CrewInfo
import com.sansantek.sansanmulmul.data.model.DelegateUser
import com.sansantek.sansanmulmul.data.model.GroupDetailFirstData
import com.sansantek.sansanmulmul.data.model.ProfileUpdateData
import com.sansantek.sansanmulmul.data.model.RequestMember
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
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

    @GET("crew/detail/{crewId}/info")
    suspend fun getGroupDetailFirstTabData(@Path("crewId") groupId: Int): Response<GroupDetailFirstData>

    @DELETE("crew/{crewId}/{userId}")
    suspend fun kickMember(@Header("Authorization") accessToken: String, @Path("crewId")crewId: Int, @Path("userId")userId: Int) : Response<String>

    @PUT("crew/{crewId}/leader")
    suspend fun delegateLeader(@Header("Authorization") accessToken: String, @Path("crewId")crewId: Int, @Query("nextLeaderId") nextLeaderId: Int): Response<String>

    @GET("crew/{crewId}/requests")
    suspend fun getRequestList(@Header("Authorization") accessToken: String, @Path("crewId")crewId: Int): Response<List<RequestMember>>

    @PATCH("crew/{requestId}/refuse")
    suspend fun refuseRegister(@Header("Authorization") accessToken: String, @Path("requestId")requestId: Int) : Response<String>

    @PATCH("crew/{requestId}/accept")
    suspend fun acceptRegister(@Header("Authorization") accessToken: String, @Path("requestId")requestId: Int) : Response<String>

    @DELETE("crew/{crewId}/out")
    suspend fun exitCrew(@Header("Authorization") accessToken: String, @Path("crewId")crewId: Int) : Response<String>

    @DELETE("crew/{crewId}")
    suspend fun deleteCrew(@Header("Authorization") accessToken: String, @Path("crewId")crewId: Int) : Response<String>

    @GET("crew/detail/{crewId}/common")
    suspend fun getCrewCommon(@Header("Authorization") accessToken: String, @Path("crewId")crewId: Int) : Response<CrewCommon>

    @DELETE("crew/detail/{crewId}/gallery")
    suspend fun deleteGallery(@Header("Authorization") accessToken: String, @Path("crewId")crewId: Int, @Query("picId") picId: Int) : Response<String>
}