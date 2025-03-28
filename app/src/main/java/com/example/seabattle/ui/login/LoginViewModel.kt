package com.example.seabattle.ui.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow



class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    var uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailUpdate(email: String) {
        _uiState.value = _uiState.value.copy(email = email)
    }

    fun onPasswordUpdate(password: String) {
        _uiState.value = _uiState.value.copy(password = password)
    }
}