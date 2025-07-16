package com.example.seabattle.data.firestore.mappers

import com.example.seabattle.data.firestore.dto.GameCreationDto
import com.example.seabattle.data.firestore.dto.GameDto
import com.example.seabattle.data.firestore.dto.PlayerDto
import com.example.seabattle.domain.entity.Game
import com.example.seabattle.domain.entity.Player


// Function to convert Game DTO to Game Entity
fun GameDto.toEntity(): Game {
    return Game(
        gameId = gameId,
        gameName = gameName,
        privateGame = privateGame,
        player1 = Player(
            userId = player1.userId,
            displayName = player1.displayName,
            photoUrl = player1.photoUrl,
            status = player1.status,
            score = player1.score
        ),
        player1Ready = player1Ready,
        boardForPlayer1 = boardForPlayer1,
        player1Ships = player1Ships,
        player2 = Player(
            userId = player2.userId,
            displayName = player2.displayName,
            photoUrl = player2.photoUrl,
            status = player2.status,
            score = player2.score
        ),
        player2Ready = player2Ready,
        boardForPlayer2 = boardForPlayer2,
        player2Ships = player2Ships,
        currentPlayer = currentPlayer,
        gameState = gameState,
        winnerId = winnerId,
        createdAt = createdAt,
        expireAt = expireAt,
        updatedAt = updatedAt,
    )
}


// Function to convert Game Entity to create Game DTO
fun Game.toGameCreationDto(): GameCreationDto {
    return GameCreationDto(
        gameId = gameId,
        gameName = gameName,
        privateGame = privateGame,
        player1 = PlayerDto(
            userId = player1.userId,
            displayName = player1.displayName,
            photoUrl = player1.photoUrl,
            status = player1.status,
            score = player1.score
        ),
        player1Ready = player1Ready,
        boardForPlayer1 = boardForPlayer1,
        player1Ships = player1Ships,
        player2 = PlayerDto(
            userId = player2.userId,
            displayName = player2.displayName,
            photoUrl = player2.photoUrl,
            status = player2.status,
            score = player2.score
        ),
        player2Ready = player2Ready,
        boardForPlayer2 = boardForPlayer2,
        player2Ships = player2Ships,
        currentPlayer = currentPlayer,
        gameState = gameState,
        winnerId = winnerId
    )
}
