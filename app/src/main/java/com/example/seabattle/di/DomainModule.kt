package com.example.seabattle.di

import com.example.seabattle.domain.Session
import com.example.seabattle.domain.usecase.auth.LoginUserUseCase
import com.example.seabattle.domain.usecase.auth.LogoutUserUseCase
import com.example.seabattle.domain.usecase.auth.CheckUserAuthUseCase
import com.example.seabattle.domain.usecase.auth.GetProfileUseCase
import com.example.seabattle.domain.usecase.auth.RegisterUserUseCase
import com.example.seabattle.domain.usecase.game.DiscoverCellUseCase
import com.example.seabattle.domain.usecase.room.CreateRoomUseCase
import com.example.seabattle.domain.usecase.room.GetRoomsUseCase
import com.example.seabattle.domain.usecase.room.JoinRoomUseCase
import com.example.seabattle.domain.usecase.room.WaitRoomUseCase
import org.koin.dsl.module

val domainModule = module {
    single { Session() }
    // Auth use cases
    factory { LoginUserUseCase(get(), get(), get(), get()) }
    factory { LogoutUserUseCase(get(), get()) }
    factory { RegisterUserUseCase(get(), get(), get(), get()) }
    factory { CheckUserAuthUseCase(get()) }
    factory { GetProfileUseCase(get()) }
    // Game use cases
    factory { DiscoverCellUseCase(get()) }
    factory { GetRoomsUseCase(get()) }
    factory { CreateRoomUseCase(get(), get(), get(), get(), get()) }
    factory { JoinRoomUseCase(get(), get(), get(), get(), get()) }
    factory { WaitRoomUseCase(get(), get(), get(), get(), get()) }
}