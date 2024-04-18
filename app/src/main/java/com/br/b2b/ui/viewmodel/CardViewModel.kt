package com.br.b2b.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue

class CardViewModel  : ViewModel() {
    // Declare as propriedades de estado aqui
    var cardNumber by mutableStateOf(TextFieldValue())
    var cardHolderName by mutableStateOf(TextFieldValue())
    var expiryDate by mutableStateOf(TextFieldValue())
    var cardCVV by mutableStateOf(TextFieldValue())
}