package com.br.b2b.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class User(
    val name: String,
    val password: String,
    val termsCondition: String,
)
