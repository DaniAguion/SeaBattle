package com.example.seabattle.presentation.screens.game

import androidx.lifecycle.ViewModel
import com.example.seabattle.domain.auth.usecase.CellActionUseCase

class GameViewModel(
    private val cellActionUseCase: CellActionUseCase,
) : ViewModel() {
    fun onCellClick(x: Int, y: Int) {
        cellActionUseCase(x, y)
    }
}