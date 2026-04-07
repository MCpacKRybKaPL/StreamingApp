package com.example.streamingapp.auth.api

import com.example.streamingapp.dataModels.request.AuthenticationRequest
import com.example.streamingapp.dataModels.request.RefreshTokenRequest
import com.example.streamingapp.dataModels.request.RegisterRequest
import com.example.streamingapp.dataModels.response.AuthenticationResponse
import com.example.streamingapp.dataModels.response.RefreshTokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthenticationResponse>

    @POST("api/auth/authenticate")
    suspend fun authenticate(@Body request: AuthenticationRequest): Response<AuthenticationResponse>

    @POST("api/auth/refresh-token")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): Response<RefreshTokenResponse>

    @POST("api/auth/logout")
    suspend fun logout(): Response<Unit>
}