package com.example.seabattle.presentation.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seabattle.domain.usecase.auth.GetAuthUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val getAuthUserUseCase: GetAuthUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<SplashState>(SplashState.Loading)
    val uiState: StateFlow<SplashState> = _uiState

    init {
        viewModelScope.launch {
            try {
                val authUser = getAuthUserUseCase.invoke().getOrThrow()
                _uiState.value = SplashState.Success(authUser != null)
            } catch (e: Exception) {
                _uiState.value = SplashState.Error(e.message ?: "Unkwown error in splash screen")
            }
        }
    }

}
