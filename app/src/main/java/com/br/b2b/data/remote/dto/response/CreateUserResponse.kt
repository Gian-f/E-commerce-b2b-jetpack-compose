package com.br.b2b.data.remote.dto.response

import com.br.b2b.domain.model.User

data class CreateUserResponse(
    val message: String,
    val result: User,
    val status: Boolean,
)
