package com.br.b2b.data.remote.dto.request

import androidx.annotation.Keep

@Keep

data class CreateUserRequest(
    val name: String,
    val email: String,
    val confirmPassword: String,
    val password: String,
    val cpf: String,
    val termsConditions: Boolean = false
)
