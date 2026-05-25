package com.example.streamingapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import com.example.streamingapp.dataModels.Message
import com.example.streamingapp.ui.theme.Orange
import com.example.streamingapp.utils.TokenManager
import com.example.streamingapp.viewmodel.StreamViewModel
import com.example.streamingapp.viewmodel.StreamViewModelFactory
import kotlin.OptIn

@androidx.annotation.OptIn(UnstableApi::class)
@OptIn(UnstableApi::class, ExperimentalMaterial3Api::class)
@Composable
fun StreamView(
    streamID: String,
    navController: NavController
) {
    val context = LocalContext.current
    val tokenManager = TokenManager(context)

    val viewModel: StreamViewModel = viewModel(
        factory = StreamViewModelFactory(streamID, tokenManager)
    )

    val messages by viewModel.messages.collectAsState()
    val chatStatus by viewModel.chatStatus.collectAsState()
    val listState = rememberLazyListState()

    var message by remember { mutableStateOf("") }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    val exoPlayer = remember {
        val token = viewModel.getToken()

        val dataSourceFactory = DefaultHttpDataSource.Factory().apply {
            if (token != null) {
                setDefaultRequestProperties(
                    mapOf("Authorization" to "Bearer $token")
                )
            }
        }

        val hlsSource = HlsMediaSource.Factory(dataSourceFactory)
            .createMediaSource(
                MediaItem.fromUri(viewModel.getHlsUrl())
            )

        ExoPlayer.Builder(context).build().apply {
            setMediaSource(hlsSource)
            prepare()
            playWhenReady = true
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = streamID)
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate("StreamList")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Powrót"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            AndroidView(
                factory = { context ->
                    PlayerView(context).apply {
                        player = exoPlayer
                        useController = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (chatStatus.isNotEmpty()) {
                Text(
                    text = chatStatus,
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(messages) { msg ->
                    MessageView(message = msg)
                }
            }

            Spacer(modifier = Modifier.height(5.dp))

            MessageInput(
                message = message,
                onMessageChange = { message = it },
                onSend = {
                    if (message.isNotBlank()) {
                        viewModel.sendMessage(message)
                        message = ""
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding()
                    .navigationBarsPadding()
            )
        }
    }
}

@Composable
fun MessageView(message: Message) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        if (message.username.isNotEmpty()) {
            Text(
                text = message.username,
                color = Orange
            )

            Spacer(modifier = Modifier.width(5.dp))

            Text(text = message.message)
        } else {
            Text(
                text = message.message,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun MessageInput(
    message: String,
    onMessageChange: (String) -> Unit,
    onSend: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.height(IntrinsicSize.Min)
    ) {
        OutlinedTextField(
            value = message,
            onValueChange = onMessageChange,
            modifier = Modifier.weight(1f),
            singleLine = true
        )

        Spacer(modifier = Modifier.width(5.dp))

        IconButton(
            onClick = onSend,
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(5.dp))
                .background(Orange)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.Send,
                contentDescription = "Wyślij"
            )
        }
    }
}