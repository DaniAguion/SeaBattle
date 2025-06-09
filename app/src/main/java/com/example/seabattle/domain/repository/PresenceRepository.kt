package com.example.seabattle.domain.repository

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.flow.Flow

interface PresenceRepository {
    suspend fun definePresence(userId: String): Task<Void?>
    fun listenUserPresence(userId: String): Flow<Result<Boolean>>
}