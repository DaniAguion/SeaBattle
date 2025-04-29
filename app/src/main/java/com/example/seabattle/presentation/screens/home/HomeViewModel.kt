package com.example.seabattle.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seabattle.domain.room.usescases.CreateRoomUseCase
import com.example.seabattle.domain.room.usescases.GetRoomsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val createRoomUseCase: CreateRoomUseCase,
    private val getRoomsUseCase: GetRoomsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(roomList = emptyList()))
    var uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getRoomsUseCase.invoke()
                .onSuccess { rooms ->
                    _uiState.value = _uiState.value.copy(roomList = rooms, errorList = false)
                }.onFailure {
                    _uiState.value = _uiState.value.copy(errorList = true)
                }
        }
    }

    fun onClickCreateRoom() {
        viewModelScope.launch {
            createRoomUseCase.invoke()
        }
    }

}