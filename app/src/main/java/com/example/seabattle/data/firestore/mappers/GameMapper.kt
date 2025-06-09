package com.example.seabattle.data.firestore.mappers

import com.example.seabattle.data.firestore.dto.GameCreationDto
import com.example.seabattle.data.firestore.dto.GameDto
import com.example.seabattle.domain.entity.Game


// Function to convert Game DTO to Game Entity
fun GameDto.toGameEntity(): Game {
    return Game(
        gameId = gameId,
        gameName = gameName,
        numberOfPlayers = numberOfPlayers,
        player1 = player1,
        player1Ready = player1Ready,
        boardForPlayer1 = boardForPlayer1,
        player1Ships = player1Ships,
        player2 = player2,
        player2Ready = player2Ready,
        boardForPlayer2 = boardForPlayer2,
        player2Ships = player2Ships,
        currentMove = currentMove,
        currentPlayer = currentPlayer,
        gameState = gameState,
        winnerId = winnerId
    )
}


// Function to convert Game Entity to create Game DTO
fun Game.toGameCreationDto(): GameCreationDto {
    return GameCreationDto(
        gameId = gameId,
        gameName = gameName,
        numberOfPlayers = numberOfPlayers,
        player1 = player1,
        player1Ready = player1Ready,
        boardForPlayer1 = boardForPlayer1,
        player1Ships = player1Ships,
        player2 = player2,
        player2Ready = player2Ready,
        boardForPlayer2 = boardForPlayer2,
        player2Ships = player2Ships,
        currentMove = currentMove,
        currentPlayer = currentPlayer,
        gameState = gameState,
        winnerId = winnerId,
    )
}
