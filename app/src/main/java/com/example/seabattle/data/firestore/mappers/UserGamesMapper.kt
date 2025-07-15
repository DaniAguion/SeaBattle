package com.example.seabattle.data.firestore.mappers

import com.example.seabattle.data.firestore.dto.UserGamesDto
import com.example.seabattle.domain.entity.UserGames

fun UserGamesDto.toEntity(): UserGames =
    UserGames(
        userId = userId,
        currentGameId = currentGameId,
        invitedToGameId = invitedToGameId.map { it.toEntity() },
        history = history.map { it.toEntity() }
    )
