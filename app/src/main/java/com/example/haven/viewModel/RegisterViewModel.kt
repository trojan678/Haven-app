package com.example.haven.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class RegisterState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val error: String? = null
)

class RegisterViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterState())
    val uiState: StateFlow<RegisterState> = _uiState

    private val _events = Channel<RegisterEvent>()
    val events = _events.receiveAsFlow()

    private val auth = FirebaseAuth.getInstance()

    fun onNameChange(name: String) {
        _uiState.update { it.copy(name = name, error = null) }
    }

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, error = null) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, error = null) }
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = confirmPassword, error = null) }
    }

    fun updateError(message: String?) {
        _uiState.update { it.copy(error = message) }
    }

    fun register() {
        val currentState = _uiState.value

        viewModelScope.launch {
            try {

                when {
                    currentState.email.isEmpty() -> {
                        _events.send(RegisterEvent.Error("Email cannot be empty"))
                    }
                    currentState.password.isEmpty() -> {
                        _events.send(RegisterEvent.Error("Password cannot be empty"))
                    }
                    currentState.password != currentState.confirmPassword -> {
                        _events.send(RegisterEvent.Error("Passwords do not match"))
                    }
                    else -> {

                        auth.createUserWithEmailAndPassword(
                            currentState.email,
                            currentState.password
                        ).await()

                        _events.send(RegisterEvent.Success)
                    }
                }
            } catch (e: Exception) {
                _events.send(RegisterEvent.Error(e.message ?: "Registration failed"))
            }
        }
    }
}