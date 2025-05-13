package com.example.seabattle.domain.usecase.game

import com.example.seabattle.data.local.SecurePrefsData
import com.example.seabattle.domain.Session
import com.example.seabattle.domain.entity.Game
import com.example.seabattle.domain.entity.GameState
import com.example.seabattle.domain.entity.RoomState
import com.example.seabattle.domain.repository.GameRepository
import com.example.seabattle.domain.repository.RoomRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.UUID

class ListenGameUseCase(
    val gameRepository: GameRepository,
    val securePrefs: SecurePrefsData,
    val ioDispatcher: CoroutineDispatcher,
    val session: Session,
) {
    // This function listen the game entity and updates the local game object
    suspend operator fun invoke(gameId: String): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            //TO DO:
        }
    }
}