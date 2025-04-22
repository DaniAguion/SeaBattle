package com.example.seabattle.presentation.screens.game

import androidx.lifecycle.ViewModel
import com.example.seabattle.domain.game.usecases.CellActionUseCase
import com.example.seabattle.presentation.screens.welcome.WelcomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel(
    private val cellActionUseCase: CellActionUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState())
    var uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    fun onCellClick(x: Int, y: Int) {
        cellActionUseCase(x, y)
    }
}