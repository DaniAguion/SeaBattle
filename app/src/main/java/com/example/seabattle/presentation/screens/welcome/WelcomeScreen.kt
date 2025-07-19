package com.example.seabattle.presentation.screens.welcome

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.seabattle.R
import com.example.seabattle.presentation.resources.InfoMessages
import com.example.seabattle.presentation.resources.toErrorMessageUI
import com.example.seabattle.presentation.screens.Tabs
import com.example.seabattle.presentation.theme.SeaBattleTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    welcomeViewModel: WelcomeViewModel = koinViewModel()
) {
    val welcomeUiState by welcomeViewModel.uiState.collectAsState()
    val context = LocalContext.current


    // Show a toast message when an error occurs
    LaunchedEffect(key1 = welcomeUiState.error) {
        welcomeUiState.error?.let { error ->
            Toast.makeText(context, context.getString(error.toErrorMessageUI()), Toast.LENGTH_LONG).show()
            welcomeViewModel.onErrorShown()
        }
    }

    // Navigate to Home screen if user is already logged in
    LaunchedEffect(key1 = welcomeUiState.isLoggedIn) {
        if (welcomeUiState.isLoggedIn) {
            navController.navigate(Tabs.Home.title)
        }
    }

    WelcomeScreenContent(
        modifier = modifier,
        welcomeUiState = welcomeUiState,
        onResetError = welcomeViewModel::onResetError,
        onUsernameUpdate = welcomeViewModel::onUsernameUpdate,
        onEmailUpdate = welcomeViewModel::onEmailUpdate,
        onPasswordUpdate = welcomeViewModel::onPasswordUpdate,
        onPasswordRepeatUpdate = welcomeViewModel::onRepeatPasswordUpdate,
        onLoginButtonClicked = welcomeViewModel::onLoginButtonClicked,
        onRegisterButtonClicked = welcomeViewModel::onRegisterButtonClicked,
        onGoogleButtonClicked = { welcomeViewModel.onGoogleButtonClicked(context) }
    )
}

