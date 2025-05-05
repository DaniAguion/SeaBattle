package com.example.seabattle.presentation.screens.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seabattle.domain.usecase.auth.CheckUserAuthUseCase
import com.example.seabattle.domain.usecase.auth.GetProfileUseCase
import com.example.seabattle.domain.usecase.auth.LogoutUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val logoutUseCase: LogoutUserUseCase,
    private val checkAuthUseCase: CheckUserAuthUseCase,
    private val getProfileUseCase: GetProfileUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState())
    var uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(user = getProfileUseCase())
            Log.d("ProfileViewModel", "User profile: ${_uiState.value.user}")
        }
    }

    fun onLogoutButtonClicked() {
        viewModelScope.launch {
            logoutUseCase()
        }
        val isLogged = checkAuthUseCase()
        _uiState.value = _uiState.value.copy(userLoggedIn = isLogged)
    }
}