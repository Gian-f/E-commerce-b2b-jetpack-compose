package com.br.b2b.data.remote.service

import com.br.b2b.data.remote.dto.request.CreateUserRequest
import com.br.b2b.data.remote.dto.request.LoginRequest
import com.br.b2b.data.remote.dto.response.CreateUserResponse
import com.br.b2b.data.remote.dto.response.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("v1/login")
    fun authenticate(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("v1/user")
    fun createUser(@Body createUserRequest: CreateUserRequest): Call<CreateUserResponse>


//    @GET("/products")
//    fun fetchAllProducts() : Call<ProductResponse>
}