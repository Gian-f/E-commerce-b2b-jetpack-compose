package com.br.jetpacktest.domain.repository

import com.br.jetpacktest.data.remote.dto.request.LoginRequest
import com.br.jetpacktest.data.remote.dto.response.LoginResponse
import com.br.jetpacktest.data.remote.service.ApiService
import javax.inject.Inject
import java.io.IOException

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
                Result.failure(IOException("Unsuccessful response: $errorMessage"))
            }
        } catch (e: IOException) {
            Result.failure(e)
        }
    }
}
