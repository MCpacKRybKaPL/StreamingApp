package com.example.testoweui.network.api.backend

import com.example.testoweui.model.response.LiveChannelsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header

interface ChannelApi {
    @GET("api/channel/get/allLive")
    suspend fun getLiveChannels(@Header("Authorization") auth: String): Response<LiveChannelsResponse>
}