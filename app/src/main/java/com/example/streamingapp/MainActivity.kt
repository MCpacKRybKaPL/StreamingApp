package com.example.streamingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.streamingapp.screens.LoginScreen
import com.example.streamingapp.screens.StreamList
import com.example.streamingapp.screens.StreamView
import com.example.streamingapp.ui.theme.StreamingAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StreamingAppTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "LoginRegister",
                    enterTransition = {
                        slideIntoContainer(
                            animationSpec = tween(500),
                            towards = AnimatedContentTransitionScope.SlideDirection.Start
                        )
                    },
                    exitTransition = {
                        slideOutOfContainer(
                            animationSpec = tween(500),
                            towards = AnimatedContentTransitionScope.SlideDirection.Start
                        )
                    }
                ) {
                    composable("LoginRegister",
                    ) {
                        LoginScreen(
                            onLogin = {
                                navController.navigate(route = "StreamList")
                            },
                            onRegister = {
                                navController.navigate(route = "StreamList")
                            }
                        )
                    }
                    composable("StreamList") { StreamList(navController) }
                    composable(
                        "StreamView/{streamID}",
                        arguments = listOf(
                            navArgument("streamID") { type = NavType.StringType }
                        )
                    ) { backStackEntry ->
                        val streamID = backStackEntry.arguments?.getString("streamID") ?: "0"
                        StreamView(streamID, navController)
                    }
                }
            }
        }
    }
}