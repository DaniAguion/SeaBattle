package com.example.seabattle.presentation.screens.room


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seabattle.domain.Session
import com.example.seabattle.domain.usecase.room.CloseRoomUseCase
import com.example.seabattle.domain.usecase.room.WaitRoomUseCase
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

    init {
        viewModelScope.launch {
            session.currentRoom.collect { room ->
                if (room != null) {
                    _uiState.value = RoomUiState(room = room)
                }
            }
        }
        viewModelScope.launch {
            session.currentRoom.first { room ->
                if (room != null) {
                    waitRoomUseCase.invoke(room.roomId)
                        .onSuccess { return@first true }
                }
                false
            }
        }
    }

    fun onUserLeave(){
        viewModelScope.launch {
            closeRoomUseCase.invoke()
        }
    }
}