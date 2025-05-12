package com.example.seabattle.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seabattle.domain.usecase.room.CreateRoomUseCase
import com.example.seabattle.domain.usecase.room.GetRoomsUseCase
import com.example.seabattle.domain.usecase.room.JoinRoomUseCase
import com.example.seabattle.domain.usecase.room.WaitRoomUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.acos


class HomeViewModel(
    private val createRoomUseCase: CreateRoomUseCase,
    private val getRoomsUseCase: GetRoomsUseCase,
    private val joinRoomUseCase: JoinRoomUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(roomList = emptyList()))
    var uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()


    init {
        _uiState.value = _uiState.value.copy(
            actionFailed = false,
            loadingList = true
        )

        viewModelScope.launch {
            getRoomsUseCase.invoke()
            . collect { result ->
                result
                .onSuccess { rooms ->
                    _uiState.value = _uiState.value.copy(
                        roomList = rooms,
                        errorList = false,
                        loadingList = false
                    )
                }
                .onFailure { throwable ->
                    _uiState.value = _uiState.value.copy(
                        roomList = emptyList(),
                        errorList = true,
                        loadingList = false
                    )
                }
            }
        }
    }


    fun onClickCreateRoom() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(actionFailed = false)
            createRoomUseCase.invoke()
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        hasJoined = true
                    )
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(
                        actionFailed = true
                    )
                }
        }
    }


    fun onClickJoinRoom(roomId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(actionFailed = false)
            joinRoomUseCase.invoke(roomId)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        hasJoined = true
                    )
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(
                        actionFailed = true
                    )
                }
        }
    }
}