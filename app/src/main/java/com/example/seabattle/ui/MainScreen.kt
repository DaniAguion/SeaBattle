package com.example.seabattle.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.seabattle.ui.home.HomeScreen
import com.example.seabattle.ui.welcome.WelcomeScreen
import com.example.seabattle.ui.theme.SeaBattleTheme
import com.example.seabattle.ui.splash.SplashScreen


enum class SeaBattleScreen(val title: String) {
    Splash(title = "Splash"),
    Welcome(title = "Welcome"),
    Home(title = "Home"),
    Play(title = "Play"),
    Profile(title = "Profile"),
    BattlePlan(title = "Battle Plan"),
    Gameboard(title = "Gameboard")
}


@Composable
fun SeaBattleApp(modifier : Modifier = Modifier) {
    val navController = rememberNavController()

    Scaffold(modifier = Modifier.fillMaxSize()) {
        innerPadding ->

        NavHost(
            navController = navController,
            startDestination = SeaBattleScreen.Splash.title,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = SeaBattleScreen.Splash.title) {
                SplashScreen(navController = navController)
            }
            composable(route = SeaBattleScreen.Welcome.title) {
                WelcomeScreen()
            }
            composable(route = SeaBattleScreen.Home.title) {
                HomeScreen()
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SeaBattleTheme {
        SeaBattleApp()
    }
}