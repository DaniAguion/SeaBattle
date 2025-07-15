package com.example.seabattle.domain.repository

import com.example.seabattle.domain.entity.BasicPlayer
import com.example.seabattle.domain.entity.User

interface UserRepository {
    suspend fun createUser(user: User): Result<Unit>
    suspend fun getUser(userId: String) : Result<User>
    suspend fun deleteUser(userId: String): Result<Unit>
    suspend fun getLeaderboard(): Result<List<BasicPlayer>>
    suspend fun getUserPosition(userId: String, userScore: Int): Result<Int>
}