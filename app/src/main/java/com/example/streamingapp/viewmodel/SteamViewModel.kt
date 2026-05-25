package com.example.streamingapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.streamingapp.BuildConfig
import com.example.streamingapp.dataModels.Message
import com.example.streamingapp.dataModels.request.SendChatMessageRequest
import com.example.streamingapp.network.RetrofitClient
import com.example.streamingapp.utils.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class StreamViewModel(
    private val channelName: String,
    private val tokenManager: TokenManager
) : ViewModel() {

    private val _messages = kotlinx.coroutines.flow.MutableStateFlow<List<Message>>(emptyList())
    val messages: kotlinx.coroutines.flow.StateFlow<List<Message>> = _messages

    private val _chatStatus = kotlinx.coroutines.flow.MutableStateFlow("Łączenie...")
    val chatStatus: kotlinx.coroutines.flow.StateFlow<String> = _chatStatus

    private val httpClient = OkHttpClient()

    private val chatBaseUrl = "http://${BuildConfig.BACKEND_IP}:8088"

    private var chatJob: Job? = null
    private var isConnecting = false

    init {
        connectChatOnce()
    }

    private fun connectChatOnce() {
        if (chatJob?.isActive == true || isConnecting) return

        chatJob = viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                val token = tokenManager.getAccessToken()

                if (token == null) {
                    _chatStatus.value = "Brak tokenu logowania."
                    delay(3000)
                    continue
                }

                try {
                    isConnecting = true
                    _chatStatus.value = "Łączenie..."

                    val request = Request.Builder()
                        .url("$chatBaseUrl/api/chat/$channelName/stream")
                        .addHeader("Authorization", "Bearer $token")
                        .build()

                    httpClient.newCall(request).execute().use { response ->
                        isConnecting = false

                        if (!response.isSuccessful) {
                            _chatStatus.value = "Błąd połączenia z czatem: ${response.code}"
                            delay(3000)
                            return@use
                        }

                        _chatStatus.value = ""

                        val source = response.body?.source()
                        if (source == null) {
                            _chatStatus.value = "Brak odpowiedzi z czatu."
                            delay(3000)
                            return@use
                        }

                        val buffer = StringBuilder()

                        while (isActive && !source.exhausted()) {
                            val line = source.readUtf8Line() ?: break

                            if (line.isEmpty()) {
                                val block = buffer.toString()
                                buffer.clear()

                                if (block.isNotBlank()) {
                                    parseSSEBlock(block)
                                }
                            } else {
                                buffer.appendLine(line)
                            }
                        }
                    }

                    _chatStatus.value = "Rozłączono. Ponawiam..."
                    delay(3000)

                } catch (e: Exception) {
                    isConnecting = false

                    if (isActive) {
                        _chatStatus.value = "Rozłączono. Ponawiam..."
                        delay(3000)
                    }
                }
            }
        }
    }

    private fun parseSSEBlock(block: String) {
        var eventName = ""
        var data = ""

        block.lines().forEach { line ->
            when {
                line.startsWith("event:") -> {
                    eventName = line.removePrefix("event:").trim()
                }

                line.startsWith("data:") -> {
                    data = line.removePrefix("data:").trim()
                }
            }
        }

        when (eventName) {
            "chat" -> {
                if (data.isNotEmpty()) {
                    runCatching {
                        val json = JSONObject(data)

                        val msg = Message(
                            username = json.optString("username", ""),
                            message = json.optString("text", "")
                        )

                        if (msg.message.isNotBlank()) {
                            _messages.value = (_messages.value + msg).takeLast(100)
                        }
                    }
                }
            }

            "connected" -> {
                // Nie dodawaj tego do listy wiadomości,
                // bo wtedy przy każdym połączeniu widzisz:
                // "connected with channel: testChannel"
                _chatStatus.value = ""
            }
        }
    }

    fun sendMessage(text: String) {
        val cleanText = text.trim()
        if (cleanText.isBlank()) return

        viewModelScope.launch {
            val token = tokenManager.getAccessToken()

            if (token == null) {
                _chatStatus.value = "Nie możesz wysłać wiadomości bez tokenu."
                return@launch
            }

            runCatching {
                RetrofitClient.chatInstance.sendMessage(
                    channel = channelName,
                    auth = "Bearer $token",
                    body = SendChatMessageRequest(cleanText)
                )
            }.onFailure {
                _chatStatus.value = "Nie udało się wysłać wiadomości."
            }
        }
    }

    fun getHlsUrl(): String {
        return "$chatBaseUrl/hls/$channelName.m3u8"
    }

    fun getToken(): String? {
        return tokenManager.getAccessToken()
    }

    override fun onCleared() {
        super.onCleared()
        chatJob?.cancel()
    }
}

class StreamViewModelFactory(
    private val channelName: String,
    private val tokenManager: TokenManager
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return StreamViewModel(channelName, tokenManager) as T
    }
}