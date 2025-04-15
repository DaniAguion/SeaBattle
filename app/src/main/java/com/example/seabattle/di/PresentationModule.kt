package com.example.seabattle.di

import com.example.seabattle.ui.screens.battleplan.BattlePlanViewModel
import com.example.seabattle.ui.screens.home.HomeViewModel
import com.example.seabattle.ui.screens.profile.ProfileViewModel
import com.example.seabattle.ui.screens.welcome.WelcomeViewModel
import com.example.seabattle.ui.screens.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { SplashViewModel(get()) }
    viewModel { WelcomeViewModel(get(), get(), get()) }
    viewModel { ProfileViewModel(get(), get()) }
    viewModel { HomeViewModel() }
    viewModel { BattlePlanViewModel() }
}