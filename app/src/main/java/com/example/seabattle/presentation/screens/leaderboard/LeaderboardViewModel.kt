package com.example.seabattle.presentation.screens.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seabattle.domain.usecase.GetLeaderboardUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LeaderboardViewModel(
    private val getLeaderboardUseCase : GetLeaderboardUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(LeaderboardUiState(usersList = emptyList()))
    var uiState: StateFlow<LeaderboardUiState> = _uiState.asStateFlow()


    fun getLeaderboard() {
        viewModelScope.launch {
            getLeaderboardUseCase.invoke()
                .onSuccess { usersList ->
                    _uiState.value = _uiState.value.copy(
                        usersList = usersList,
                        errorList = false,
                        loadingList = false
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        usersList = emptyList(),
                        errorList = true,
                        loadingList = false,
                        errorMessage = e.message
                    )
                }
        }
    }


    fun onErrorShown(){
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}