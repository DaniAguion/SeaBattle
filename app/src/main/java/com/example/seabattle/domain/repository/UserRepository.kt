package com.example.seabattle.domain.repository

import com.example.seabattle.domain.entity.User

interface UserRepository {
    suspend fun createUser(user: User): Result<Unit>
    suspend fun getUser(userId: String): User?
}