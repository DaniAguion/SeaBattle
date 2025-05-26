package com.example.seabattle.di

import com.example.seabattle.domain.services.BoardManager
import com.example.seabattle.domain.repository.AuthRepository
import com.example.seabattle.data.firebase.AuthRepositoryImpl
import com.example.seabattle.data.firestore.repository.UserRepositoryImpl
import com.example.seabattle.data.firestore.repository.GameRepositoryImpl
import com.example.seabattle.data.firestore.repository.PreGameRepositoryImpl
import com.example.seabattle.domain.repository.UserRepository
import com.example.seabattle.domain.repository.GameRepository
import com.example.seabattle.domain.repository.PreGameRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val dataModule = module {
    single<CoroutineDispatcher>{ Dispatchers.IO }
    // Firebase
    single { FirebaseAuth.getInstance() }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    // Firestore
    single { FirebaseFirestore.getInstance() }
    single<UserRepository> { UserRepositoryImpl(get(), get()) }
    single<GameRepository> { GameRepositoryImpl(get(), get()) }
    single<PreGameRepository> { PreGameRepositoryImpl(get(), get()) }
    // Managers
    single { BoardManager() }
}
