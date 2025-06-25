package com.example.seabattle.domain

import com.example.seabattle.domain.entity.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// This class is used as repository of the state.
// It is used to store the current game and authenticated user in cache.
class SessionService() {
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser

    private var currentGameId: String? = null

    //
    // Repository of the current user.
    //
    fun setCurrentUser(user: User?) {
        _currentUser.value = user
    }

    fun getCurrentUserId(): String {
        return _currentUser.value?.userId ?: ""
    }

    fun clearCurrentUser() {
        _currentUser.value = null
    }


    //
    // Repository of the current gameId.
    //
    fun setCurrentGameId(gameId: String) {
        currentGameId = gameId
    }

    fun getCurrentGameId(): String {
        return currentGameId ?: ""
    }

    fun clearCurrentGame() {
        currentGameId = null
    }
}