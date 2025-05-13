package com.example.seabattle.presentation.screens.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seabattle.domain.Session
import com.example.seabattle.domain.usecase.game.ListenGameUseCase
import com.example.seabattle.domain.usecase.game.UserReadyUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class GameViewModel(
    private val session: Session,
    private val userReadyUseCase: UserReadyUseCase,
    private val listenGameUseCase: ListenGameUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow<GameUiState>(GameUiState())
    var uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    // Listeners use to observe the room updates
    private var updateUIJob: Job? = null
    private var waitGameJob: Job? = null

    init{
        updateUIJob = viewModelScope.launch {
            session.currentGame.collect { game ->
                _uiState.value = GameUiState(game = game)
            }
        }

        // Observe the current game
        waitGameJob = viewModelScope.launch {
            session.currentGame.first { game ->
                if (game != null) {
                    listenGameUseCase.invoke(game.gameId)
                        .onSuccess { return@first true }
                }
                false
            }
        }
    }


    fun onClickReady() {
        viewModelScope.launch {
            userReadyUseCase.invoke()
        }
    }


    fun onCellClick(x: Int, y: Int) {
        //_uiState.value = _uiState.value.copy(gameBoard = discoverCellUseCase(x, y))
    }

    fun stopListening() {
        updateUIJob?.cancel()
        updateUIJob = null
        waitGameJob?.cancel()
        waitGameJob = null
    }
}