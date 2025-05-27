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
import kotlinx.coroutines.flow.first
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
            }
        }


        // Observe the current room from the session and react to changes
        // This will update the UI state with the current room information
        updateRoomJob = viewModelScope.launch {
            session.currentRoom.collect { room ->
                waitRoomUseCase.invoke()
                // If the room is not null, update the UI state with the room information
                if (room != null) {
                    _uiState.value = RoomUiState(room = room)
                }
            }
        }
    }


    // Function to close the room when the user leaves
    fun onUserLeave(){
        viewModelScope.launch {
            _uiState.value = RoomUiState(actionFailed = false)
            closeRoomUseCase.invoke()
                .onSuccess {
                    _uiState.value = RoomUiState(room = null)
                }
                .onFailure {
                    _uiState.value = RoomUiState(actionFailed = true)
                }
        }
    }


    fun stopListening() {
        listenRoomJob?.cancel()
        listenRoomJob = null
        updateRoomJob?.cancel()
        updateRoomJob = null
    }
}