import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.br.b2b.domain.routes.Screen
import com.br.b2b.ui.viewmodel.LoginViewModel
import com.br.b2b.ui.widgets.dialogs.ShowErrorSheet
import com.br.jetpacktest.R

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel,
) {
    val context = LocalContext.current as Activity
    context.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    val login by viewModel.username.collectAsStateWithLifecycle("")
    val password by viewModel.password.collectAsStateWithLifecycle("")
    val isLoginLoading by viewModel.isLoginLoading.collectAsStateWithLifecycle()
    val loginErrorMessage by viewModel.loginError.collectAsStateWithLifecycle()
    val loginErrorState = remember { mutableStateOf(false) }
    var passwordVisibility by rememberSaveable { mutableStateOf(false) }

    val loginTouched = remember { mutableStateOf(false) }
    val passwordTouched = remember { mutableStateOf(false) }

    BackHandler(enabled = true) {
        context.finish()
    }

    Box(
        Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp, horizontal = 12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(150.dp)
                        .padding(bottom = 16.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Compra",
                        fontWeight = FontWeight.Bold,
                        fontSize = TextUnit(26F, TextUnitType.Sp),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Certa",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = TextUnit(26F, TextUnitType.Sp),
                        color = Color(0xFF0097b2)
                    )
                }
            }
            OutlinedTextFieldValidation(
                value = login,
                onValueChange = { viewModel.onLoginChange(it) },
                label = "E-mail",
                placeholder = "E-mail",
                error = if (loginTouched.value && login.isEmpty()) "O E-mail pode ser vazio!" else null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Rounded.Email,
                        contentDescription = "Email"
                    )
                },
                interactionSource = remember { MutableInteractionSource() },
                modifier = Modifier.onFocusChanged {
                    if (it.isFocused) {
                        loginTouched.value = true
                    }
                }
            )

            OutlinedTextFieldValidation(
                value = password,
                onValueChange = { viewModel.onPasswordChange(it) },
                label = "Senha",
                placeholder = "Senha",
                error = if (passwordTouched.value && password.isEmpty()) "A senha não pode ser vazia!" else null,
                visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                trailingIcon = {
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        Icon(
                            imageVector = if (passwordVisibility) {
                                ImageVector.vectorResource(id = R.drawable.ic_visibility_off)
                            } else {
                                ImageVector.vectorResource(id = R.drawable.ic_visibility_on)
                            },
                            contentDescription = if (passwordVisibility) {
                                "Esconder senha"
                            } else {
                                "Mostrar senha"
                            }
                        )
                    }
                },
                leadingIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_lock),
                        contentDescription = null
                    )
                },
                interactionSource = remember { MutableInteractionSource() },
                modifier = Modifier.onFocusChanged {
                    if (it.isFocused) {
                        passwordTouched.value = true
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                ButtonComponent("Entrar", isLoading = isLoginLoading) {
                    viewModel.authenticateUser(
                        onSuccess = {
                            viewModel.saveToken(it)
                            navController.navigate(Screen.Products.route)
                        },
                        onFailure = {
                            loginErrorState.value = true
                        },
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Não possui uma conta?")
                    TextButton(onClick = { navController.navigate(Screen.Register.route) }) {
                        Text(text = "Registre-se")
                    }
                }
            }

            if (loginErrorState.value) {
                ShowErrorSheet(
                    message = loginErrorMessage.toString(),
                    onDismiss = {
                        loginErrorState.value = false
                    }
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedTextFieldValidation(
    modifier: Modifier = Modifier.fillMaxWidth(),
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    error: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    trailingIcon: @Composable() (() -> Unit)? = null,
    leadingIcon: @Composable() (() -> Unit)? = null,
    singleLine: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    shape: Shape = MaterialTheme.shapes.small,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        disabledTextColor = Color.Transparent,
        disabledBorderColor = Color.Transparent,
        errorBorderColor = Color.Transparent,
        errorContainerColor = Color(0xFFE7E7E7),
        focusedBorderColor = Color.Transparent,
        unfocusedBorderColor = Color.Transparent,
        containerColor = Color(0xFFE7E7E7),
    ),
) {
    Column(modifier = modifier.padding(8.dp)) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = label, fontWeight = FontWeight.SemiBold) },
            isError = error != null,
            placeholder = { Text(text = placeholder) },
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            interactionSource = interactionSource,
            shape = shape,
            colors = colors,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon
        )
        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 16.dp, top = 0.dp)
            )
        }
    }
}

@Composable
fun ButtonComponent(
    value: String,
    isLoading: Boolean = false,
    onButtonClicked: () -> Unit,
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp)
            .heightIn(48.dp),
        onClick = {
            onButtonClicked.invoke()
        },
        contentPadding = PaddingValues(),
        colors = ButtonDefaults.buttonColors(Color.Transparent),
        shape = RoundedCornerShape(8.dp),
        enabled = !isLoading
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(55.dp)
                .background(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primary
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("Aguarde...", color = Color.White, fontSize = 10.sp)
                    CircularProgressIndicator(
                        color = Color.White,
                        strokeWidth = 1.dp,
                        modifier = Modifier.size(14.dp)
                    )
                }
            } else {
                Text(
                    text = value,
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}