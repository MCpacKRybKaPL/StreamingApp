package com.example.streamingapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.streamingapp.network.RetrofitClient
import com.example.streamingapp.repository.AuthRepository
import com.example.streamingapp.screens.LoginScreen
import com.example.streamingapp.screens.StreamList
import com.example.streamingapp.screens.StreamView
import com.example.streamingapp.ui.theme.StreamingAppTheme
import com.example.streamingapp.utils.TokenManager
import com.example.streamingapp.viewmodel.AuthViewModel
import com.example.streamingapp.viewmodel.AuthViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val tokenManager = TokenManager(applicationContext)

        val authRepository = AuthRepository(
            api = RetrofitClient.instance,
            tokenManager = tokenManager
        )

        val authViewModelFactory = AuthViewModelFactory(authRepository)

        setContent {
            StreamingAppTheme {
                val navController = rememberNavController()

                val authViewModel: AuthViewModel = viewModel(
                    factory = authViewModelFactory
                )

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
                    composable("LoginRegister") {
                        LoginScreen(
                            viewModel = authViewModel,
                            onLoginSuccess = {
                                navController.navigate("StreamList") {
                                    popUpTo("LoginRegister") {
                                        inclusive = true
                                    }
                                }
                            }
                        )
                    }

                    composable("StreamList") {
                        StreamList(
                            navController = navController,
                            authViewModel = authViewModel
                        )
                    }

                    composable(
                        route = "StreamView/{streamID}",
                        arguments = listOf(
                            navArgument("streamID") {
                                type = NavType.StringType
                            }
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