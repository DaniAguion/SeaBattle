package com.example.seabattle.di

import com.example.seabattle.domain.auth.repository.AuthRepository
import com.example.seabattle.data.repository.AuthRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import org.koin.dsl.module

val dataModule = module {
    single { FirebaseAuth.getInstance() }
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single { AuthRepositoryImpl(get()) }
}