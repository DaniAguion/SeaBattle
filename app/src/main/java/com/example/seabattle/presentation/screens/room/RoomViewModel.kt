package com.example.seabattle.presentation.screens.room

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seabattle.domain.Session
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
    private val session: Session,
) : ViewModel() {
    private val _uiState = MutableStateFlow<RoomUiState>(RoomUiState())
    var uiState: StateFlow<RoomUiState> = _uiState.asStateFlow()

    // Listeners use to observe the room updates
    private var listenRoomJob: Job? = null
    private var updateRoomJob: Job? = null


    init {
        // Observe the assigned room and start listening for updates
        listenRoomJob = viewModelScope.launch {
            val room = session.getCurrentRoom()
            if (room != null) {
                listenRoomUseCase.invoke(room.roomId)
                    .onFailure { e ->
                        _uiState.value = RoomUiState(errorMessage = e.message)
                    }
            }
        }


        // Observe the current room from the session and react to changes
        // This will update the UI state with the current room information
        updateRoomJob = viewModelScope.launch {
            session.currentRoom.collect { room ->
                if (room != null) {
                    waitRoomUseCase.invoke()
                    _uiState.value = RoomUiState(room = room)
                }
            }
        }
    }


    // Function to close the room when the user leaves
    fun onUserLeave(){
        viewModelScope.launch {
            closeRoomUseCase.invoke()
                .onSuccess {
                    _uiState.value = RoomUiState(room = null)
                }
                .onFailure { e ->
                    _uiState.value = RoomUiState(errorMessage = e.message)
                }
        }
    }


    fun onErrorShown(){
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }


    fun stopListening() {
        listenRoomJob?.cancel()
        listenRoomJob = null
        updateRoomJob?.cancel()
        updateRoomJob = null
    }
}