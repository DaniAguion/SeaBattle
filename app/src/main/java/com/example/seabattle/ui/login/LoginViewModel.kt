package com.example.seabattle.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seabattle.data.repository.AuthRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class LoginViewModel(private val authRepository: AuthRepositoryImpl) : ViewModel() {
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
        viewModelScope.launch {
            authRepository.loginUser(
                email = _uiState.value.email,
                password = _uiState.value.password
            ).collect { authState ->

            }
        }
    }
}
