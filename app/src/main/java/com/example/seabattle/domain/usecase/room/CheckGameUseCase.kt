package com.example.seabattle.domain.usecase.room

import com.example.seabattle.domain.Session
import com.example.seabattle.domain.entity.RoomState



class CheckGameUseCase(
    val session: Session,
) {
    operator fun invoke(): Boolean  {
        val room = session.getCurrentRoom()
        val game = session.getCurrentGame()

        return (room?.roomState == RoomState.GAME_CREATED.name || game != null)
    }
}