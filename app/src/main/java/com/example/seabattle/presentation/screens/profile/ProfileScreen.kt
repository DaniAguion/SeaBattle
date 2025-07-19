package com.example.seabattle.presentation.screens.profile

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.seabattle.R
import com.example.seabattle.data.local.sampleUser
import com.example.seabattle.domain.entity.GameHistory
import com.example.seabattle.domain.entity.User
import com.example.seabattle.presentation.resources.toErrorMessageUI
import com.example.seabattle.presentation.screens.Screen
import com.example.seabattle.presentation.theme.SeaBattleTheme
import org.koin.androidx.compose.koinViewModel
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.seabattle.presentation.resources.InfoMessages
import com.example.seabattle.presentation.resources.ValidationError


@Composable
fun ProfileScreen(
    modifier: Modifier,
    navController: NavHostController,
    profileViewModel: ProfileViewModel = koinViewModel(),
) {
    val profileUiState by profileViewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Stop listeners when the screen is disposed
    DisposableEffect(Unit) {
        profileViewModel.startListening()
        profileViewModel.loadUserProfile()
        onDispose {
            profileViewModel.stopListening()
        }
    }


    // Show a toast message when an error occurs
    LaunchedEffect(key1 = profileUiState.error) {
        profileUiState.error?.let { error ->
            Toast.makeText(context, context.getString(error.toErrorMessageUI()), Toast.LENGTH_LONG).show()
            profileViewModel.onErrorShown()
        }
    }


    LaunchedEffect(key1 = profileUiState.userLoggedIn) {
        if (!profileUiState.userLoggedIn) {
            navController.navigate(Screen.Welcome.title)
        }
    }


    // Show a dialog when the user wants to delete a note permanently
    if (profileUiState.deleteDialog) {
        AlertDialog(
            onDismissRequest = { profileViewModel.onClickCancel() },
            title = { Text(stringResource(R.string.delete_account_title)) },
            text = {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_xsmall)),
                    modifier = Modifier.fillMaxWidth()
                ){
                    Text(stringResource(R.string.delete_account_title_msg))
                    OutlinedTextField(
                        value = profileUiState.deleteConfirmationText,
                        onValueChange = { profileViewModel.onDeleteConfirmTextUpdated(it) },
                        label = { Text(stringResource(R.string.delete_confirmation_text)) },
                        singleLine = true,
                        isError = profileUiState.deleteConfirmationText != "Delete",
                        supportingText = {
                            if (profileUiState.deleteConfirmationText != "Delete") {
                                Text(
                                    text = stringResource(R.string.delete_confirmation_error),
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    )
                }
            },
            confirmButton = {
                Button(
                    enabled = profileUiState.deleteConfirmationText == "Delete",
                    onClick = {
                        profileViewModel.onClickConfirm()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ),
                ) {
                    Text(stringResource(R.string.delete_button))
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        profileViewModel.onClickCancel()
                    },
                ) {
                    Text(stringResource(R.string.cancel_button))
                }
            }
        )
    }


    ProfileScreenContent(
        modifier = modifier,
        user = profileUiState.user,
        userNameField = profileUiState.userNameField,
        userNameError = profileUiState.userNameError,
        historyList = profileUiState.user.history.reversed(),
        errorList = profileUiState.errorList,
        loadingList = profileUiState.loadingList,
        msgResult = profileUiState.msgResult,
        onLogoutButtonClicked = profileViewModel::onLogoutButtonClicked,
        onDeleteAccountClicked = profileViewModel::onDeleteAccountClicked,
        onUserNameUpdate = profileViewModel::onUsernameUpdate,
        onChangeUsernameClicked = profileViewModel::onChangeUsernameClicked,
    )
}

