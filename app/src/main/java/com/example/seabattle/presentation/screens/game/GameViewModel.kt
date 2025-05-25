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

    // Listeners used to observe the room updates
    private var listenGameJob: Job? = null
    private var updateUIJob: Job? = null


    init{
        // Observe the current game
        listenGameJob = viewModelScope.launch {
            session.currentGame.first { game ->
                if (game != null) {
                    listenGameUseCase.invoke(game.gameId)
                        .onSuccess { return@first true }
                }
                false
            }
        }


        // Observe the current game from the session and update the UI state
        updateUIJob = viewModelScope.launch {
            session.currentGame.collect { game ->
                _uiState.value = GameUiState(game = game)
            }
        }
    }


    fun onClickReady() {
        viewModelScope.launch {
            userReadyUseCase.invoke()
        }
    }

    fun enableReadyButton() : Boolean {
        val userId = session.getCurrentUserId()
        if (userId == uiState.value.game?.player1?.userId && uiState.value.game?.player1Ready == true) {
            return false
        } else if (userId == uiState.value.game?.player2?.userId && uiState.value.game?.player2Ready == true) {
            return false
        } else {
            return true
        }
    }


    fun onCellClick(x: Int, y: Int) {
        //_uiState.value = _uiState.value.copy(gameBoard = discoverCellUseCase(x, y))
    }

    fun stopListening() {
        listenGameJob?.cancel()
        listenGameJob = null
        updateUIJob?.cancel()
        updateUIJob = null
    }
}