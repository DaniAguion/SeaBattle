package com.example.seabattle.di

import com.example.seabattle.ui.welcome.WelcomeViewModel
import com.example.seabattle.ui.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { WelcomeViewModel(get(), get(), get(), get()) }
    viewModel { SplashViewModel(get()) }
}