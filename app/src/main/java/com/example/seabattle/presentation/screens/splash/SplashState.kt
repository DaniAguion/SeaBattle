package com.example.seabattle.presentation.screens.splash

sealed class SplashState {
    object Loading : SplashState()
    data class Success(val isLogged: Boolean) : SplashState()
    data class Error(val message: String) : SplashState()
}