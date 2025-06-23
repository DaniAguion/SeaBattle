package com.example.seabattle.di

import com.example.seabattle.domain.SessionService
import com.example.seabattle.domain.usecase.auth.GetAuthUserUseCase
import com.example.seabattle.domain.usecase.auth.LoginUserUseCase
import com.example.seabattle.domain.usecase.auth.LogoutUserUseCase
import com.example.seabattle.domain.usecase.auth.ListenUserUseCase
import com.example.seabattle.domain.usecase.auth.RegisterUserUseCase
import com.example.seabattle.domain.usecase.game.EnableClaimUseCase
import com.example.seabattle.domain.usecase.game.ClaimVictoryUseCase
import com.example.seabattle.domain.usecase.game.MakeMoveUseCase
import com.example.seabattle.domain.usecase.game.LeaveGameUseCase
import com.example.seabattle.domain.usecase.game.ListenGameUseCase
import com.example.seabattle.domain.usecase.game.UserReadyUseCase
import com.example.seabattle.domain.usecase.game.CreateGameUseCase
import com.example.seabattle.domain.usecase.game.EnableReadyUseCase
import com.example.seabattle.domain.usecase.game.GetGamesUseCase
import com.example.seabattle.domain.usecase.game.JoinGameUseCase
import com.example.seabattle.domain.usecase.presence.SetPresenceUseCase
import org.koin.dsl.module

val domainModule = module {
    single { SessionService()}
    // Auth Use cases
    factory { LoginUserUseCase(get(), get(), get(), get())}
    factory { LogoutUserUseCase(get(), get())}
    factory { RegisterUserUseCase(get(), get(), get(), get())}
    factory { ListenUserUseCase(get()) }
    factory { GetAuthUserUseCase(get(), get(), get()) }
    // Game Use cases
    factory { GetGamesUseCase(get(), get()) }
    factory { CreateGameUseCase(get(), get(), get(), get(), get())}
    factory { JoinGameUseCase(get(), get(), get(), get())}
    factory { ListenGameUseCase(get())}
    factory { LeaveGameUseCase(get(), get(), get())}
    factory { MakeMoveUseCase(get(), get(), get())}
    factory { EnableReadyUseCase() }
    factory { UserReadyUseCase(get(), get(), get())}
    factory { SetPresenceUseCase(get(), get(), get()) }
    factory { EnableClaimUseCase() }
    factory { ClaimVictoryUseCase(get(), get(), get()) }
}