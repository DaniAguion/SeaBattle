package com.example.seabattle.di

import com.example.seabattle.domain.Session
import com.example.seabattle.domain.usecase.auth.LoginUserUseCase
import com.example.seabattle.domain.usecase.auth.LogoutUserUseCase
import com.example.seabattle.domain.usecase.auth.CheckUserAuthUseCase
import com.example.seabattle.domain.usecase.auth.RegisterUserUseCase
import com.example.seabattle.domain.usecase.game.DiscoverCellUseCase
import com.example.seabattle.domain.usecase.game.LeaveGameUseCase
import com.example.seabattle.domain.usecase.game.ListenGameUseCase
import com.example.seabattle.domain.usecase.game.UserReadyUseCase
import com.example.seabattle.domain.usecase.room.CloseRoomUseCase
import com.example.seabattle.domain.usecase.room.CreateRoomUseCase
import com.example.seabattle.domain.usecase.room.GetRoomsUseCase
import com.example.seabattle.domain.usecase.room.JoinRoomUseCase
import com.example.seabattle.domain.usecase.room.ListenRoomUseCase
import com.example.seabattle.domain.usecase.room.WaitRoomUseCase
import org.koin.dsl.module

val domainModule = module {
    single { Session(get())}
    // Auth Use cases
    factory { LoginUserUseCase(get(), get(), get(), get())}
    factory { LogoutUserUseCase(get(), get())}
    factory { RegisterUserUseCase(get(), get(), get(), get())}
    factory { CheckUserAuthUseCase(get())}
    // Room Use cases
    factory { GetRoomsUseCase(get()) }
    factory { CreateRoomUseCase(get(), get(), get(), get())}
    factory { ListenRoomUseCase(get(), get(), get())}
    factory { JoinRoomUseCase(get(), get(), get(), get())}
    factory { WaitRoomUseCase(get(), get(), get(), get(), get())}
    factory { CloseRoomUseCase(get(), get(), get())}
    // Game Use cases
    factory { ListenGameUseCase(get(), get(), get())}
    factory { UserReadyUseCase(get(), get(), get())}
    factory { LeaveGameUseCase(get(), get(), get())}
    factory { DiscoverCellUseCase(get())}
}