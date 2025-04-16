package com.example.seabattle.di

import com.example.seabattle.domain.auth.usecase.LoginUserUseCase
import com.example.seabattle.domain.auth.usecase.LogoutUserUseCase
import com.example.seabattle.domain.auth.usecase.CheckUserAuthUseCase
import com.example.seabattle.domain.auth.usecase.GetProfileUseCase
import com.example.seabattle.domain.auth.usecase.RegisterUserUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { LoginUserUseCase(get()) }
    factory { LogoutUserUseCase(get()) }
    factory { RegisterUserUseCase(get()) }
    factory { CheckUserAuthUseCase(get()) }
    factory { GetProfileUseCase(get()) }
}