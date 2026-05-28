//package com.example.streamingapp.network
//
//import com.example.streamingapp.BuildConfig
//import com.example.streamingapp.network.backend.AuthApi
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import kotlin.getValue
//import kotlin.jvm.java
//
//object RetrofitClient {
//    private const val BASE_URL = "http://${BuildConfig.BACKEND_IP}:8080/"
//
//    val instance: AuthApi by lazy {
//        val retrofit = Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        retrofit.create(AuthApi::class.java)
//    }
//}
package com.example.streamingapp.network

import com.example.streamingapp.BuildConfig
import com.example.streamingapp.network.backend.AuthApi
import com.example.streamingapp.network.backend.ChannelApi
import com.example.streamingapp.network.backend.ChatApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "http://${BuildConfig.BACKEND_IP}:8080/"
    private const val CHAT_BASE_URL = "http://${BuildConfig.BACKEND_IP}:8088/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val instance: AuthApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(AuthApi::class.java)
    }

    val channelInstance: ChannelApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ChannelApi::class.java)
    }

    val chatInstance: ChatApi by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(CHAT_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ChatApi::class.java)
    }
}