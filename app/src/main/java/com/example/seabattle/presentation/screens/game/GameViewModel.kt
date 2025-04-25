package com.example.seabattle.presentation.screens.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seabattle.domain.game.usecases.DiscoverCellUseCase
import com.example.seabattle.domain.game.usecases.CreateGameUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameViewModel(
    private val createGameUseCase: CreateGameUseCase,
    private val discoverCellUseCase: DiscoverCellUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState(gameBoard = createGameUseCase.getGameBoard()))
    var uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            createGameUseCase.invoke()
        }
    }

    fun onCellClick(x: Int, y: Int) {
        _uiState.value = _uiState.value.copy(gameBoard = discoverCellUseCase(x, y))
    }
}