package com.example.seabattle.data.firestore.mappers

import com.example.seabattle.data.firestore.entities.GameEntity
import com.example.seabattle.domain.model.Game


fun GameEntity.toDomainModel(): Game =
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


fun Game.toEntity(): GameEntity =
    GameEntity(
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
