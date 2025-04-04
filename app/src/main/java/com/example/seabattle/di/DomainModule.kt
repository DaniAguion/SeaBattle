package com.example.seabattle.di

import com.example.seabattle.domain.usecase.LoginUserUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { LoginUserUseCase(get()) }
}