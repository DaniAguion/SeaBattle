package com.example.seabattle.domain.repository

import kotlinx.coroutines.flow.Flow

interface PresenceRepository {
    suspend fun definePresence(userId: String): Result<Unit>
    fun listenUserPresence(userId: String): Flow<Result<String>>
}