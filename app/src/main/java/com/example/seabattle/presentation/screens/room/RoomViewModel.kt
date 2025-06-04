package com.example.seabattle.presentation.screens.room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seabattle.domain.Session
import com.example.seabattle.domain.usecase.room.CheckGameUseCase
import com.example.seabattle.domain.usecase.room.CloseRoomUseCase
import com.example.seabattle.domain.usecase.room.ListenRoomUseCase
import com.example.seabattle.domain.usecase.room.WaitRoomUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class RoomViewModel(
    private val listenRoomUseCase: ListenRoomUseCase,
    private val waitRoomUseCase: WaitRoomUseCase,
    private val closeRoomUseCase: CloseRoomUseCase,
    private val checkGameUseCase: CheckGameUseCase,
    private val session: Session,
) : ViewModel() {
    private val _uiState = MutableStateFlow<RoomUiState>(RoomUiState())
    var uiState: StateFlow<RoomUiState> = _uiState.asStateFlow()

    // Listeners use to observe the room updates
    private var listenRoomJob: Job? = null


    init {
        // Start listening for room updates when the ViewModel is initialized
        listenRoomJob = viewModelScope.launch {
            val room = session.getCurrentRoom()
            // If the room is not null, start listening for updates
            if (room != null && room.roomId.isNotEmpty()) {
                listenRoomUseCase.invoke(room.roomId)
                    .collect { result ->
                        result
                            .onSuccess { room ->
                                // If the room is not null, update the UI state and react to the room updates
                                if (room != null) {
                                    _uiState.value = _uiState.value.copy(room = room)
                                    waitRoomUseCase.invoke()
                                        .onFailure { e ->
                                            _uiState.value = _uiState.value.copy(errorMessage = e.message)
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


    // Function to close the room when the user leaves
    fun onUserLeave(){
        viewModelScope.launch {
            closeRoomUseCase.invoke()
                .onSuccess {
                    stopListening() // Stop listening to the room updates before clearing the room
                    _uiState.value = _uiState.value.copy(room = null)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(errorMessage = e.message)
                }
        }
    }

    // Function to check if the room is in a game created state
    fun gameIsReady(): Boolean {
        return checkGameUseCase.invoke()
    }


    fun onErrorShown(){
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }


    fun stopListening() {
        listenRoomJob?.cancel()
        listenRoomJob = null
    }
}