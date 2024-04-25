package com.br.b2b.data.remote.service

import androidx.annotation.Keep
import com.br.b2b.data.remote.dto.request.CreateUserRequest
import com.br.b2b.data.remote.dto.request.LoginRequest
import com.br.b2b.data.remote.dto.response.CategoryResponse
import com.br.b2b.data.remote.dto.response.CreateUserResponse
import com.br.b2b.data.remote.dto.response.LoginResponse
import com.br.b2b.data.remote.dto.response.ProductResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

@Keep
interface ApiService {

    @POST("v1/login")
    suspend fun authenticate(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @POST("v1/user/register")
    suspend fun createUser(@Body createUserRequest: CreateUserRequest): Response<CreateUserResponse>

    @GET("v1/store/products")
    suspend fun fetchAllProducts(): Response<ProductResponse>

    @GET("v1/store/categories")
    suspend fun fetchAllCategories(): Response<CategoryResponse>
}