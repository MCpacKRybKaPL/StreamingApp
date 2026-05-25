package com.example.streamingapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.streamingapp.dataModels.response.ChannelsPublicResponse
import com.example.streamingapp.network.RetrofitClient
import com.example.streamingapp.utils.TokenManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

sealed class ChannelUiState {
    object Loading : ChannelUiState()
    data class Success(val channels: List<ChannelsPublicResponse>) : ChannelUiState()
    data class Error(val message: String) : ChannelUiState()
}

class ChannelViewModel(private val tokenManager: TokenManager) : ViewModel() {

    private val _uiState = MutableStateFlow<ChannelUiState>(ChannelUiState.Loading)
    val uiState: StateFlow<ChannelUiState> = _uiState

    init {
        startPolling()
    }

    private fun startPolling() {
        viewModelScope.launch {
            while (isActive) {
                fetchLiveChannels()
                delay(30_000L)
            }
        }
    }

    fun retry() {
        viewModelScope.launch { fetchLiveChannels() }
    }

    private suspend fun fetchLiveChannels() {
        if (_uiState.value !is ChannelUiState.Success) {
            _uiState.value = ChannelUiState.Loading
        }
        try {
            val token = tokenManager.getAccessToken()
                ?: run { _uiState.value = ChannelUiState.Error("Brak tokenu"); return }

            val response = RetrofitClient.channelInstance.getLiveChannels("Bearer $token")

            if (response.isSuccessful) {
                _uiState.value = ChannelUiState.Success(response.body()?.channels ?: emptyList())
            } else {
                _uiState.value = ChannelUiState.Error("HTTP ${response.code()}")
            }
        } catch (e: Exception) {
            _uiState.value = ChannelUiState.Error(e.message ?: "Nieznany błąd")
        }
    }
}

class ChannelViewModelFactory(private val tokenManager: TokenManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return ChannelViewModel(tokenManager) as T
    }
}