@Composable
fun WelcomeScreenContent(
    modifier: Modifier = Modifier,
    welcomeUiState: WelcomeUiState = WelcomeUiState(),
    onResetError: () -> Unit = {},
    onUsernameUpdate: (String) -> Unit = {},
    onEmailUpdate: (String) -> Unit = {},
    onPasswordUpdate: (String) -> Unit = {},
    onPasswordRepeatUpdate: (String) -> Unit = {},
    onLoginButtonClicked: () -> Unit = {},
    onRegisterButtonClicked: () -> Unit = {},
    onGoogleButtonClicked: () -> Unit = {},
) {
    val tabs = listOf(
        stringResource(R.string.sign_in),
        stringResource(R.string.sign_up)
    )
    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { 2 })

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(dimensionResource(R.dimen.padding_container)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Header
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillParentMaxHeight(0.3f)
            ){
                Text(
                    text = stringResource(R.string.welcome_page_title),
                    fontSize = 32.sp,
                    style = MaterialTheme.typography.displayLarge
                )
                Text(
                    text = stringResource(R.string.welcome_page_desc),
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .padding(
                            top = dimensionResource(R.dimen.padding_xsmall),
                            bottom = dimensionResource(R.dimen.padding_small)
                        )

                )
            }
        }


        // Google button
        item {
            Button(
                onClick = onGoogleButtonClicked,
                modifier = Modifier
                    .padding(horizontal = dimensionResource(R.dimen.padding_container))
                    .padding(bottom = dimensionResource(R.dimen.padding_small))
            ) {
                Text(stringResource(R.string.access_with_google),)
            }
        }


        //Forms to login and register
        item {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillParentMaxHeight(0.1f)
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(text = title) },
                        selected = pagerState.currentPage == index,
                        onClick = {
                            onResetError()
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        }
                    )
                }
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
            ) { page ->
                when (page) {
                    0 -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(dimensionResource(R.dimen.padding_small))
                        ) {
                            CommonForm(
                                registerFields = false,
                                welcomeUiState = welcomeUiState,
                                onEmailUpdate = onEmailUpdate,
                                onPasswordUpdate = onPasswordUpdate
                            )
                            Button(
                                onClick = onLoginButtonClicked,
                            ) {
                                Text(stringResource(R.string.submit))
                            }
                        }
                    }

                    1 -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(dimensionResource(R.dimen.padding_small))
                        ) {
                            CommonForm(
                                registerFields = true,
                                welcomeUiState = welcomeUiState,
                                onEmailUpdate = onEmailUpdate,
                                onPasswordUpdate = onPasswordUpdate,
                                onUsernameUpdate = onUsernameUpdate,
                                onPasswordRepeatUpdate = onPasswordRepeatUpdate
                            )
                            Button(
                                onClick = onRegisterButtonClicked,

                            ) {
                                Text(stringResource(R.string.submit))
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun CommonForm(
    registerFields: Boolean,
    welcomeUiState : WelcomeUiState,
    onEmailUpdate: (String) -> Unit,
    onPasswordUpdate: (String) -> Unit,
    onUsernameUpdate: (String) -> Unit = {  }, // Optional for login only
    onPasswordRepeatUpdate: (String) -> Unit = {  } // Optional for login only
){
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        if (registerFields) {
            OutlinedTextField(
                value = welcomeUiState.username,
                onValueChange = onUsernameUpdate,
                label = { Text(stringResource(R.string.username)) },
                singleLine = true,
                isError = welcomeUiState.usernameError != null,
                supportingText = {
                    welcomeUiState.usernameError?.let {
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
                )
            )
        }
        OutlinedTextField(
            value = welcomeUiState.email,
            onValueChange = onEmailUpdate,
            label = { Text(stringResource(R.string.email)) },
            singleLine = true,
            isError = welcomeUiState.emailError != null,
            supportingText = {
                welcomeUiState.emailError?.let {
                    Text(
                        text = stringResource(it.idString),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            )
        )
        OutlinedTextField(
            value = welcomeUiState.password,
            onValueChange = onPasswordUpdate,
            label = { Text(stringResource(R.string.password)) },
            singleLine = true,
            isError = welcomeUiState.passwordError != null,
            supportingText = {
                welcomeUiState.passwordError?.let {
                    Text(
                        text = stringResource(it.idString),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            )
        )
        if (registerFields) {
            OutlinedTextField(
                value = welcomeUiState.repeatedPassword,
                onValueChange = onPasswordRepeatUpdate,
                label = { Text(stringResource(R.string.repeat_password)) },
                singleLine = true,
                isError = welcomeUiState.repeatedPasswordError != null,
                supportingText = {
                    welcomeUiState.repeatedPasswordError?.let {
                        Text(
                            text = stringResource(it.idString),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    autoCorrectEnabled = false,
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                )
            )
        }
    }
    Spacer(
        modifier = Modifier.height(dimensionResource(R.dimen.padding_xsmall))
    )
    welcomeUiState.msgResult?.let { msgResult ->
        when (msgResult) {
            InfoMessages.LOGIN_SUCCESSFUL -> {
                Text(
                    text = stringResource(R.string.login_successful),
                    color = colorResource(id = R.color.success_text_color)
                )
            }
            InfoMessages.LOGIN_UNSUCCESSFUL -> {
                Text(
                    text = stringResource(R.string.login_unsuccessful),
                    color = colorResource(id = R.color.unsuccessful_text_color)
                )
            }
            InfoMessages.REGISTER_SUCCESSFUL -> {
                Text(
                    text = stringResource(R.string.register_successful),
                    color = colorResource(id = R.color.success_text_color)
                )
            }
            InfoMessages.REGISTER_UNSUCCESSFUL -> {
                Text(
                    text = stringResource(R.string.register_unsuccessful),
                    color = colorResource(id = R.color.unsuccessful_text_color)
                )
            }
            InfoMessages.PASSWORDS_DOES_NOT_MATCH -> {
                Text(
                    text = stringResource(R.string.passwords_does_not_match),
                    color = colorResource(id = R.color.unsuccessful_text_color)
                )
            }
            InfoMessages.INVALID_FIELDS -> {
                Text(
                    text = stringResource(R.string.invalid_fields),
                    color = colorResource(id = R.color.unsuccessful_text_color)
                )
            }
            else -> {
                // No message to show, other errors should not happen here
            }
        }
    }
}


@Preview(showBackground = true, device = "id:small_phone")
@Composable
fun WelcomeScreenPreview() {
    SeaBattleTheme {
        WelcomeScreenContent()
    }
}