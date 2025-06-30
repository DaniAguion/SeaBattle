package com.example.seabattle.domain.repository

import com.example.seabattle.domain.entity.User

interface UserRepository {
    suspend fun createUser(user: User): Result<Unit>
    suspend fun getUser(userId: String) : Result<User>
    suspend fun getLeaderboard(): Result<List<User>>
    suspend fun getUserPosition(userId: String, userScore: Int): Result<Int>
}