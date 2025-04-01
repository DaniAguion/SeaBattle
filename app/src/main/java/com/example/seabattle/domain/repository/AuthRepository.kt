package com.example.seabattle.domain.repository

import com.example.seabattle.firebase.auth.AuthState
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun loginUser(email: String, password: String): Flow<AuthState>
    fun registerUser(email: String, password: String): Flow<AuthState>
    fun signOut(): Flow<AuthState>
    fun checkAuthStatus(): Flow<AuthState>
}