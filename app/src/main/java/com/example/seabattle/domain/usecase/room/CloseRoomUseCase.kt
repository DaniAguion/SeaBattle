package com.example.seabattle.domain.usecase.room

import com.example.seabattle.domain.Session
import com.example.seabattle.domain.entity.RoomState
import com.example.seabattle.domain.repository.RoomRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class CloseRoomUseCase(
    val roomRepository: RoomRepository,
    val ioDispatcher: CoroutineDispatcher,
    val session: Session,
) {
    suspend operator fun invoke(): Result<Unit> = withContext(ioDispatcher) {
        runCatching {
            val roomId = session.getCurrentRoom()?.roomId
            if (roomId != null){
                val room = roomRepository.getRoom(roomId).getOrNull()
                if (room == null) {
                    session.clearCurrentRoom()
                    return@runCatching
                }
                if(room.roomState == RoomState.WAITING_FOR_PLAYER.name || room.roomState == RoomState.GAME_STARTED.name) {
                    roomRepository.deleteRoom(roomId).getOrThrow()
                    session.clearCurrentRoom()
                }
            }
        }
    }
}