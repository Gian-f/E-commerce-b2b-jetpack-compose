package com.br.b2b.data.remote.dto.request

import androidx.annotation.Keep

@Keep
data class LoginRequest(
    val email: String,
    val password: String,
)
