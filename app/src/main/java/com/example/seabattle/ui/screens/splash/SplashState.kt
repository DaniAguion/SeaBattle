package com.example.seabattle.ui.screens.splash

sealed class SplashState {
    object Loading : SplashState()
    data class Success(val isLogged: Boolean) : SplashState()
    data class Error(val message: String) : SplashState()
}