package com.br.b2b.domain.repository

import com.br.b2b.data.remote.dto.response.CategoryResponse
import com.br.b2b.data.remote.dto.response.ProductResponse
import com.br.b2b.data.remote.service.ApiService
import javax.inject.Inject

class StoreRepositoryImpl
@Inject constructor(
    private val service: ApiService
) : StoreRepository {

    override suspend fun fetchAllProducts(): Result<ProductResponse?> {
        return try {
            val response = service.fetchAllProducts()
            if (response.isSuccessful) {
                Result.success(response.body())
            } else {
                Result.failure(NullPointerException("Erro ao trazer produtos"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Erro ao trazer produtos: ${e.message}"))
        }
    }

    override suspend fun fetchAllCategories(): Result<CategoryResponse?> {
        return try {
            val response = service.fetchAllCategories()
            if(response.isSuccessful) {
                Result.success(response.body())
            } else {
                Result.failure(Exception("Erro ao trazer categorias: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(Exception("Erro ao trazer categorias: ${e.message}"))
        }
    }
}