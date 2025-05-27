package com.example.seabattle

import android.app.Application
import com.example.seabattle.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class SeaBattleApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Enable Debug logging with Timber if the build is a debug build.
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Timber.d("Timber DebugTree plantado para la build de depuración.")
            }

        startKoin {
            androidContext(this@SeaBattleApplication)
            modules(appModule)
        }
    }
}