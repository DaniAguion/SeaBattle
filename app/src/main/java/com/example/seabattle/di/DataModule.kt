package com.example.seabattle.di

import com.example.seabattle.data.local.GameBoardRepositoryImpl
import com.example.seabattle.domain.repository.AuthRepository
import com.example.seabattle.data.firebase.AuthRepositoryImpl
import com.example.seabattle.data.firestore.repository.UserRepositoryImpl
import com.example.seabattle.data.firestore.repository.GameRepositoryImpl
import com.example.seabattle.data.firestore.repository.UserGamesRepositoryImpl
import com.example.seabattle.data.functions.BackendRepositoryImpl
import com.example.seabattle.data.realtimedb.PresenceRepoImpl
import com.example.seabattle.data.sound.SoundManagerImpl
import com.example.seabattle.domain.repository.GameBoardRepository
import com.example.seabattle.domain.repository.UserRepository
import com.example.seabattle.domain.repository.GameRepository
import com.example.seabattle.domain.repository.PresenceRepository
import com.example.seabattle.domain.repository.BackendRepository
import com.example.seabattle.domain.repository.SoundManagerRepo
import com.example.seabattle.domain.repository.UserGamesRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val dataModule = module {
    single<CoroutineDispatcher>{ Dispatchers.IO }

    // Firebase Authentication
    single { FirebaseAuth.getInstance() }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }

    // Firebase Realtime Database
    single { FirebaseDatabase.getInstance() }

    // Firebase Firestore
    single { FirebaseFirestore.getInstance() }
    single<UserRepository> { UserRepositoryImpl(get(), get()) }
    single<GameRepository> { GameRepositoryImpl(get(), get()) }
    single<UserGamesRepository> { UserGamesRepositoryImpl(get(), get()) }
    single<PresenceRepository> { PresenceRepoImpl(get(), get()) }
    single<GameBoardRepository> { GameBoardRepositoryImpl() }

    // Firebase Cloud Functions
    single<BackendRepository> { BackendRepositoryImpl(get()) }

    // Sound Manager
    single<SoundManagerRepo> { SoundManagerImpl(get()) }
}
