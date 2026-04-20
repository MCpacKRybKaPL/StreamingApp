package com.example.streamingapp.repository

import com.example.streamingapp.dataModels.request.AuthenticationRequest
import com.example.streamingapp.dataModels.request.RefreshTokenRequest
import com.example.streamingapp.dataModels.request.RegisterRequest
import com.example.streamingapp.network.backend.AuthApi
import com.example.streamingapp.utils.TokenManager

class AuthRepository(
    private val api: AuthApi,
    private val tokenManager: TokenManager
) {
    suspend fun login(username: String, password: String): Boolean {
        try {
            val response = api.authenticate(AuthenticationRequest(username, password))
            val body = response.body()
            android.util.Log.d("AUTH", "Code: ${response.code()}")
            android.util.Log.d("AUTH", "Body: $body")
            android.util.Log.d("AUTH", "Error: ${response.errorBody()?.string()}")
            if (response.isSuccessful && body != null) {
                tokenManager.saveAccessToken(body.accessToken)
                tokenManager.saveRefreshToken(body.refreshToken)
                return true
            }
            return false
        } catch (e: Exception) {
            android.util.Log.e("AUTH", "Exception: ${e.message}")
            return false
        }
    }

    suspend fun register(username: String, password: String, invCode: String): Boolean{
        try{
            val response = api.register(RegisterRequest(username, password, invCode))
            val body = response.body()
            if(response.isSuccessful && body != null){
                tokenManager.saveAccessToken(body.accessToken)
                tokenManager.saveRefreshToken(body.refreshToken)
                return true
            }
            return false
        }catch (e: Exception){
            return false
        }
    }

    suspend fun logout(): Boolean{
        try{
            val response = api.logout()
            tokenManager.clearTokens()
            android.util.Log.d("LOGOUT", "RESPONSE:" + response.isSuccessful)
            if(response.isSuccessful){
                return true
            }
            return false
        }catch (e: Exception){
            return false
        }
    }

    suspend fun refreshToken(): Boolean{
        try {
            val response = api.refreshToken(RefreshTokenRequest(tokenManager.getRefreshToken()));
            val body = response.body()
            android.util.Log.d("AUTH", "Code: ${response.code()}")
            android.util.Log.d("AUTH", "Body: $body")
            android.util.Log.d("AUTH", "Error: ${response.errorBody()?.string()}")
            if (response.isSuccessful && body != null) {
                tokenManager.saveAccessToken(body.accessToken)
                tokenManager.saveRefreshToken(body.refreshToken)
                return true
            }
            return false
        }catch (e: Exception){
            return false
        }
    }
}