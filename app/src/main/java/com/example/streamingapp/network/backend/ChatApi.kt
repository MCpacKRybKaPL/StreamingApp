package com.example.streamingapp.network.backend

import com.example.streamingapp.dataModels.request.SendChatMessageRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path


interface ChatApi {
    @POST("api/chat/{channel}/send")
    suspend fun sendMessage(
        @Path("channel") channel: String,
        @Header("Authorization") auth: String,
        @Body body: SendChatMessageRequest
    ): Response<Unit>
}