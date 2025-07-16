package com.example.haven.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.haven.viewModel.LoginEvent
import com.example.haven.viewModel.LoginViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun Loginscreen(
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit
) {
    val viewModel: LoginViewModel = viewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val auth = Firebase.auth


    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                LoginEvent.Success -> {
                    isLoading = false
                    onLoginSuccess()
                }
                is LoginEvent.Error -> {
                    isLoading = false
                    viewModel.updateError(event.message)
                }
            }
        }
    }

    // Loading overlay
    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(50.dp),
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Logging in...",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Welcome to Haven", fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = state.email,
                onValueChange = viewModel::onEmailChange,
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            )

            OutlinedTextField(
                value = state.password,
                onValueChange = viewModel::onPasswordChange,
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                enabled = !isLoading,
                trailingIcon = {
                    val icon = if (passwordVisible) "🙈" else "👁️"

                    TextButton(
                        onClick = { passwordVisible = !passwordVisible },
                        enabled = !isLoading
                    ) {
                        Text(text = icon, fontSize = 16.sp)
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    isLoading = true
                    viewModel.login(auth)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading && state.email.isNotBlank() && state.password.isNotBlank()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text("Login")
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(
                onClick = onRegisterClick,
                enabled = !isLoading
            ) {
                Text("Don't have an account? Register Here")
            }

            state.error?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it, color = Color.Red)
            }
        }
    }
}