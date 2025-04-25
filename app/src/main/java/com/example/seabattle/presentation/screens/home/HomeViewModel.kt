package com.example.seabattle.presentation.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seabattle.domain.game.usecases.CreateGameUseCase
import kotlinx.coroutines.launch

class HomeViewModel(
    private val createGameUseCase: CreateGameUseCase
) : ViewModel() {

    fun onClickCreateRoom() {
        viewModelScope.launch {
            createGameUseCase.invoke()
        }
    }

}