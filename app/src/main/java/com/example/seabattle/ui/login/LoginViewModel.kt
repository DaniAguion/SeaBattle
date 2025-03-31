package com.example.seabattle.ui.login

import androidx.lifecycle.ViewModel
import com.example.seabattle.firebase.auth.AuthViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    var uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailUpdate(email: String) {
        val emailError = if (email.isEmpty()) "Please introduce your password" else ""
        _uiState.value = _uiState.value.copy(email = email, emailError = emailError)
    }

    fun onPasswordUpdate(password: String) {
        val passwordError = if (password.isEmpty()) "Please introduce your password" else ""
        _uiState.value = _uiState.value.copy(password = password, passwordError = passwordError)
    }

    fun onSignInButtonClicked() {
        val authViewModel = AuthViewModel()
        authViewModel.signIn(
            email = _uiState.value.email,
            password = _uiState.value.password
        )
    }
}
