package com.example.seabattle.di


import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val generalModule = module {
    single {
        val context = androidContext()
    }
}