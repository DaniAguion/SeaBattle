package com.example.seabattle.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seabattle.domain.usecase.user.ListenUserUseCase
import com.example.seabattle.domain.usecase.user.LogoutUserUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class ProfileViewModel(
    private val logoutUseCase: LogoutUserUseCase,
    private val listenUserUseCase: ListenUserUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState())
    var uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    // Listeners use to observe the game updates
    private var updateUIJob: Job? = null

    init {
        // Observe the current game from the session and update the UI state
        updateUIJob = viewModelScope.launch {
            listenUserUseCase.invoke()
                .collect { user ->
                    if (user != null) {
                        _uiState.value = ProfileUiState(user = user, userLoggedIn = true)
                    } else {
                        _uiState.value = ProfileUiState(userLoggedIn = false)
                    }
                }
        }
    }


    fun onLogoutButtonClicked() {
        viewModelScope.launch {
            logoutUseCase()
        }
    }


    fun stopListening() {
        updateUIJob?.cancel()
        updateUIJob = null
    }
}