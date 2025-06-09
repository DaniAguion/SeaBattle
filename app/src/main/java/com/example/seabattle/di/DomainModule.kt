package com.example.seabattle.di

import com.example.seabattle.domain.Session
import com.example.seabattle.domain.usecase.auth.LoginUserUseCase
import com.example.seabattle.domain.usecase.auth.LogoutUserUseCase
import com.example.seabattle.domain.usecase.auth.CheckUserAuthUseCase
import com.example.seabattle.domain.usecase.auth.RegisterUserUseCase
import com.example.seabattle.domain.usecase.game.MakeMoveUseCase
import com.example.seabattle.domain.usecase.game.LeaveGameUseCase
import com.example.seabattle.domain.usecase.game.ListenGameUseCase
import com.example.seabattle.domain.usecase.game.UserReadyUseCase
import com.example.seabattle.domain.usecase.game.CreateGameUseCase
import com.example.seabattle.domain.usecase.game.GetGamesUseCase
import com.example.seabattle.domain.usecase.game.JoinGameUseCase
import org.koin.dsl.module

val domainModule = module {
    single { Session(get())}
    // Auth Use cases
    factory { LoginUserUseCase(get(), get(), get(), get())}
    factory { LogoutUserUseCase(get(), get())}
    factory { RegisterUserUseCase(get(), get(), get(), get())}
    factory { CheckUserAuthUseCase(get())}
    // Game Use cases
    factory { GetGamesUseCase(get(), get()) }
    factory { CreateGameUseCase(get(), get(), get(), get(), get())}
    factory { JoinGameUseCase(get(), get(), get(), get())}
    factory { ListenGameUseCase(get(), get(), get())}
    factory { UserReadyUseCase(get(), get(), get())}
    factory { LeaveGameUseCase(get(), get(), get())}
    factory { MakeMoveUseCase(get(), get(), get())}
}