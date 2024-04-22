package com.br.b2b.data.remote.dto.response

import androidx.annotation.Keep


@Keep
data class LoginResponse(
    val message: String,
    val result: String,
    val status : Boolean
)
