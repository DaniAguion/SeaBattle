package com.example.seabattle.data.firestore.mappers

import com.example.seabattle.data.firestore.dto.UserGamesDto
import com.example.seabattle.domain.entity.UserGames


fun UserGames.toDto(): UserGamesDto =
    UserGamesDto(
        userId = userId,
        currentGameId = currentGameId,
        invitedToGameId = invitedToGameId,
        history = historial.map { it.toDto() }
    )


fun UserGamesDto.toEntity(): UserGames =
    UserGames(
        userId = userId,
        currentGameId = currentGameId,
        invitedToGameId = invitedToGameId,
        historial = history.map { it.toEntity() }
    )
