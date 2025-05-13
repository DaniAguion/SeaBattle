package com.example.seabattle.data.firestore.mappers

import com.example.seabattle.data.firestore.dto.GameDtoRd
import com.example.seabattle.data.firestore.dto.GameDtoWr
import com.example.seabattle.domain.entity.Game


// This function maps a Game object to a create a new Game in server.
// The createdAt and updatedAt fields are initialized as server timestamps.
fun Game.toDto(): GameDtoWr =
    GameDtoWr(
        gameId = gameId,
        player1 = player1,
        player1Board = player1Board,
        player1Ready = player1Ready,
        player2 = player2,
        player2Board = player2Board,
        player2Ready = player2Ready,
        currentTurn = currentTurn,
        currentPlayer = currentPlayer,
        gameState = gameState,
        gameFinished = gameFinished,
        winnerId = winnerId,
    )


// This function is used to read a Game object from the server.
fun GameDtoRd.toEntity(): Game =
    Game(
        gameId = gameId,
        player1 = player1,
        player1Board = player1Board,
        player1Ready = player1Ready,
        player2 = player2,
        player2Board = player2Board,
        player2Ready = player2Ready,
        currentTurn = currentTurn,
        currentPlayer = currentPlayer,
        gameState = gameState,
        gameFinished = gameFinished,
        winnerId = winnerId,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
