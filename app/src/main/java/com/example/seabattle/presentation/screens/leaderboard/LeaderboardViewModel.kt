package com.example.seabattle.presentation.screens.leaderboard

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LeaderboardViewModel() : ViewModel() {
    private val _uiState = MutableStateFlow(LeaderboardUiState(usersList = emptyList()))
    var uiState: StateFlow<LeaderboardUiState> = _uiState.asStateFlow()

}