package com.br.b2b.domain.repository

import com.br.b2b.data.remote.dto.request.CreateUserRequest
import com.br.b2b.data.remote.dto.request.LoginRequest
import com.br.b2b.data.remote.dto.response.CreateUserResponse
import com.br.b2b.data.remote.dto.response.LoginResponse

interface AuthRepository {
    fun authenticate(loginRequest: LoginRequest) : Result<LoginResponse>

    fun createUser(createUserRequest: CreateUserRequest): Result<CreateUserResponse>
}