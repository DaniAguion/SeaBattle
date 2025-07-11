package com.example.seabattle.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.seabattle.ConnectivityObserver
import com.example.seabattle.domain.usecase.presence.ListenPresenceUseCase
import com.example.seabattle.domain.usecase.presence.SetPresenceUseCase
import com.example.seabattle.domain.usecase.user.ListenUserUseCase
import com.example.seabattle.presentation.screens.MyAppNavigation
import com.example.seabattle.presentation.theme.SeaBattleTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import timber.log.Timber


class MainActivity : ComponentActivity() {
    private val soundManager: SoundManager by inject()
    private val setPresenceUseCase: SetPresenceUseCase by inject()
    private val listenPresenceUseCase: ListenPresenceUseCase by inject()
    private val listenUserUseCase: ListenUserUseCase by inject()
    private val connectivityObserver: ConnectivityObserver by inject()


    private val activityScope: CoroutineScope = MainScope()
    private var isUserAuthenticated = false
    private var listeningPresence: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SeaBattleTheme {
                MyAppNavigation()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        startPresenceMonitoring()
    }

    override fun onStop() {
        super.onStop()
        listeningPresence?.cancel()
        listeningPresence = null
    }


    private fun startPresenceMonitoring() {
        // Start listening for user authentication changes and set presence when authenticated
        activityScope.launch {
            listenUserUseCase.invoke().collect { user ->
                val wasAuthenticated = isUserAuthenticated
                isUserAuthenticated = (user != null)

                if (isUserAuthenticated && !wasAuthenticated) {
                    setPresenceUseCase.invoke()

                    listeningPresence = activityScope.launch {
                        listenPresenceUseCase.invoke().collect { presenceResult ->
                            presenceResult
                                .onSuccess { status ->
                                    Timber.d("Status changed on client to: $status")
                                }
                                .onFailure { error ->
                                    Timber.e(error, "Failed to get current presence status.")
                                }
                        }
                    }
                } else if (!isUserAuthenticated) {
                    listeningPresence?.cancel()
                }
            }
        }


        // Observe connectivity changes and set presence accordingly is user is authenticated
        activityScope.launch {
            connectivityObserver.observe().collect { status ->
                if (status == ConnectivityObserver.Status.Available) {
                    if (isUserAuthenticated) {
                        setPresenceUseCase.invoke()
                    }
                }
            }
        }
    }


    // When the activity is destroyed, release the sound manager resources
    override fun onDestroy() {
        super.onDestroy()
        soundManager.release()
    }
}

