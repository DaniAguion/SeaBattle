package com.example.seabattle.data.firestore.mappers

import com.example.seabattle.data.firestore.dto.GameHistoryDto
import com.example.seabattle.domain.entity.GameHistory


fun GameHistory.toDto(): GameHistoryDto =
    GameHistoryDto(
        gameId = gameId,
        winnerId = winnerId,
        player1 = player1.toUserDto(),
        player2 = player2.toUserDto(),
        scoreTransacted = scoreTransacted,
        playedAt = playedAt
    )


fun GameHistoryDto.toEntity(): GameHistory =
    GameHistory(
        gameId = gameId,
        winnerId = winnerId,
        player1 = player1.toUserEntity(),
        player2 = player2.toUserEntity(),
        scoreTransacted = scoreTransacted,
        playedAt = playedAt
    )
