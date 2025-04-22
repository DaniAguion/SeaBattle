package com.example.seabattle.presentation.screens.game

import com.example.seabattle.domain.model.GameBoard

data class GameUiState (
    var gameBoard: GameBoard = GameBoard()
)