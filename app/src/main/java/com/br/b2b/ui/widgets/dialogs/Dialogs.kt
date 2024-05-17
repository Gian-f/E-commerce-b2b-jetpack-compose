package com.br.b2b.ui.widgets.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.br.jetpacktest.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowErrorSheet(
    message: String,
    onDismiss: () -> Unit = {},
) {
    val state = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        state.expand()
    }
    if (state.isVisible) {
        ModalBottomSheet(
            sheetState = state,
            onDismissRequest = {
                scope.launch { state.hide() }
                onDismiss.invoke()
            },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = message,
                        modifier = Modifier.fillMaxWidth(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        onClick = {
                            scope.launch { state.hide() }
                            onDismiss.invoke()
                        }) {
                        Text(text = "OK")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        )
    }
}


@Composable
fun ConfirmDialog(
    dialogState: MutableState<Boolean> = mutableStateOf(false),
    onConfirm: () -> Unit = {},
    onCancel: () -> Unit = {},
    title: String = "Atenção!",
    message: String,
    confirmButtonText: String = "Confirmar",
    cancelButtonText: String = "Cancelar",
) {
    if (dialogState.value) {
        AlertDialog(
            onDismissRequest = {
                dialogState.value = false
            },
            title = {
                Text(text = title)
            },
            text = {
                Text(
                    text = message,
                    fontSize = TextUnit(16f, TextUnitType.Sp)
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    dialogState.value = false
                    onConfirm()
                }) {
                    Text(confirmButtonText)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    dialogState.value = false
                    onCancel()
                }) {
                    Text(cancelButtonText)
                }
            }
        )
    }
}


@Composable
fun LoadingDialog(
    message: String = "Carregando. Por favor, aguarde...",
) {
    AlertDialog(
        onDismissRequest = {},
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        properties = DialogProperties(usePlatformDefaultWidth = false),
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                LoadingAnimation()
                Text(
                    textAlign = TextAlign.Center,
                    text = message,
                    fontSize = TextUnit(22f, TextUnitType.Sp),
                    lineHeight = TextUnit(30f, TextUnitType.Sp),
                )
            }
        },
        confirmButton = {},
    )
}

@Composable
fun LoadingAnimation() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition = composition,
            modifier = Modifier.size(300.dp),
            iterations = LottieConstants.IterateForever
        )
    }
}
