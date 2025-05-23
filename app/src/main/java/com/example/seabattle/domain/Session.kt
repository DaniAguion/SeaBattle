package com.example.seabattle.domain

import com.example.seabattle.data.local.SecurePrefsData
import com.example.seabattle.domain.entity.Game
import com.example.seabattle.domain.entity.Room
import com.example.seabattle.domain.entity.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// This class is used as repository of the state.
// It is used to store the current room, game and user in cache.
class Session(private val securePrefs: SecurePrefsData) {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    private val _currentRoom = MutableStateFlow<Room?>(null)
    val currentRoom: StateFlow<Room?> = _currentRoom

    private val _currentGame = MutableStateFlow<Game?>(null)
    val currentGame: StateFlow<Game?> = _currentGame


    init{
        // Initialize the current user from secure prefs.
        _currentUser.value = securePrefs.getUserSession()
    }

    // Repository of the current user.
    // The user session is stored in secure prefs to persist.
    //
    fun setCurrentUser(user: User?) {
        _currentUser.value = user
        if (user != null) {
            securePrefs.saveUserSession(user)
        } else {
            securePrefs.clearUserSession()
        }
    }

    fun getCurrentUser(): User {
        /// If the userId is null, it means that the user is not logged in.
        /// Return an user to blank to avoid null pointer exception.
        val user = _currentUser.value
        println("Session: Get user: $user")
        if (user == null) {
            return User()
        }
        return user
    }

    fun getCurrentUserId(): String {
        return _currentUser.value?.userId ?: ""
    }

    fun clearCurrentUser() {
        _currentUser.value = null
    }


    //
    // Repository of the current room.
    //
    fun setCurrentRoom(room: Room) {
        _currentRoom.value = room
    }

    fun getCurrentRoom() : Room? {
        return _currentRoom.value
    }

    fun clearCurrentRoom() {
        _currentRoom.value = null
    }


    //
    // Repository of the current game.
    //
    fun setCurrentGame(game: Game) {
        _currentGame.value = game
    }

    fun getCurrentGame() : Game? {
        return _currentGame.value
    }

    fun clearCurrentGame() {
        _currentGame.value = null
    }
}