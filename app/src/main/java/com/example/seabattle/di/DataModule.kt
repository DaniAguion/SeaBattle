package com.example.seabattle.di

import com.example.seabattle.domain.auth.repository.AuthRepository
import com.example.seabattle.data.repository.AuthRepositoryImpl
import com.example.seabattle.data.repository.FirestoreRepositoryImpl
import com.example.seabattle.data.session.SessionManager
import com.example.seabattle.domain.firestore.repository.FirestoreRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.dsl.module

val dataModule = module {
    // FirebaseAuth instance
    single { FirebaseAuth.getInstance() }
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    // Firestore instance
    single { FirebaseFirestore.getInstance() }
    single<FirestoreRepository> { FirestoreRepositoryImpl(get()) }
    // SessionManager instance
    single { SessionManager(get(), get(), get()) }
}
