package com.sansantek.sansanmulmul.data.network.api

import com.sansantek.sansanmulmul.data.model.MessageHistory
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ChatService {
    @GET("/chat/history/{crewId}")
    suspend fun loadChatHistory(@Path("crewId")crewId: Int): Response<List<MessageHistory>>
}