@Composable
fun ProfileScreenContent(
    modifier: Modifier = Modifier,
    user: User,
    userNameField: String,
    userNameError: ValidationError? = null,
    historyList: List<GameHistory> = emptyList(),
    errorList: Boolean,
    loadingList: Boolean,
    msgResult: InfoMessages? = null,
    onLogoutButtonClicked: () -> Unit = {},
    onDeleteAccountClicked: () -> Unit = {},
    onUserNameUpdate: (String) -> Unit = {},
    onChangeUsernameClicked: () -> Unit = {},
) {
    var expandedOptions by remember { mutableStateOf(false ) }

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .padding(dimensionResource(R.dimen.padding_container))
            .fillMaxSize(),

    ) {
        // Profile Info
        item {
            if (user.photoUrl.isEmpty()) {
                Image(
                    painter = painterResource(id = R.drawable.account_box_40px),
                    contentDescription = "User photo",
                    modifier = Modifier
                        .padding(top= dimensionResource(R.dimen.padding_small))
                        .size(dimensionResource(R.dimen.profile_image_size))
                        .clip(CircleShape)
                )
            } else {
                AsyncImage(
                    model = user.photoUrl,
                    contentDescription = "User photo",
                    contentScale = ContentScale.Crop,
                    error = painterResource(id = R.drawable.account_box_40px),
                    modifier = Modifier
                        .padding(top= dimensionResource(R.dimen.padding_small))
                        .size(dimensionResource(R.dimen.profile_image_size))
                        .clip(CircleShape)
                )
            }
        }
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_xsmall)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(dimensionResource(R.dimen.padding_small)),
            ){
                Text(
                    text = user.displayName,
                    style = MaterialTheme.typography.headlineMedium,
                )
                Text(
                    text = user.email,
                    style = MaterialTheme.typography.titleMedium,
                )
            }

        }
        item {
            Button(
                onClick = onLogoutButtonClicked,
                modifier = Modifier
                    .padding(bottom = dimensionResource(R.dimen.padding_small)),
            ) {
                Text("SignOut")
            }
        }

        // Account Options Section
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expandedOptions = !expandedOptions },
            ) {
                Text(
                    text = stringResource(R.string.account_options),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
                Icon(
                    imageVector = if (expandedOptions) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                    contentDescription = if (expandedOptions) "Collapse" else "Expand"
                )
            }
            AnimatedVisibility(
                visible = expandedOptions,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ){
                    OutlinedTextField(
                        value = userNameField,
                        onValueChange = onUserNameUpdate,
                        label = { Text(stringResource(R.string.username_label)) },
                        singleLine = true,
                        isError = userNameError != null,
                        supportingText = {
                            userNameError?.let {
                                Text(
                                    text = stringResource(it.idString),
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            autoCorrectEnabled = false,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = dimensionResource(R.dimen.padding_small),
                                bottom = dimensionResource(R.dimen.padding_xsmall)
                            )
                    )
                    Button(
                        onClick = onChangeUsernameClicked,
                        modifier = Modifier
                            .padding(bottom = dimensionResource(R.dimen.padding_xsmall))
                    ) {
                        Text(stringResource(R.string.change_username_button))
                    }
                    msgResult?.let { msgResult ->
                        when (msgResult) {
                            InfoMessages.CHANGE_SUCCESSFUL -> {
                                Text(
                                    text = stringResource(R.string.change_successful),
                                    color = colorResource(id = R.color.success_text_color)
                                )
                            }

                            InfoMessages.CHANGE_UNSUCCESSFUL -> {
                                Text(
                                    text = stringResource(R.string.change_unsuccessful),
                                    color = colorResource(id = R.color.unsuccessful_text_color)
                                )
                            }

                            InfoMessages.INVALID_USERNAME -> {
                                Text(
                                    text = stringResource(R.string.invalid_username),
                                    color = colorResource(id = R.color.unsuccessful_text_color)
                                )
                            }

                            InfoMessages.INVALID_PASSWORD -> {
                                Text(
                                    text = stringResource(R.string.invalid_password),
                                    color = colorResource(id = R.color.unsuccessful_text_color)
                                )
                            }

                            InfoMessages.PASSWORDS_DOES_NOT_MATCH -> {
                                Text(
                                    text = stringResource(R.string.passwords_does_not_match),
                                    color = colorResource(id = R.color.unsuccessful_text_color)
                                )
                            }

                            else -> {
                                // No message to show, other errors should not happen here
                            }
                        }
                    }
                    Button(
                        onClick = onDeleteAccountClicked,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        ),
                        modifier = Modifier
                            .padding(
                                top = dimensionResource(R.dimen.padding_small),
                                bottom = dimensionResource(R.dimen.padding_small)
                            )
                    ) {
                        Text(stringResource(R.string.delete_account_button))
                    }
                }
            }
        }


        // Played Games Title
        item{
            Text(
                text = stringResource(R.string.history_header),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = dimensionResource(R.dimen.padding_large),
                        bottom = dimensionResource(R.dimen.padding_small),
                    )
            )
        }



        // Played Games List
        when {
            loadingList ->
                item {
                    CircularProgressIndicator(
                        modifier = Modifier.size(dimensionResource(R.dimen.progress_size)),
                        strokeWidth = 2.dp
                    )
                }

            !errorList -> {
                items(items = historyList, key = { it.gameId }) { playedGame ->
                    PlayedGameCard(
                        userId = user.userId,
                        game = playedGame,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = dimensionResource(R.dimen.padding_xsmall))
                    )
                }
            }

            else -> {
                item {
                    Text(
                        text = stringResource(R.string.error_get_history),
                        modifier = Modifier.padding(dimensionResource(R.dimen.padding_xsmall)),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview(){
    SeaBattleTheme {
        ProfileScreenContent(
            modifier = Modifier.fillMaxSize(),
            user = sampleUser,
            userNameField = sampleUser.displayName,
            userNameError = null,
            historyList = sampleUser.history,
            errorList = false,
            loadingList = false,
            msgResult = null
        )
    }
}