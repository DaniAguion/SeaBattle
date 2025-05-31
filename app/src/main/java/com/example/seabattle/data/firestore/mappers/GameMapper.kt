package com.example.seabattle.data.firestore.mappers

import com.example.seabattle.data.firestore.dto.GameDto
import com.example.seabattle.domain.entity.Game


// Function to convert Game DTO to Game Entity
fun GameDto.toGameEntity(): Game {
    return Game(
        gameId = gameId,
        player1 = player1,
        boardForPlayer2 = boardForPlayer2,
        player1Ready = player1Ready,
        player1Ships = player1Ships,
        player2 = player2,
        boardForPlayer1 = boardForPlayer1,
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


// Function to convert Game Entity to Game DTO
fun Game.toGameDto(): GameDto {
    return GameDto(
        gameId = gameId,
        player1 = player1,
        boardForPlayer2 = boardForPlayer2,
        player1Ready = player1Ready,
        player1Ships = player1Ships,
        player2 = player2,
        boardForPlayer1 = boardForPlayer1,
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
