package com.br.b2b.ui.screens

import ButtonComponent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.br.b2b.domain.routes.Screen
import com.br.b2b.ui.viewmodel.SignUpViewModel
import com.br.b2b.ui.widgets.dialogs.ShowErrorSheet
import com.br.b2b.ui.widgets.forms.CPFTextFieldComponent
import com.br.b2b.ui.widgets.forms.CheckboxComponent
import com.br.b2b.ui.widgets.forms.ClickableLoginTextComponent
import com.br.b2b.ui.widgets.forms.DividerTextComponent
import com.br.b2b.ui.widgets.forms.HeadingTextComponent
import com.br.b2b.ui.widgets.forms.MyTextFieldComponent
import com.br.b2b.ui.widgets.forms.NormalTextComponent
import com.br.b2b.ui.widgets.forms.PasswordTextFieldComponent
import com.br.jetpacktest.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    signUpViewModel: SignUpViewModel,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val nameState = signUpViewModel.name.collectAsStateWithLifecycle()
    val emailState = signUpViewModel.email.collectAsStateWithLifecycle()
    val cpfState = signUpViewModel.cpf.collectAsStateWithLifecycle()
    val errorState = signUpViewModel.errorMessage.collectAsStateWithLifecycle()

    val passwordState = signUpViewModel.password.collectAsStateWithLifecycle()
    val isLoadingState = signUpViewModel.isLoading.collectAsStateWithLifecycle()
    val isErrorState = remember { mutableStateOf(false) }

    BackHandler {
        navController.navigate(Screen.Login.route)
        scope.launch {
            signUpViewModel.clearForm()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            scope.launch {
                signUpViewModel.clearForm()
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screen.Login.route) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                            contentDescription = "Voltar"
                        )
                    }
                },
                title = {

                }
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 22.dp),
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            NormalTextComponent(value = stringResource(id = R.string.hello))
            HeadingTextComponent(value = stringResource(id = R.string.create_account))
            Spacer(modifier = Modifier.height(20.dp))

            MyTextFieldComponent(
                fieldName = "Nome completo",
                labelValue = stringResource(id = R.string.first_name),
                painterResource = painterResource(id = R.drawable.profile),
                textValue = nameState,
                onTextChanged = {
                    scope.launch {
                        signUpViewModel.updateName(it)
                    }
                },
                errorStatus = nameState.value.isNotBlank()
            )

            MyTextFieldComponent(
                fieldName = "E-mail",
                labelValue = stringResource(id = R.string.email),
                painterResource = painterResource(id = R.drawable.ic_email),
                textValue = emailState,
                onTextChanged = {
                    scope.launch {
                        signUpViewModel.updateEmail(it)
                    }
                },
                errorStatus = signUpViewModel.isEmailValid(emailState.value)
            )

            CPFTextFieldComponent(
                fieldName = "CPF",
                labelValue = stringResource(id = R.string.cpf),
                painterResource = painterResource(id = R.drawable.ic_person_search),
                onTextChanged = {
                    scope.launch {
                        signUpViewModel.updateCPF(it)
                    }
                },
                errorStatus = cpfState.value.filter { it.isDigit() }.length == 11
            )

            PasswordTextFieldComponent(
                labelValue = stringResource(id = R.string.password),
                painterResource = painterResource(id = R.drawable.ic_lock),
                passwordValue = passwordState,
                onTextSelected = {
                    scope.launch {
                        signUpViewModel.updatePassword(it)
                    }
                },
                errorStatus = passwordState.value.isNotEmpty()
            )

            CheckboxComponent(
                value = stringResource(id = R.string.terms_and_conditions),
                onTextSelected = {
                    // Router.navigateTo(Screen.TermsAndConditionsScreen)
                },
                onCheckedChange = {
                    scope.launch {
                        signUpViewModel.updateTermsCondition(it)
                    }
                }
            )

            Spacer(modifier = Modifier.height(30.dp))

            ButtonComponent(
                value = stringResource(id = R.string.register),
                isLoading = isLoadingState.value,
                onButtonClicked = {
                    signUpViewModel.createUser(
                        onSuccess = {
                            navController.navigate(Screen.Login.route)
                            Toast.makeText(
                                context,
                                "Usuário criado com sucesso!",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        onFailure = {
                            scope.launch {
                                isErrorState.value = true
                            }
                        }
                    )
                }
            )

            Spacer(modifier = Modifier.height(15.dp))

            DividerTextComponent()

            Spacer(modifier = Modifier.height(15.dp))

            ClickableLoginTextComponent(tryingToLogin = true) {
                navController.navigate(Screen.Login.route)
            }
        }
    }

    if (isErrorState.value) {
        ShowErrorSheet(
            message = errorState.value.toString(),
            onDismiss = {
                scope.launch {
                    isErrorState.value = false
                }
            }
        )
    }
}