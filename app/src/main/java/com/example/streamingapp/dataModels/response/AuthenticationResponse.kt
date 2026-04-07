package com.example.streamingapp.dataModels.response

import com.google.gson.annotations.SerializedName

data class AuthenticationResponse(
    val id: Long,
    val username: String,
    val roles: List<String>,

    @SerializedName("access_token")
    val accessToken: String,

    @SerializedName("refresh_token")
    val refreshToken: String,

    @SerializedName("token_type")
    val tokenType: String
)
