package com.example.seabattle.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seabattle.domain.usecase.room.CreateRoomUseCase
import com.example.seabattle.domain.usecase.room.GetRoomsUseCase
import com.example.seabattle.domain.usecase.room.JoinRoomUseCase
import com.example.seabattle.presentation.validation.Validator
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class HomeViewModel(
    private val createRoomUseCase: CreateRoomUseCase,
    private val getRoomsUseCase: GetRoomsUseCase,
    private val joinRoomUseCase: JoinRoomUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(roomList = emptyList()))
    var uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // Active listener use get the updated room list
    private var getRoomsJob: Job? = null

    init {
        _uiState.value = _uiState.value.copy(loadingList = true)

        getRoomsJob = viewModelScope.launch {
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
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        roomList = emptyList(),
                        errorList = true,
                        loadingList = false,
                        errorMessage = e.message
                    )
                }
            }
        }
    }

    fun onRoomNameUpdate(roomName: String) {
        val validationResult = Validator.validateRoomName(roomName)
        _uiState.value = _uiState.value.copy(roomNameError = validationResult)
        _uiState.value = _uiState.value.copy(roomName = roomName)
    }


    fun onClickCreateRoom(roomName: String) {
        onRoomNameUpdate(roomName = roomName)
        if (uiState.value.roomNameError != null){
            return
        }
        viewModelScope.launch {
            createRoomUseCase.invoke(roomName = roomName)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(hasJoined = true)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(errorMessage = e.message)
                }
        }
    }


    fun onClickJoinRoom(roomId: String) {
        viewModelScope.launch {
            joinRoomUseCase.invoke(roomId)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(hasJoined = true)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(errorMessage = e.message)
                }
        }
    }


    fun onErrorShown(){
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun stopListening() {
        getRoomsJob?.cancel()
        getRoomsJob = null
    }
}