package com.example.seabattle.domain.usecase.auth

import com.example.seabattle.domain.repository.AuthRepository

class CheckUserAuthUseCase ( private val authRepository: AuthRepository ) {
    operator fun invoke(): Boolean {
        return authRepository.isLoggedIn()
    }
}