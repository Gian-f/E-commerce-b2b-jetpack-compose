package com.br.b2b.domain.repository

import com.br.b2b.data.remote.dto.response.CategoryResponse
import com.br.b2b.data.remote.dto.response.ProductResponse
import com.br.b2b.domain.model.Category
import com.br.b2b.domain.model.Product

interface StoreRepository {
    suspend fun fetchAllProducts(): Result<ProductResponse?>
    suspend fun fetchAllCategories(): Result<CategoryResponse?>
    suspend fun findProductById(id: Int): Result<Product?>
    suspend fun createProduct(product: Product?): Result<Unit>
    suspend fun createProducts(products: List<Product>): Result<Unit>
    suspend fun createCategory(category: Category?): Result<Unit>
    suspend fun createCategories(categories: List<Category>): Result<Unit>
}