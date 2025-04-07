package com.example.seabattle.di

import com.example.seabattle.ui.HomeViewModel
import com.example.seabattle.ui.login.LoginViewModel
import com.example.seabattle.ui.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { LoginViewModel(get(), get()) }
    viewModel { RegisterViewModel(get()) }
    viewModel { HomeViewModel(get())}
}