package com.example.streamingapp.network.backend

import com.example.streamingapp.dataModels.response.LiveChannelsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface ChannelApi {
    @GET("api/channel/get/allLive")
    suspend fun getLiveChannels(@Header("Authorization") auth: String): Response<LiveChannelsResponse>
}