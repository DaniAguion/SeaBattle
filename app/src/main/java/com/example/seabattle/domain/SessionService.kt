package com.example.seabattle.domain

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// This class is used as repository of the state.
// It is used to store the current game and authenticated user in cache.
class SessionService() {
    private val _currentUserId = MutableStateFlow<String?>(null)
    val currentUserId: StateFlow<String?> = _currentUserId

    private var currentGameId: String? = null

    //
    // Repository of the current user.
    //
    fun setCurrentUserId(userId: String?) {
        _currentUserId.value = userId
    }

    fun getCurrentUserId(): String {
        return _currentUserId.value ?: ""
    }

    fun clearCurrentUserId() {
        _currentUserId.value = null
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