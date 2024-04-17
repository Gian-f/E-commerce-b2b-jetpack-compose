package com.br.jetpacktest.data.remote.service

import retrofit2.Call
import com.br.jetpacktest.data.remote.dto.request.LoginRequest
import com.br.jetpacktest.data.remote.dto.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    @POST("v1/login")
    fun authenticate(@Body loginRequest: LoginRequest): Call<LoginResponse>


//    @GET("/products")
//    fun fetchAllProducts() : Call<ProductResponse>
}