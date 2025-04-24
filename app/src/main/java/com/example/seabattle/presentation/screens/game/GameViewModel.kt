package com.example.seabattle.presentation.screens.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seabattle.domain.game.usecases.DiscoverCellUseCase
import com.example.seabattle.domain.game.usecases.StartGameUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.invoke

class GameViewModel(
    private val startGameUseCase: StartGameUseCase,
    private val discoverCellUseCase: DiscoverCellUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState(gameBoard = startGameUseCase.getGameBoard()))
    var uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            startGameUseCase.invoke()
        }
    }

    fun onCellClick(x: Int, y: Int) {
        _uiState.value = _uiState.value.copy(gameBoard = discoverCellUseCase(x, y))
    }
}