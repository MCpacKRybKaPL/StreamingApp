package com.example.streamingapp.api

import com.example.streamingapp.dataModels.request.RefreshTokenRequest
import com.example.streamingapp.dataModels.response.LiveChannelsResponse
import com.example.streamingapp.dataModels.response.RefreshTokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface StreamingApi {

    @GET("/api/channel/get/allLive")
    suspend fun getAllLive(
        @Header("Authorization") token: String
    ): Response<LiveChannelsResponse>

    @POST("/api/auth/refresh-token")
    suspend fun refreshToken(
        @Body body: RefreshTokenRequest
    ): Response<RefreshTokenResponse>

    @POST("/api/auth/logout")
    suspend fun logout(
        @Header("Authorization") token: String
    ): Response<Unit>
}