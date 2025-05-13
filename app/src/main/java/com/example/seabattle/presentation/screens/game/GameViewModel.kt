package com.example.seabattle.presentation.screens.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seabattle.domain.Session
import com.example.seabattle.domain.usecase.game.GameReadyUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class GameViewModel(
    private val session: Session,
    private val gameReadyUseCase: GameReadyUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow<GameUiState>(GameUiState())
    var uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    init{
        viewModelScope.launch {
            session.currentGame.collect { game ->
                _uiState.value = GameUiState(game = game)
            }
        }
    }


    fun onClickReady() {
        viewModelScope.launch {
            gameReadyUseCase.invoke()
        }
    }


    fun onCellClick(x: Int, y: Int) {
        //_uiState.value = _uiState.value.copy(gameBoard = discoverCellUseCase(x, y))
    }

}