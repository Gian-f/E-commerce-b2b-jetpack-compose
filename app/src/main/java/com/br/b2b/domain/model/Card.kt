package com.br.b2b.domain.model

import androidx.compose.runtime.Immutable
import java.util.Date

@Immutable
data class Card(
    val holderName: String,
    val cardNumber: String,
    val cardType: CardType,
    val cvv: String,
    val expiryDate: Date
)