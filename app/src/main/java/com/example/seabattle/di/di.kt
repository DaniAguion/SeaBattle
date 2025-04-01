package com.example.seabattle.di

import com.example.seabattle.firebase.auth.AuthViewModel
import com.example.seabattle.ui.login.LoginViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { FirebaseAuth.getInstance() }
    viewModel { AuthViewModel(get()) }
    viewModel { LoginViewModel(get()) }
}