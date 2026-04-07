package com.example.streamingapp.network

import com.example.streamingapp.BuildConfig
import com.example.streamingapp.auth.api.AuthApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.getValue
import kotlin.jvm.java

object RetrofitClient {
    private const val BASE_URL = "http://${BuildConfig.BACKEND_IP}:8080/"

    val instance: AuthApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(AuthApi::class.java)
    }
}