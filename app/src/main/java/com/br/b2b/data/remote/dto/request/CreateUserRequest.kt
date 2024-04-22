package com.br.b2b.data.remote.dto.request

import androidx.annotation.Keep

@Keep

data class CreateUserRequest(
    val name: String,
    val password: String,
    val termsCondition: Boolean = false
)
