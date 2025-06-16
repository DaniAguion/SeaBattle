package com.example.seabattle.presentation.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seabattle.domain.usecase.auth.CheckUserAuthUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val checkUserAuthUseCase: CheckUserAuthUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<SplashState>(SplashState.Loading)
    val uiState: StateFlow<SplashState> = _uiState

    init {
        viewModelScope.launch {
            try {
                val isLogged = checkUserAuthUseCase()
                _uiState.value = SplashState.Success(isLogged)
            } catch (e: Exception) {
                _uiState.value = SplashState.Error(e.message ?: "Unkwown error in splash screen")
            }
        }
    }

}
