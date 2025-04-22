package com.example.seabattle.presentation.screens.game

import androidx.lifecycle.ViewModel
import com.example.seabattle.domain.game.usecases.CellActionUseCase
import com.example.seabattle.domain.game.usecases.StartGameUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel(
    private val cellActionUseCase: CellActionUseCase,
    private val startGameUseCase: StartGameUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState(gameBoard = startGameUseCase()))
    var uiState: StateFlow<GameUiState> = _uiState.asStateFlow()


    fun onCellClick(x: Int, y: Int) {
        _uiState.value = _uiState.value.copy(gameBoard = cellActionUseCase(x, y))
    }
}