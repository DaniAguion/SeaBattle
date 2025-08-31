package com.example.seabattle.data.firestore.mappers

import com.example.seabattle.data.firestore.dto.GameCreationDto
import com.example.seabattle.data.firestore.dto.GameDto
import com.example.seabattle.domain.entity.Game


// Function to convert Game DTO to Game Entity
fun GameDto.toEntity(): Game {
    return Game(
        gameId = gameId,
        privateGame = privateGame,
        player1 = player1.toEntity(),
        player1Ready = player1Ready,
        boardForPlayer1 = boardForPlayer1,
        player1Ships = player1Ships,
        player2 = player2.toEntity(),
        player2Ready = player2Ready,
        boardForPlayer2 = boardForPlayer2,
        player2Ships = player2Ships,
        currentPlayer = currentPlayer,
        gameState = gameState,
        winnerId = winnerId,
        createdAt = createdAt?.toInstant(),
        expireAt = expireAt?.toInstant(),
        updatedAt = updatedAt?.toInstant(),
    )
}


// Function to convert Game Entity to create Game DTO
fun Game.toGameCreationDto(): GameCreationDto {
    return GameCreationDto(
        gameId = gameId,
        privateGame = privateGame,
        player1 = player1.toDto(),
        player1Ready = player1Ready,
        boardForPlayer1 = boardForPlayer1,
        player1Ships = player1Ships,
        player2 = player2.toDto(),
        player2Ready = player2Ready,
        boardForPlayer2 = boardForPlayer2,
        player2Ships = player2Ships,
        currentPlayer = currentPlayer,
        gameState = gameState,
        winnerId = winnerId
    )
}
