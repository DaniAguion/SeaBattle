package com.example.seabattle

import android.app.Application
import com.example.seabattle.di.appModule
import com.example.seabattle.domain.usecase.presence.ListenPresenceUseCase
import com.example.seabattle.domain.usecase.presence.SetPresenceUseCase
import com.example.seabattle.domain.usecase.user.ListenUserUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin
import timber.log.Timber

class SeaBattleApplication : Application() {

    // Needed variables for dependency injection and presence monitoring
    private lateinit var setPresenceUseCase: SetPresenceUseCase
    private lateinit var listenPresenceUseCase: ListenPresenceUseCase
    private lateinit var connectivityObserver: ConnectivityObserver
    private lateinit var listenUserUseCase: ListenUserUseCase
    private val applicationScope = CoroutineScope(SupervisorJob())
    private var isUserAuthenticated = false
    private var userListeningJob: Job? = null
    private var listenPresence: Job? = null

    override fun onCreate() {
        super.onCreate()

        // Enable Debug logging with Timber if the build is a debug build.
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        // Start Koin for dependency injection
        startKoin {
            androidContext(this@SeaBattleApplication)
            modules(appModule)
        }


        // Initialize use cases and call the function to start monitoring presence
        setPresenceUseCase = getKoin().get()
        listenPresenceUseCase = getKoin().get()
        connectivityObserver = getKoin().get()
        listenUserUseCase = getKoin().get()
        startPresenceMonitoring()
    }


    private fun startPresenceMonitoring() {
        // Start listening for user authentication changes and set presence when authenticated
        applicationScope.launch {
            userListeningJob?.cancel()
            userListeningJob = applicationScope.launch {
                listenUserUseCase.invoke().collect { user ->
                    isUserAuthenticated = (user != null)
                    if (isUserAuthenticated) {
                        setPresenceUseCase.invoke()
                        listenPresence?.cancel()
                        listenPresence = applicationScope.launch {
                            listenPresenceUseCase.invoke()
                        }
                    }
                }
            }
        }


        // Observe connectivity changes and set presence accordingly is user is authenticated
        applicationScope.launch {
            connectivityObserver.observe().collect { status ->
                if (status != ConnectivityObserver.Status.Available) {
                    if (isUserAuthenticated) {
                        setPresenceUseCase.invoke()
                    }
                }
            }
        }
    }
}