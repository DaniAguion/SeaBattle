package com.example.seabattle.domain.repository

interface BackendRepository {
    suspend fun finishGame(gameId: String) : Result<Unit>
}