package com.br.b2b.data.remote.dto.response

import androidx.annotation.Keep
import com.br.b2b.domain.model.User


@Keep
data class LoginResponse(
    val message: String,
    val result: String,
    val status : Boolean,
    val user : User
)
