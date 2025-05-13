package com.example.seabattle.presentation.screens.room

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seabattle.domain.Session
import com.example.seabattle.domain.usecase.room.CloseRoomUseCase
import com.example.seabattle.domain.usecase.room.WaitRoomUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class RoomViewModel(
    private val waitRoomUseCase: WaitRoomUseCase,
    private val closeRoomUseCase: CloseRoomUseCase,
    private val session: Session,
) : ViewModel() {
    private val _uiState = MutableStateFlow<RoomUiState>(RoomUiState())
    var uiState: StateFlow<RoomUiState> = _uiState.asStateFlow()

    // Listeners use to observe the room updates
    private var updateUIJob: Job? = null
    private var waitRoomJob: Job? = null

    init {
        // Observe the current room from the session and update the UI state
        updateUIJob = viewModelScope.launch {
            session.currentRoom.collect { room ->
                if (room != null) {
                    _uiState.value = RoomUiState(room = room)
                }
            }
        }


        // Observe the current room and execute the waitRoomUseCase until success
        waitRoomJob = viewModelScope.launch {
            session.currentRoom.first { room ->
                if (room != null) {
                    waitRoomUseCase.invoke(room.roomId)
                        .onSuccess { return@first true }
                }
                false
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
        updateUIJob?.cancel()
        updateUIJob = null
        waitRoomJob?.cancel()
        waitRoomJob = null
    }
}