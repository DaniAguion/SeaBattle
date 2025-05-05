package com.example.seabattle.di

import com.example.seabattle.domain.usecase.auth.LoginUserUseCase
import com.example.seabattle.domain.usecase.auth.LogoutUserUseCase
import com.example.seabattle.domain.usecase.auth.CheckUserAuthUseCase
import com.example.seabattle.domain.usecase.auth.GetProfileUseCase
import com.example.seabattle.domain.usecase.auth.RegisterUserUseCase
import com.example.seabattle.domain.usecase.game.CreateGameUseCase
import com.example.seabattle.domain.usecase.game.DiscoverCellUseCase
import com.example.seabattle.domain.usecase.room.CreateRoomUseCase
import com.example.seabattle.domain.usecase.room.GetRoomsUseCase
import org.koin.dsl.module

val domainModule = module {
    // Auth use cases
    factory { LoginUserUseCase(get()) }
    factory { LogoutUserUseCase(get()) }
    factory { RegisterUserUseCase(get(), get(), get(), get()) }
    factory { CheckUserAuthUseCase(get()) }
    factory { GetProfileUseCase(get()) }
    // Game use cases
    factory { DiscoverCellUseCase(get()) }
    factory { CreateGameUseCase(get(), get()) }
    factory { CreateRoomUseCase(get(), get()) }
    factory { GetRoomsUseCase(get()) }
}