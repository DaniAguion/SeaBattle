package com.example.seabattle.presentation.screens.welcome


import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.example.seabattle.presentation.resources.ValidationError
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
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current


    // Show a snack bar when an error occurs
    LaunchedEffect(key1 = welcomeUiState.error) {
        welcomeUiState.error?.let { error ->
            snackbarHostState.showSnackbar(
                message = context.getString(error.toErrorMessageUI()),
                duration = SnackbarDuration.Short
            )
            welcomeViewModel.onErrorShown()
        }
    }

    // Navigate to Home screen if user is already logged in
    LaunchedEffect(key1 = welcomeUiState.isLoggedIn) {
        if (welcomeUiState.isLoggedIn) {
            navController.navigate(Tabs.Home.title)
        }
    }


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        WelcomeScreenContent(
            modifier = modifier,
            username = welcomeUiState.username,
            email = welcomeUiState.email,
            password = welcomeUiState.password,
            repeatedPassword = welcomeUiState.repeatedPassword,
            usernameError = welcomeUiState.usernameError,
            emailError = welcomeUiState.emailError,
            passwordError = welcomeUiState.passwordError,
            repeatedPasswordError = welcomeUiState.repeatedPasswordError,
            msgResult = welcomeUiState.msgResult,
            onResetError = welcomeViewModel::onResetError,
            onUsernameUpdate = welcomeViewModel::onUsernameUpdate,
            onEmailUpdate = welcomeViewModel::onEmailUpdate,
            onPasswordUpdate = welcomeViewModel::onPasswordUpdate,
            onPasswordRepeatUpdate = welcomeViewModel::onRepeatPasswordUpdate,
            onLoginButtonClicked = welcomeViewModel::onLoginButtonClicked,
            onClickForgotPassword = welcomeViewModel::onClickForgotPassword,
            onRegisterButtonClicked = welcomeViewModel::onRegisterButtonClicked,
            onGoogleButtonClicked = { welcomeViewModel.onGoogleButtonClicked(context) },
        )
    }
}

@Composable
fun WelcomeScreenContent(
    modifier: Modifier = Modifier,
    username: String = "",
    email: String = "",
    password: String = "",
    repeatedPassword: String = "",
    usernameError: ValidationError? = null,
    emailError: ValidationError? = null,
    passwordError: ValidationError? = null,
    repeatedPasswordError: ValidationError? = null,
    msgResult: InfoMessages? = null,
    onResetError: () -> Unit = {},
    onUsernameUpdate: (String) -> Unit = {},
    onEmailUpdate: (String) -> Unit = {},
    onPasswordUpdate: (String) -> Unit = {},
    onPasswordRepeatUpdate: (String) -> Unit = {},
    onLoginButtonClicked: () -> Unit = {},
    onClickForgotPassword: () -> Unit = {},
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
                Text(stringResource(R.string.access_with_google))
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
                                registerMode = false,
                                email = email,
                                password = password,
                                emailError = emailError,
                                passwordError = passwordError,
                                msgResult = msgResult,
                                onEmailUpdate = onEmailUpdate,
                                onPasswordUpdate = onPasswordUpdate,
                                onClickForgotPassword = onClickForgotPassword
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
                                registerMode = true,
                                username = username,
                                email = email,
                                password = password,
                                repeatedPassword = repeatedPassword,
                                usernameError = usernameError,
                                emailError = emailError,
                                passwordError = passwordError,
                                repeatedPasswordError = repeatedPasswordError,
                                msgResult = msgResult,
                                onEmailUpdate = onEmailUpdate,
                                onPasswordUpdate = onPasswordUpdate,
                                onClickForgotPassword = onClickForgotPassword,
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
    registerMode: Boolean,
    username: String = "",
    email: String = "",
    password: String = "",
    repeatedPassword: String = "",
    usernameError: ValidationError? = null,
    emailError: ValidationError? = null,
    passwordError: ValidationError? = null,
    repeatedPasswordError: ValidationError? = null,
    msgResult: InfoMessages? = null,
    onEmailUpdate: (String) -> Unit,
    onPasswordUpdate: (String) -> Unit,
    onClickForgotPassword: () -> Unit,
    onUsernameUpdate: (String) -> Unit = {  }, // Optional for register only
    onPasswordRepeatUpdate: (String) -> Unit = {  }, // Optional for register only
){
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        if (registerMode) {
            OutlinedTextField(
                value = username,
                onValueChange = onUsernameUpdate,
                label = { Text(stringResource(R.string.username)) },
                singleLine = true,
                isError = usernameError != null,
                supportingText = {
                    usernameError?.let {
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
            value = email,
            onValueChange = onEmailUpdate,
            label = { Text(stringResource(R.string.email)) },
            singleLine = true,
            isError = emailError != null,
            supportingText = {
                emailError?.let {
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
            value = password,
            onValueChange = onPasswordUpdate,
            label = { Text(stringResource(R.string.password)) },
            singleLine = true,
            isError = passwordError != null,
            supportingText = {
                passwordError?.let {
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
        if (!registerMode) {
            Text(
                text = stringResource(R.string.forgot_password),
                modifier = Modifier
                    .clickable { onClickForgotPassword() }
                    .padding(
                        top = dimensionResource(R.dimen.padding_xsmall),
                        bottom = dimensionResource(R.dimen.padding_small)
                    )
            )
        } else {
            OutlinedTextField(
                value = repeatedPassword,
                onValueChange = onPasswordRepeatUpdate,
                label = { Text(stringResource(R.string.repeat_password)) },
                singleLine = true,
                isError = repeatedPasswordError != null,
                supportingText = {
                    repeatedPasswordError?.let {
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
    msgResult?.let { msgResult ->
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
            InfoMessages.PASSWORD_RESET_SENT -> {
                Text(
                    text = stringResource(R.string.password_reset_sent),
                    color = colorResource(id = R.color.success_text_color)
                )
            }
            InfoMessages.PASSWORD_RESET_FAILED -> {
                Text(
                    text = stringResource(R.string.password_reset_failed),
                    color = colorResource(id = R.color.unsuccessful_text_color)
                )
            }
            else -> {
                // No message to show, other errors should not happen here
            }
        }
    }
    Spacer(
        modifier = Modifier.height(dimensionResource(R.dimen.padding_small))
    )
}



@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    SeaBattleTheme {
        WelcomeScreenContent()
    }
}