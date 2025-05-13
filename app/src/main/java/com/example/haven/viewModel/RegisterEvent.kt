package com.example.haven.viewModel

sealed class RegisterEvent {
    object Success : RegisterEvent()
    data class Error(val message: String) : RegisterEvent()


}
