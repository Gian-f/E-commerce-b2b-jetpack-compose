package com.br.b2b.domain.repository

import com.br.b2b.data.remote.dto.request.CreateUserRequest
import com.br.b2b.data.remote.dto.request.LoginRequest
import com.br.b2b.data.remote.dto.response.CreateUserResponse
import com.br.b2b.data.remote.dto.response.LoginResponse
import com.br.b2b.data.remote.service.ApiService
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val service: ApiService
) : AuthRepository {
    override fun authenticate(loginRequest: LoginRequest): Result<LoginResponse> {
        return try {
            val response = service.authenticate(loginRequest).execute()
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    Result.success(responseBody)
                } else {
                    Result.failure(NullPointerException("Response body is null"))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = errorBody ?: "Error body is null"
                Result.failure(Exception("Unsuccessful response: $errorMessage"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun createUser(createUserRequest: CreateUserRequest): Result<CreateUserResponse> {
        return try {
            val response = service.createUser(createUserRequest).execute()
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody != null) {
                    Result.success(responseBody)
                } else {
                    Result.failure(NullPointerException("Response body is null"))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                val errorMessage = errorBody ?: "Error body is null"
                Result.failure(Exception("Unsuccessful response: $errorMessage"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
