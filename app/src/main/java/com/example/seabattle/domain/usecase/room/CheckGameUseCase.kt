package com.example.seabattle.domain.usecase.room

import com.example.seabattle.domain.Session
import com.example.seabattle.domain.entity.GameState
import com.example.seabattle.domain.entity.Room
import com.example.seabattle.domain.entity.RoomState
import com.example.seabattle.domain.entity.toBasic
import com.example.seabattle.domain.errors.DomainError
import com.example.seabattle.domain.errors.RoomError
import com.example.seabattle.domain.errors.UserError
import com.example.seabattle.domain.repository.RoomRepository
import com.example.seabattle.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber


class CheckGameUseCase(
    val session: Session,
) {
    operator fun invoke(): Boolean  {
        val room = session.getCurrentRoom()
        val game = session.getCurrentGame()

        return (room?.roomState == RoomState.GAME_CREATED.name || game != null)
    }
}