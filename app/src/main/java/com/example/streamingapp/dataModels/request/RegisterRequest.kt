package com.example.streamingapp.dataModels.request

data class RegisterRequest(
    val username: String,
    val password: String,
    val invCode: String
)
