package com.example.seabattle.data.firestore.mappers

import com.example.seabattle.data.firestore.dto.BasicPlayerDto
import com.example.seabattle.data.firestore.dto.GameHistoryDto
import com.example.seabattle.domain.entity.BasicPlayer
import com.example.seabattle.domain.entity.GameHistory



fun GameHistoryDto.toEntity(): GameHistory =
    GameHistory(
        gameId = gameId,
        winnerId = winnerId,
        player1 = player1.toEntity(),
        player2 = player2.toEntity(),
        scoreTransacted = scoreTransacted,
        playedAt = playedAt
    )


fun GameHistory.toDto(): GameHistoryDto =
    GameHistoryDto(
        gameId = gameId,
        winnerId = winnerId,
        player1 = player1.toDto(),
        player2 = player2.toDto(),
        scoreTransacted = scoreTransacted,
        playedAt = playedAt
    )
