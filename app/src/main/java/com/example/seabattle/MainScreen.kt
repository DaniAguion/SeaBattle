package com.example.seabattle

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.seabattle.ui.HomeScreen
import com.example.seabattle.ui.login.LoginScreen
import com.example.seabattle.ui.theme.SeaBattleTheme

enum class SeaBattleScreen(val title: String) {
    Home(title = "Home"),
    Login(title = "Login"),
    Register(title = "Register"),
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
            startDestination = SeaBattleScreen.Home.title,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = SeaBattleScreen.Home.title) {
                HomeScreen(
                    onLoginButtonClicked = {
                        navController.navigate(SeaBattleScreen.Login.title)
                    },
                    onRegisterButtonClicked = {
                        navController.navigate(SeaBattleScreen.Register.title)
                    }
                )
            }
            composable(route = SeaBattleScreen.Login.title) {
                LoginScreen()
            }
            composable(route = SeaBattleScreen.Register.title) {
                //RegisterScreen()
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