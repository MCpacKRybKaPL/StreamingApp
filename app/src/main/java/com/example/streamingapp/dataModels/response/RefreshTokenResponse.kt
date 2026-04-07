package com.example.streamingapp.dataModels.response

data class RefreshTokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val tokenType: String
)
