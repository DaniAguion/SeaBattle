package com.example.seabattle.ui.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier
) {
    // TO DO
    Column(
        modifier = modifier
    ) {
        Text(
            text = "Profile Screen",
            modifier = modifier
        )
    }
}


@Preview
@Composable
fun ProfilePreview(){
    ProfileScreen(
        modifier = Modifier
    )
}