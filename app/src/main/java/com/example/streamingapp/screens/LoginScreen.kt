package com.example.streamingapp.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.streamingapp.ui.theme.PitchBlack
import com.example.streamingapp.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit
) {
    val isLoading = viewModel.isLoading
    val errorMessage = viewModel.errorMessage
    val isLoggedIn = viewModel.isLoggedIn

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            onLoginSuccess()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = PitchBlack
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var loginRegister by remember { mutableStateOf(false) }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Log In")
                Switch(
                    checked = loginRegister,
                    onCheckedChange = { loginRegister = it },
                    modifier = Modifier.padding(5.dp)
                )
                Text("Register")
            }

            Spacer(modifier = Modifier.height(15.dp))

            AnimatedContent(
                targetState = loginRegister,
                label = "LoginRegisterAnimation",
                transitionSpec = {
                    val animationDuration = 300

                    slideInHorizontally(
                        animationSpec = tween(
                            animationDuration,
                            animationDuration / 2,
                            easing = EaseIn
                        ),
                        initialOffsetX = { if (loginRegister) -it else it }
                    ) + fadeIn(
                        animationSpec = tween(animationDuration, animationDuration / 2)
                    ) togetherWith slideOutHorizontally(
                        animationSpec = tween(animationDuration, easing = EaseOut),
                        targetOffsetX = { if (loginRegister) it else -it }
                    ) + fadeOut(
                        animationSpec = tween(animationDuration)
                    )
                }
            ) { isRegister ->

                if (isRegister) {
                    RegisterForm(
                        isLoading = isLoading,
                        onRegister = { username, password, repeatPassword, inviteCode ->
                            if (password != repeatPassword) {
                                return@RegisterForm
                            }

                            viewModel.register(username, password, inviteCode)
                        }
                    )
                } else {
                    LoginForm(
                        isLoading = isLoading,
                        onLogin = { username, password ->
                            viewModel.login(username, password)
                        }
                    )
                }
            }

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(15.dp))
                Text(text = errorMessage)
            }
        }
    }
}


@Composable
fun LoginForm(
    isLoading: Boolean,
    onLogin: (username: String, password: String) -> Unit
) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordHidden by remember { mutableStateOf(true) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedTextField(
            value = login,
            onValueChange = { login = it },
            label = { Text("Login") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password
            ),
            visualTransformation = if (passwordHidden) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            trailingIcon = {
                val icon = if (passwordHidden) {
                    Icons.Outlined.VisibilityOff
                } else {
                    Icons.Outlined.Visibility
                }

                IconButton(
                    onClick = { passwordHidden = !passwordHidden }
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            enabled = !isLoading,
            onClick = {
                onLogin(login, password)
            }
        ) {
            Text(if (isLoading) "Logging in..." else "Log In")
        }
    }
}
@Composable
fun RegisterForm(
    isLoading: Boolean,
    onRegister: (
        username: String,
        password: String,
        repeatPassword: String,
        inviteCode: String
    ) -> Unit
) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatPassword by remember { mutableStateOf("") }
    var inviteCode by remember { mutableStateOf("") }
    var passwordHidden by remember { mutableStateOf(true) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedTextField(
            value = login,
            onValueChange = { login = it },
            label = { Text("Login") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password
            ),
            visualTransformation = if (passwordHidden) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
            trailingIcon = {
                val icon = if (passwordHidden) {
                    Icons.Outlined.VisibilityOff
                } else {
                    Icons.Outlined.Visibility
                }

                IconButton(
                    onClick = { passwordHidden = !passwordHidden }
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = repeatPassword,
            onValueChange = { repeatPassword = it },
            label = { Text("Repeat Password") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password
            ),
            visualTransformation = if (passwordHidden) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = inviteCode,
            onValueChange = { inviteCode = it },
            label = { Text("Invite Code") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            enabled = !isLoading,
            onClick = {
                onRegister(login, password, repeatPassword, inviteCode)
            }
        ) {
            Text(if (isLoading) "Registering..." else "Register")
        }
    }
}
