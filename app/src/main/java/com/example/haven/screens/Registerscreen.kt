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
import com.example.haven.viewModel.RegisterEvent
import com.example.haven.viewModel.RegisterViewModel

@Composable
fun Registerscreen(
    onRegisterSuccess: () -> Unit,
    onLoginClick: () -> Unit
) {
    val viewModel: RegisterViewModel = viewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    // State for password visibility
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                RegisterEvent.Success -> onRegisterSuccess()
                is RegisterEvent.Error -> viewModel.updateError(event.message)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Create Account", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = state.name,
            onValueChange = viewModel::onNameChange,
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.email,
            onValueChange = viewModel::onEmailChange,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = state.password,
            onValueChange = viewModel::onPasswordChange,
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon = if (passwordVisible) "🙈" else "👁️"

                TextButton(onClick = { passwordVisible = !passwordVisible }) {
                    Text(text = icon, fontSize = 16.sp)
                }
            }
        )

        OutlinedTextField(
            value = state.confirmPassword,
            onValueChange = viewModel::onConfirmPasswordChange,
            label = { Text("Confirm Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val icon = if (confirmPasswordVisible) "🙈" else "👁️"

                TextButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                    Text(text = icon, fontSize = 16.sp)
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.register() },
            modifier = Modifier.fillMaxWidth(),
            enabled = state.error == null
        ) {
            Text("Create Account")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onLoginClick) {
            Text("Already have an account? Login")
        }

        state.error?.let { errorMessage ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.align(Alignment.Start)
            )
        }
    }
}