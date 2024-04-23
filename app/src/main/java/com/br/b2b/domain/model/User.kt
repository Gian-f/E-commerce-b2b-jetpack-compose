package com.br.b2b.domain.model

import androidx.compose.runtime.Immutable
import java.util.Date

@Immutable
data class User(
    val id: String,
    val email: String,
    val name: String,
    val status: Boolean,
    val cpf: String,
    val termsConditions: String,
    val createdAt: String,
    val updatedAt: String,
    val enabled: Boolean,
    val password: String,
)
