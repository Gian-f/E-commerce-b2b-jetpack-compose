package com.br.jetpacktest.domain.repository

import com.br.jetpacktest.data.remote.dto.request.LoginRequest
import com.br.jetpacktest.data.remote.dto.response.LoginResponse

interface AuthRepository {
    fun authenticate(loginRequest: LoginRequest) : Result<LoginResponse>
}