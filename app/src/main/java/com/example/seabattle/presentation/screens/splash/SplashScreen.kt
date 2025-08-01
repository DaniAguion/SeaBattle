package com.example.seabattle.presentation.screens.splash

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.example.seabattle.R
import com.example.seabattle.presentation.screens.Screen
import com.example.seabattle.presentation.theme.SeaBattleTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun SplashScreen(
    navController: NavHostController,
    splashViewModel: SplashViewModel = koinViewModel(),
) {
    val uiState = splashViewModel.uiState.collectAsState()
    val context = LocalContext.current


    LaunchedEffect(uiState.value) {
        when (val state = uiState.value) {
            is SplashState.Success -> {
                if (state.isLogged) {
                    navController.navigate(Screen.Home.title) {
                        popUpTo(Screen.Splash.title) { inclusive = true }
                    }
                } else {
                    navController.navigate(Screen.Welcome.title) {
                        popUpTo(Screen.Splash.title) { inclusive = true }
                    }
                }
            }
            is SplashState.Error -> {
               Toast.makeText(
                    context,
                    context.getString(R.string.error_splash_screen),
                    Toast.LENGTH_LONG
                ).show()
                navController.navigate(Screen.Welcome.title) {
                    popUpTo(Screen.Splash.title) { inclusive = true }
                }
            }
            SplashState.Loading -> {
                // There is no need to do anything here
                // the splash screen will show until the state changes
            }
        }
    }
    SplashScreenContent(
        modifier = Modifier.fillMaxSize()
    )
}


@Composable
fun SplashScreenContent(
    modifier : Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = "Application Logo"
        )
    }
}



@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SeaBattleTheme {
        SplashScreenContent(
            modifier = Modifier.fillMaxSize()
        )
    }
}