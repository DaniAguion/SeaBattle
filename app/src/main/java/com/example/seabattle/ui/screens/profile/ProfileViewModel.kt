package com.example.seabattle.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seabattle.domain.auth.usecase.CheckUserAuthUseCase
import com.example.seabattle.domain.auth.usecase.LogoutUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val logoutUserUseCase: LogoutUserUseCase,
    private val checkUserAuthUseCase: CheckUserAuthUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState())
    var uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun onLogoutButtonClicked() {
        viewModelScope.launch {
            logoutUserUseCase()
        }
        val isLogged = checkUserAuthUseCase()
        _uiState.value = _uiState.value.copy(userLoggedIn = isLogged)
    }
}