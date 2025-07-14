package com.example.seabattle.data.firestore.mappers

import com.example.seabattle.data.firestore.dto.GameHistoryDto
import com.example.seabattle.domain.entity.GameHistory
import com.example.seabattle.domain.entity.User


fun GameHistoryDto.toEntity(): GameHistory =
    GameHistory(
        gameId = gameId,
        winnerId = winnerId,
        player1 = User(
            userId = player1.userId,
            displayName = player1.displayName,
            photoUrl = player1.photoUrl,
            score = player1.score
        ),
        player2 = User(
            userId = player2.userId,
            displayName = player2.displayName,
            photoUrl = player2.photoUrl,
            score = player2.score
        ),
        scoreTransacted = scoreTransacted,
        playedAt = playedAt
    )
