package com.example.seabattle.presentation.screens.room


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seabattle.domain.Session
import com.example.seabattle.domain.usecase.room.WaitRoomUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.internal.concurrent.Task


class RoomViewModel(
    private val waitRoomUseCase: WaitRoomUseCase,
    private val session: Session,
) : ViewModel() {
    private val _uiState = MutableStateFlow<RoomUiState>(RoomUiState())
    var uiState: StateFlow<RoomUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            session.currentRoom.collect { room ->
                if (room != null) {
                    _uiState.value = RoomUiState(room = room)

                    waitRoomUseCase.invoke(room.roomId)
                        .onSuccess { waitFinished ->
                            println("Game started for player 1")
                        }
                }
            }
        }
    }
}