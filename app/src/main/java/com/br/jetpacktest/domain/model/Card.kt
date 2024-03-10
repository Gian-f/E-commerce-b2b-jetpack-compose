package com.br.jetpacktest.domain.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import java.util.Date

@Immutable
data class Card(
    val holderName: String,
    val cardNumber: String,
    val cardType: CardType,
    val cvv: String,
    val expiryDate: Date
)