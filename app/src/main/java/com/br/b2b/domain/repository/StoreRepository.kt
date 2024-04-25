package com.br.b2b.domain.repository

import com.br.b2b.data.remote.dto.response.CategoryResponse
import com.br.b2b.data.remote.dto.response.ProductResponse

interface StoreRepository {
    suspend fun fetchAllProducts(): Result<ProductResponse?>
    suspend fun fetchAllCategories(): Result<CategoryResponse?>
}