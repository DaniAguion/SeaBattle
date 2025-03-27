package com.example.seabattle.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.seabattle.model.LoginUiState

class LoginViewModel : ViewModel() {
    var uiState = mutableStateOf(LoginUiState())
        private set
}