package com.example.seabattle.di

import com.example.seabattle.ui.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { LoginViewModel(get()) }
}