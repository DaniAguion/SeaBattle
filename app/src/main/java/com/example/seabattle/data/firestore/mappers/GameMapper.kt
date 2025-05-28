package com.example.seabattle.data.firestore.mappers

import com.example.seabattle.data.firestore.dto.GameDto
import com.example.seabattle.domain.entity.Game


// Function to convert Game DTO to Game Entity
fun GameDto.toGameEntity(): Game {
    return Game(
        gameId = gameId,
        player1 = player1,
        player1Board = player1Board,
        player1Ready = player1Ready,
        player1Ships = player1Ships,
        player2 = player2,
        player2Board = player2Board,
        player2Ready = player2Ready,
        player2Ships = player2Ships,
        currentTurn = currentTurn,
        currentPlayer = currentPlayer,
        gameState = gameState,
        gameFinished = gameFinished,
        winnerId = winnerId,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}