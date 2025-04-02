package com.example.seabattle

import android.app.Application
import com.example.seabattle.di.appModule
import com.example.seabattle.di.dataModule
import com.example.seabattle.di.storageModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class SeaBattleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@SeaBattleApplication)
            modules(appModule, dataModule, storageModule)
        }
    }
}