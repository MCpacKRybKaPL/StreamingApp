package com.example.streamingapp.utils


import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

@Suppress("DEPRECATION")
class TokenManager(context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "auth_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveAccessToken(token: String){
        sharedPreferences.edit().putString("ACCESS_TOKEN", token).apply()
    }

    fun getAccessToken(): String? = sharedPreferences.getString("ACCESS_TOKEN", null)

    fun saveRefreshToken(refreshToken: String){
        sharedPreferences.edit().putString("REFRESH_TOKEN", refreshToken).apply()
    }

    fun getRefreshToken(): String? = sharedPreferences.getString("REFRESH_TOKEN", null)

    fun clearTokens(){
        sharedPreferences.edit().clear().apply()
    }
}