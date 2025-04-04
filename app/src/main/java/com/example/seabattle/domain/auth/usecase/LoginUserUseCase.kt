package com.example.seabattle.domain.auth.usecase

import com.example.seabattle.data.repository.AuthRepositoryImpl
import com.example.seabattle.domain.auth.LoginMethod
import com.example.seabattle.domain.model.User

class LoginUserUseCase (
    private val authRepository: AuthRepositoryImpl
) {
    suspend operator fun invoke(method: LoginMethod): Boolean {
        return authRepository.loginUser(method)
    }
}