package com.example.seabattle.di

import com.example.seabattle.ui.welcome.WelcomeViewModel
import com.example.seabattle.ui.login.LoginViewModel
import com.example.seabattle.ui.register.RegisterViewModel
import com.example.seabattle.ui.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { LoginViewModel(get(), get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { WelcomeViewModel()}
    viewModel { SplashViewModel(get()) }
}