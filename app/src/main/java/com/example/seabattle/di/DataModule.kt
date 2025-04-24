package com.example.seabattle.di

import com.example.seabattle.data.BoardManager
import com.example.seabattle.data.GameManager
import com.example.seabattle.domain.auth.repository.AuthRepository
import com.example.seabattle.data.repository.AuthRepositoryImpl
import com.example.seabattle.data.repository.FirestoreRepositoryImpl
import com.example.seabattle.data.repository.GameRepositoryImpl
import com.example.seabattle.data.SessionManager
import com.example.seabattle.domain.firestore.repository.FirestoreRepository
import com.example.seabattle.domain.game.repository.GameRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.dsl.module

val dataModule = module {
    // Repositories
    single { FirebaseAuth.getInstance() }
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single { FirebaseFirestore.getInstance() }
    single<FirestoreRepository> { FirestoreRepositoryImpl(get()) }
    single<GameRepository> { GameRepositoryImpl(get()) }
    // Managers
    single { SessionManager(get(), get(), get()) }
    single { BoardManager() }
    single { GameManager(get(), get(), get()) }
}
