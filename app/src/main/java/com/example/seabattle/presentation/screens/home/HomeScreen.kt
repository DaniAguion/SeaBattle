package com.example.seabattle.presentation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.example.seabattle.presentation.SeaBattleScreen
import com.example.seabattle.presentation.TabItem

@Composable
fun HomeScreen(
    modifier: Modifier,
    navController: NavHostController,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Home Screen"
        )
        Button(
            onClick = {
                navController.navigate(SeaBattleScreen.Game.title) {
                }
            },
        ) {
            Text(text = "Start Game")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){
    HomeScreen(
        modifier = Modifier.fillMaxSize(),
        navController = NavHostController(context = androidx.compose.ui.platform.LocalContext.current)
    )
}