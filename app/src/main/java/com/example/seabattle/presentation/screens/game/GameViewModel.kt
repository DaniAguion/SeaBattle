package com.example.seabattle.presentation.screens.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.seabattle.domain.usecase.game.DiscoverCellUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameViewModel(
    private val discoverCellUseCase: DiscoverCellUseCase,
) : ViewModel() {


    fun onCellClick(x: Int, y: Int) {
        //_uiState.value = _uiState.value.copy(gameBoard = discoverCellUseCase(x, y))
    }

}