package com.example.haven.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

sealed class LoginEvent {
    object Success : LoginEvent()
    data class Error(val message: String) : LoginEvent()
}

data class LoginState(
    val email: String = "",
    val password: String = "",
    val error: String? = null
)

class LoginViewModel : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val uiState: StateFlow<LoginState> = _state.asStateFlow()

    private val _events = Channel<LoginEvent>()
    val events = _events.receiveAsFlow()
    // private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    fun onEmailChange(email: String) {
        _state.update { it.copy(email = email) }
    }

    fun onPasswordChange(password: String) {
        _state.update { it.copy(password = password) }
    }

    fun updateError(message: String?) {
        _state.update { it.copy(error = message) }
    }

    fun login(auth: FirebaseAuth) {
        viewModelScope.launch {
            try {
                val result = auth.signInWithEmailAndPassword(
                    _state.value.email,
                    _state.value.password
                ).await()
                if (result != null){
                _events.send(LoginEvent.Success)
                } else {
                    _events.send(LoginEvent.Error("Login Unsuccessful"))
                }

            } catch (e: Exception) {
                _events.send(LoginEvent.Error(e.message ?: "Login Failed"))
            }
        }
    }
}
