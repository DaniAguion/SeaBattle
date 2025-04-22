package com.example.seabattle.presentation.screens.game

import androidx.lifecycle.ViewModel
import com.example.seabattle.domain.game.usecases.CellActionUseCase
import com.example.seabattle.domain.game.usecases.StartGameUseCase
import com.example.seabattle.domain.model.Cell
import com.example.seabattle.domain.model.GameBoard
import com.example.seabattle.presentation.screens.welcome.WelcomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel(
    private val cellActionUseCase: CellActionUseCase,
    private val startGameUseCase: StartGameUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState(GameBoard()))
    var uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    init{
        _uiState.value = GameUiState(
            gameBoard = startGameUseCase.getGameBoard()
        )
    }

    fun onCellClick(x: Int, y: Int) {
        cellActionUseCase(x, y)
    }
}