package com.example.seabattle.presentation.screens.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seabattle.domain.Session
import com.example.seabattle.domain.usecase.game.JoinGameUseCase
import com.example.seabattle.domain.usecase.game.LeaveGameUseCase
import com.example.seabattle.domain.usecase.game.ListenGameUseCase
import com.example.seabattle.domain.usecase.game.MakeMoveUseCase
import com.example.seabattle.domain.usecase.game.UserReadyUseCase
import com.example.seabattle.domain.usecase.room.CloseRoomUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch



class GameViewModel(
    private val session: Session,
    private val joinGameUseCase: JoinGameUseCase,
    private val closeRoomUseCase: CloseRoomUseCase,
    private val userReadyUseCase: UserReadyUseCase,
    private val makeMoveUseCase: MakeMoveUseCase,
    private val listenGameUseCase: ListenGameUseCase,
    private val leaveGameUseCase: LeaveGameUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<GameUiState>(GameUiState())
    var uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    // Listeners used to observe the room updates
    private var listenGameJob: Job? = null
    private var closedRoom = false

    init{
        // Initialize the UI state with the current user ID
        val userId = session.getCurrentUserId()
        _uiState.value = _uiState.value.copy(userId = userId)


        // Observe the current game
        listenGameJob = viewModelScope.launch {
            val game = session.getCurrentGame()
            // If the game is not null, start listening for updates
            if ( game != null && game.gameId.isNotEmpty()) {
                // First indicate that the user has joined the game
                listenGameUseCase.invoke(game.gameId)
                    .first()
                    .onSuccess { collectedGame ->
                        _uiState.value = _uiState.value.copy(game = collectedGame)
                        joinGameUseCase.invoke()
                            .onFailure { e ->
                                _uiState.value = _uiState.value.copy(errorMessage = e.message)
                            }
                    }
                    .onFailure { e ->
                        _uiState.value = _uiState.value.copy(errorMessage = e.message)
                    }


                listenGameUseCase.invoke(game.gameId)
                    .collect { result ->
                        result
                            .onSuccess { collectedGame ->
                                // Update the UI state with the latest game data
                                _uiState.value = _uiState.value.copy(game = collectedGame)
                                // If both player has joined the game is in "CHECK_READY" state
                                // The room can be closed

                                if (collectedGame.gameState == "CHECK_READY"){
                                    if (!closedRoom) {
                                        closeRoomUseCase.invoke()
                                            .onSuccess {
                                                closedRoom = true
                                            }
                                            .onFailure { e ->
                                                _uiState.value = _uiState.value.copy(errorMessage = e.message)
                                            }
                                    }
                                }
                            }
                            .onFailure { e ->
                                _uiState.value = _uiState.value.copy(errorMessage = e.message)
                            }
                    }
            }
        }
    }


    fun onClickReady() {
        viewModelScope.launch {
            userReadyUseCase.invoke()
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(errorMessage = e.message)
                }
        }
    }


    fun enableReadyButton() : Boolean {
        val userId = session.getCurrentUserId()
        if (uiState.value.game?.gameState != "CHECK_READY") {
            return false
        }
        if (userId == uiState.value.game?.player1?.userId && uiState.value.game?.player1Ready == true) {
            return false
        } else if (userId == uiState.value.game?.player2?.userId && uiState.value.game?.player2Ready == true) {
            return false
        } else {
            return true
        }
    }


    fun onUserLeave() {
        viewModelScope.launch {
            leaveGameUseCase.invoke()
                .onSuccess {
                    stopListening() // Stop listening to the game updates before clearing the game
                    _uiState.value = _uiState.value.copy(game = null)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(errorMessage = e.message)
                }
        }
    }

    fun onClickCell(x: Int, y: Int){
        viewModelScope.launch {
            makeMoveUseCase.invoke(x, y)
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(errorMessage = e.message)
                }
        }
    }

    fun enableClickCell(gameBoardOwner: String) : Boolean {
        val userId = session.getCurrentUserId()
        if (
            gameBoardOwner == "player1"
            && (userId == uiState.value.game?.player1?.userId)
            && (userId == uiState.value.game?.currentPlayer)
        ) {
            return true
        } else if (
            gameBoardOwner == "player2"
            && (userId == uiState.value.game?.player2?.userId)
            && (userId == uiState.value.game?.currentPlayer)
        ) {
            return true
        }
        else return false
    }


    fun onErrorShown(){
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }


    fun stopListening() {
        listenGameJob?.cancel()
        listenGameJob = null
    }
}