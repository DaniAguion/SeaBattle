package com.example.seabattle.domain.usecase

import com.example.seabattle.data.repository.AuthRepositoryImpl

class LoginUserUseCase (
    private val authRepository: AuthRepositoryImpl
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): Boolean {
        return authRepository.loginUser(email, password)
    }
}