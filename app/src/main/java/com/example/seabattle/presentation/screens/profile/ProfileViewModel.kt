package com.example.seabattle.presentation.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seabattle.domain.Session
import com.example.seabattle.domain.usecase.auth.CheckUserAuthUseCase
import com.example.seabattle.domain.usecase.auth.LogoutUserUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val logoutUseCase: LogoutUserUseCase,
    private val checkAuthUseCase: CheckUserAuthUseCase,
    private val session: Session
) : ViewModel() {
    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState())
    var uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    // Listeners use to observe the room updates
    private var updateUIJob: Job? = null

    init {
        // Observe the current room from the session and update the UI state
        updateUIJob = viewModelScope.launch {
            session.currentUser.collect { user ->
                if (user != null) {
                    _uiState.value = ProfileUiState(user = user)
                }
            }
        }
    }


    fun onLogoutButtonClicked() {
        viewModelScope.launch {
            logoutUseCase()
        }
        val isLogged = checkAuthUseCase()
        _uiState.value = _uiState.value.copy(userLoggedIn = isLogged)
    }


    fun stopListening() {
        updateUIJob?.cancel()
        updateUIJob = null
    }
}