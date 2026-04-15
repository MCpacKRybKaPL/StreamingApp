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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.streamingapp.dataModels.Message
import com.example.streamingapp.ui.theme.Orange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StreamView(streamID: String, navController: NavController){
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(streamID)
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(route = "StreamList")
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "")
                    }
                }
            )
        },
        content = { innerPadding ->
            Column(modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)) {
                AsyncImage(
                    model = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRYOua5-UzBDdi0zfoKP4LyrqxcpHIN4u2rsw&s",
                    contentDescription = "",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    items(30){
                        MessageView(Message("asd", "kerhsdvbckhsgvdckbh"))
                    }
                }
                Spacer(modifier = Modifier.height(5.dp))
                MessageInput(modifier = Modifier
                                        .fillMaxWidth()
                                        .imePadding()
                                        .navigationBarsPadding())
            }
        }
    )
}

@Composable
fun MessageView(message: Message){
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = message.username,
            color = Orange
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(message.message)
    }
}

@Composable
fun MessageInput(modifier: Modifier = Modifier){
    var message by remember { mutableStateOf("") }
    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
    ) {
        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(5.dp))
        IconButton(
            onClick = {
                //send message
            },
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(5.dp))
                .background(Orange)
        ) {
            Icon(
                Icons.AutoMirrored.Outlined.Send,
                contentDescription = ""
            )
        }
    }
}