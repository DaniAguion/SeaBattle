package com.example.seabattle.di

import com.example.seabattle.domain.game.BoardManager
import com.example.seabattle.data.GameManager
import com.example.seabattle.domain.auth.AuthRepository
import com.example.seabattle.data.repository.AuthRepositoryImpl
import com.example.seabattle.data.firestore.FirestoreRepositoryImpl
import com.example.seabattle.data.repository.GameRepositoryImpl
import com.example.seabattle.data.repository.RoomRepositoryImpl
import com.example.seabattle.domain.auth.SessionManager
import com.example.seabattle.domain.firestore.FirestoreRepository
import com.example.seabattle.domain.game.GameRepository
import com.example.seabattle.domain.room.RoomRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val dataModule = module {
    single<CoroutineDispatcher>{ Dispatchers.IO }
    // Firebase
    single { FirebaseAuth.getInstance() }
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    // Firestore
    single { FirebaseFirestore.getInstance() }
    single<FirestoreRepository> { FirestoreRepositoryImpl(get(), get()) }
    // Others repositories
    single<GameRepository> { GameRepositoryImpl(get()) }
    single<RoomRepository> { RoomRepositoryImpl(get(), get()) }
    // Managers
    single { SessionManager(get(), get(), get()) }
    single { BoardManager() }
    single { GameManager(get(), get(), get()) }
}
