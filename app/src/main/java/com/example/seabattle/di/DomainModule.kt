package com.example.seabattle.di

import com.example.seabattle.domain.SessionService
import com.example.seabattle.domain.usecase.leaderboard.GetLeaderboardUseCase
import com.example.seabattle.domain.usecase.user.GetAuthUserUseCase
import com.example.seabattle.domain.usecase.user.LoginUserUseCase
import com.example.seabattle.domain.usecase.user.LogoutUserUseCase
import com.example.seabattle.domain.usecase.user.ListenUserUseCase
import com.example.seabattle.domain.usecase.user.RegisterUserUseCase
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
import com.example.seabattle.domain.usecase.game.FinishGameUseCase
import com.example.seabattle.domain.usecase.presence.ListenPresenceUseCase
import com.example.seabattle.domain.usecase.presence.SetPresenceUseCase
import com.example.seabattle.domain.usecase.leaderboard.GetUserPositionUseCase
import com.example.seabattle.domain.usecase.user.DeleteUserUseCase
import com.example.seabattle.domain.usecase.userGames.GetHistoryUseCase
import com.example.seabattle.domain.usecase.user.GetUserProfileUseCase
import com.example.seabattle.domain.usecase.userGames.ListenUserGamesUseCase
import org.koin.dsl.module

val domainModule = module {
    single { SessionService()}
    // User Use cases
    factory { LoginUserUseCase(get(), get(), get(), get(), get()) }
    factory { LogoutUserUseCase(get(), get(), get())}
    factory { RegisterUserUseCase(get(), get(), get(), get(), get()) }
    factory { DeleteUserUseCase(get(), get(), get(), get(), get(), get()) }
    factory { ListenUserUseCase(get()) }
    factory { GetAuthUserUseCase(get(), get(), get()) }
    factory { GetUserProfileUseCase(get(), get(), get()) }

    // User Games Use cases
    factory { GetHistoryUseCase(get(), get(), get()) }
    factory { ListenUserGamesUseCase(get(), get(), get()) }

    // Leaderboard Use cases
    factory { GetLeaderboardUseCase(get(), get()) }
    factory { GetUserPositionUseCase(get(), get(), get()) }

    // Game Use cases
    factory { GetGamesUseCase(get(), get()) }
    factory { CreateGameUseCase(get(), get(), get(), get(), get(), get())}
    factory { JoinGameUseCase(get(), get(), get(), get(), get())}
    factory { ListenGameUseCase(get())}
    factory { LeaveGameUseCase(get(), get(), get(), get()) }
    factory { MakeMoveUseCase(get(), get(), get())}
    factory { EnableReadyUseCase() }
    factory { UserReadyUseCase(get(), get(), get())}
    factory { SetPresenceUseCase(get(), get(), get()) }
    factory { ListenPresenceUseCase(get(), get()) }
    factory { EnableClaimUseCase() }
    factory { ClaimVictoryUseCase(get(), get(), get()) }
    factory { FinishGameUseCase(get(), get(), get(), get()) }
}