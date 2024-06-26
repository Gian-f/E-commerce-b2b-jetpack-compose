package com.br.b2b.ui.screens

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.br.b2b.domain.routes.Screen
import com.br.b2b.ui.viewmodel.LoginViewModel
import com.br.jetpacktest.R
import kotlinx.coroutines.delay
import java.util.UUID

@Composable
fun SplashScreen(
    navController: NavController,
    loginViewModel: LoginViewModel,
) {
    val context = LocalContext.current as Activity
    val isLoginVisible = remember { mutableStateOf(false) }
    val token by loginViewModel.token.collectAsStateWithLifecycle()
    context.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    LaunchedEffect(Unit) {
        isLoginVisible.value = true
        delay(2000)
        if (token.isNullOrEmpty()) {
            navController.navigate(Screen.Login.route)
        } else {
            navController.navigate(Screen.Products.route)
        }
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Color.White) {
        AnimatedVisibility(
            visible = isLoginVisible.value,
            enter = slideInHorizontally()
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
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
            }
        }
    }
}
