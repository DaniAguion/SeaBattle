package com.example.seabattle.di

import com.example.seabattle.presentation.screens.battleplan.BattlePlanViewModel
import com.example.seabattle.presentation.screens.game.GameViewModel
import com.example.seabattle.presentation.screens.home.HomeViewModel
import com.example.seabattle.presentation.screens.profile.ProfileViewModel
import com.example.seabattle.presentation.screens.room.RoomViewModel
import com.example.seabattle.presentation.screens.welcome.WelcomeViewModel
import com.example.seabattle.presentation.screens.splash.SplashViewModel
import com.example.seabattle.presentation.screens.welcome.GoogleSignIn
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    single { GoogleSignIn() }
    viewModel { SplashViewModel(get()) }
    viewModel { WelcomeViewModel(get(), get(), get()) }
    viewModel { ProfileViewModel(get(), get(), get()) }
    viewModel { GameViewModel(get(), get(), get(), get(), get()) }
    viewModel { RoomViewModel(get(), get(), get(), get(), get()) }
    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { BattlePlanViewModel() }
}