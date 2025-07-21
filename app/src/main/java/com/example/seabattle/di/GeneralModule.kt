package com.example.seabattle.di


import com.example.seabattle.ConnectivityObserver
import com.example.seabattle.NetworkConnectivityObserver
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val generalModule = module {
    single { val context = androidContext() }
    single<ConnectivityObserver> { NetworkConnectivityObserver(androidApplication()) }
}