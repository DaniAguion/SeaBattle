package com.example.seabattle.di

import com.example.seabattle.domain.game.usecases.CellActionUseCase
import com.example.seabattle.domain.auth.usecases.LoginUserUseCase
import com.example.seabattle.domain.auth.usecases.LogoutUserUseCase
import com.example.seabattle.domain.auth.usecases.CheckUserAuthUseCase
import com.example.seabattle.domain.auth.usecases.GetProfileUseCase
import com.example.seabattle.domain.auth.usecases.RegisterUserUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { LoginUserUseCase(get()) }
    factory { LogoutUserUseCase(get()) }
    factory { RegisterUserUseCase(get()) }
    factory { CheckUserAuthUseCase(get()) }
    factory { GetProfileUseCase(get()) }
    factory { CellActionUseCase(get()) }
}