package com.example.seabattle.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seabattle.domain.room.usescases.CreateRoomUseCase
import kotlinx.coroutines.launch

class HomeViewModel(
    private val createRoomUseCase: CreateRoomUseCase
) : ViewModel() {

    fun onClickCreateRoom() {
        viewModelScope.launch {
            createRoomUseCase.invoke()
        }
    }

}