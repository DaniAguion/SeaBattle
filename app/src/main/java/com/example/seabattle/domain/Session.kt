package com.example.seabattle.domain

import com.example.seabattle.domain.entity.Game
import com.example.seabattle.domain.entity.Room
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// This class is used to save models necessary for the game session
class Session {
    private val _currentRoom = MutableStateFlow<Room?>(null)
    val currentRoom: StateFlow<Room?> = _currentRoom

    fun setCurrentRoom(room: Room) {
        _currentRoom.value = room
    }

    fun clearCurrentRoom() {
        _currentRoom.value = null
    }


    private val _currentGame = MutableStateFlow<Game?>(null)
    val currentGame: StateFlow<Game?> = _currentGame

    fun setCurrentGame(game: Game) {
        _currentGame.value = game
    }

    fun clearCurrentGame() {
        _currentGame.value = null
    }
}