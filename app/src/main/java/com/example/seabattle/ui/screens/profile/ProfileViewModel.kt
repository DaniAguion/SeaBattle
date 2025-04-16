package com.example.seabattle.ui.screens.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seabattle.domain.auth.usecase.CheckUserAuthUseCase
import com.example.seabattle.domain.auth.usecase.GetProfileUseCase
import com.example.seabattle.domain.auth.usecase.LogoutUserUseCase
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
            _uiState.value = _uiState.value.copy(userProfile = getProfileUseCase())
            Log.d("ProfileViewModel", "User profile: ${_uiState.value.userProfile}")
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