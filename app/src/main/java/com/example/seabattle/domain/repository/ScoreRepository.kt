package com.example.seabattle.domain.repository

interface ScoreRepository {
    suspend fun updateScore(gameId: String) : Result<Unit>
}