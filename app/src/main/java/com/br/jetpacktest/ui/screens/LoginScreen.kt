import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.br.jetpacktest.R
import com.br.jetpacktest.domain.routes.OnBackPress
import com.br.jetpacktest.domain.routes.Screen
import com.br.jetpacktest.ui.viewmodel.LoginViewModel
import com.br.jetpacktest.ui.widgets.dialogs.ShowErrorSheet

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel,
) {
    val context = LocalContext.current as Activity
    context.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    val login by viewModel.username.collectAsStateWithLifecycle("")
    val password by viewModel.password.collectAsStateWithLifecycle("")
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val loginErrorMessage by viewModel.loginError.collectAsStateWithLifecycle()
    val loginErrorState = remember { mutableStateOf(false) }
    var passwordVisibility by rememberSaveable { mutableStateOf(false) }

    val loginTouched = remember { mutableStateOf(false) }
    val passwordTouched = remember { mutableStateOf(false) }

    OnBackPress {
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
//            Image(
//                painter = painterResource(id = R.drawable.logo),
//                contentDescription = "Logo",
//                modifier = Modifier
//                    .fillMaxWidth(0.8f)
//                    .height(150.dp)
//            )
            OutlinedTextFieldValidation(
                value = login,
                onValueChange = { viewModel.onLoginChange(it) },
                label = "E-mail",
                placeholder = "Cod. transportadora",
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
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

            Spacer(modifier = Modifier.height(8.dp))

            ButtonComponent("Entrar", isLoading = isLoading) {
                viewModel.authenticateUser(
                    onSuccess = {
                        navController.navigate(Screen.Products.route)
                    },
                    onFailure = {
                        loginErrorState.value = true
                    },
                )
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
        disabledTextColor = Color.Black,
        focusedTextColor = Color.Black,
        focusedBorderColor = Color.Black,
        focusedLabelColor = Color.Black
    ),
) {

    Column(modifier = modifier.padding(8.dp)) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = label) },
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
        shape = RoundedCornerShape(15.dp),
        enabled = true
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(48.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(
                            Color(0x80030013),
                            MaterialTheme.colorScheme.onSurface
                        )
                    ),
                    shape = RoundedCornerShape(15.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 1.dp,
                    modifier = Modifier.size(14.dp)
                )
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