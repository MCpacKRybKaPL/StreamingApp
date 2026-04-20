package com.example.streamingapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.streamingapp.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var isLoggedIn by mutableStateOf(false)
        private set


    fun login(username: String, password: String){
        viewModelScope.launch {
            isLoading = true;
            errorMessage = null;
            val success = repository.login(username, password)

            if(success){
                isLoggedIn = true
            }else{
                errorMessage = "Check your credentials"
            }
            isLoading = false
        }
    }

    fun register(username: String, password: String, invCode: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            val success = repository.register(username, password, invCode)
            if (success) {
                isLoggedIn = true
            } else {
                errorMessage = "Failed to register new user."
            }
            isLoading = false
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()

            isLoggedIn = false
        }
    }

    fun refreshTokenLogin(){
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            val success = repository.refreshToken()
            if (success) {
                isLoggedIn = true
            } else {
                errorMessage = "Failed to login with saved token."
            }
            isLoading = false
        }
    }
}