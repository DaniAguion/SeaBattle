package com.example.seabattle.data.firestore.mappers

import com.example.seabattle.data.firestore.dto.GameDTO
import com.example.seabattle.domain.entity.Game


fun GameDTO.toEntity(): Game =
    Game(
         gameId = gameId,
         player1 = player1,
         player1Board = player1Board,
         player2 = player2,
         player2Board = player2Board,
         currentTurn = currentTurn,
         currentPlayer = currentPlayer,
         gameState = gameState,
         gameFinished = gameFinished,
         winnerId = winnerId
    )


fun Game.toDTO(): GameDTO =
    GameDTO(
        gameId = gameId,
        player1 = player1,
        player1Board = player1Board,
        player2 = player2,
        player2Board = player2Board,
        currentTurn = currentTurn,
        currentPlayer = currentPlayer,
        gameState = gameState,
        gameFinished = gameFinished,
        winnerId = winnerId
    )
