package com.example.seabattle.di

import com.example.seabattle.firebase.auth.AuthViewModel
import com.example.seabattle.ui.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { AuthViewModel(get()) }
    viewModel { LoginViewModel(get()) }
}