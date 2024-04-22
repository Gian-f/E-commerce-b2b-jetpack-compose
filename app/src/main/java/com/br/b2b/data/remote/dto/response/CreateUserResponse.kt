package com.br.b2b.data.remote.dto.response

import androidx.annotation.Keep
import com.br.b2b.domain.model.User
import com.google.gson.annotations.SerializedName
@Keep
data class CreateUserResponse(
    val message: String,
    val result: String,
    val status: Boolean,
)
