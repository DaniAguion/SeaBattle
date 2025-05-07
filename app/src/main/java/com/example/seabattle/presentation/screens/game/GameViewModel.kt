package com.example.seabattle.presentation.screens.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seabattle.domain.Session
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class GameViewModel(
    private val session: Session,
) : ViewModel() {
    private val _uiState = MutableStateFlow<GameUiState>(GameUiState())
    var uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    init{
        viewModelScope.launch {
            session.currentGame.collect { game ->
                if (game != null) {
                    _uiState.value = GameUiState(game = game)
                }
            }
        }
    }

    fun onCellClick(x: Int, y: Int) {
        //_uiState.value = _uiState.value.copy(gameBoard = discoverCellUseCase(x, y))
    }

}