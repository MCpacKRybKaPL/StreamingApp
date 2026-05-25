//package com.example.streamingapp.screens
//
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.lazy.grid.GridCells
//import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.ArrowBack
//import androidx.compose.material3.Card
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavController
//import coil.compose.AsyncImage
//import com.example.streamingapp.ui.theme.Orange
//import kotlin.random.Random
//
//@Composable
//fun StreamList(navController: NavController){
//    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//        Column(
//            modifier = Modifier.padding(innerPadding)
//        ) {
//            Row{
//                IconButton(onClick = {
//                    navController.navigate(route = "LoginRegister")
//                }) {
//                    Icon(
//                        Icons.AutoMirrored.Filled.ArrowBack,
//                        contentDescription = "")
//                }
//            }
//            LazyVerticalGrid(
//                columns = GridCells.Fixed(2)
//            ) {
//                items(30) {
//                    StreamPreview(
//                        preview = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRYOua5-UzBDdi0zfoKP4LyrqxcpHIN4u2rsw&s",
//                        proile = "https://freerangestock.com/sample/120147/business-man-profile-vector.jpg",
//                        title = "Zagrajmy w Minecraft",
//                        streamerName = "JJjoker",
//                        streamID = Random.nextInt(1, 100),
//                        navController = navController
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Composable
//
//fun StreamPreview(preview: String, proile: String, title: String, streamerName: String, streamID: Int, navController: NavController){
//    Card(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(10.dp)
//            .clickable( onClick = {
//                navController.navigate("StreamView/$streamID")
//            } ),
//        shape = RoundedCornerShape(10.dp)
//    ) {
//        Column {
//            AsyncImage(
//                model = preview,
//                contentDescription = "",
//                contentScale = ContentScale.FillWidth,
//                modifier = Modifier.fillMaxWidth()
//            )
//            Column(
//                modifier = Modifier.padding(10.dp)
//            ) {
//                Row(
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    AsyncImage(
//                        model = proile,
//                        contentDescription = "",
//                        modifier = Modifier.clip(CircleShape).size(30.dp)
//                    )
//                    Spacer(modifier = Modifier.width(10.dp))
//                    Text(
//                        text = title,
//                        fontSize = 14.sp,
//                        fontWeight = FontWeight.SemiBold
//                    )
//                }
//                Row {
//                    Spacer(modifier = Modifier.width(40.dp))
//                    Text(
//                        text = streamerName,
//                        fontSize = 12.sp,
//                        color = Orange
//                    )
//                }
//            }
//        }
//    }
//}
package com.example.streamingapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.streamingapp.dataModels.response.ChannelsPublicResponse
import com.example.streamingapp.ui.theme.Orange
import com.example.streamingapp.utils.TokenManager
import com.example.streamingapp.viewmodel.ChannelUiState
import com.example.streamingapp.viewmodel.ChannelViewModel
import com.example.streamingapp.viewmodel.ChannelViewModelFactory

@Composable
fun StreamList(navController: NavController) {
    val context = LocalContext.current
    val tokenManager = TokenManager(context)
    val viewModel: ChannelViewModel = viewModel(
        factory = ChannelViewModelFactory(tokenManager)
    )
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Row {
                IconButton(onClick = {
                    navController.navigate(route = "LoginRegister")
                }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "")
                }
            }

            when (val state = uiState) {
                is ChannelUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                }
                is ChannelUiState.Error -> {
                    Text(
                        text = "Błąd: ${state.message}",
                        modifier = Modifier.padding(16.dp)
                    )
                    Button(
                        onClick = { viewModel.retry() },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Text("Spróbuj ponownie")
                    }
                }
                is ChannelUiState.Success -> {
                    if (state.channels.isEmpty()) {
                        Text(
                            text = "Nikt teraz nie streamuje.",
                            modifier = Modifier.padding(16.dp)
                        )
                    } else {
                        LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                            items(state.channels) { channel ->
                                StreamPreview(
                                    channel = channel,
                                    navController = navController
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StreamPreview(channel: ChannelsPublicResponse, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .clickable {
                navController.navigate("StreamView/${channel.name}")
            },
        shape = RoundedCornerShape(10.dp)
    ) {
        Column {
            AsyncImage(
                model = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRYOua5-UzBDdi0zfoKP4LyrqxcpHIN4u2rsw&s",
                contentDescription = "",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth()
            )
            Column(modifier = Modifier.padding(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = "https://freerangestock.com/sample/120147/business-man-profile-vector.jpg",
                        contentDescription = "",
                        modifier = Modifier
                            .clip(CircleShape)
                            .size(30.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = channel.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Row {
                    Spacer(modifier = Modifier.width(40.dp))
                    Text(
                        text = "@${channel.username}",
                        fontSize = 12.sp,
                        color = Orange
                    )
                }
            }
        }
    }
}