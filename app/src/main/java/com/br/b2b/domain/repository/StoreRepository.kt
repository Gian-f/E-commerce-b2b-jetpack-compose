package com.br.b2b.domain.repository

import com.br.b2b.domain.model.Category
import com.br.b2b.domain.model.Product

interface StoreRepository {
    suspend fun fetchAllProducts(): Result<List<Product>>
    suspend fun fetchAllRecommendedProducts(): Result<List<Product>>
    suspend fun fetchAllCategories(): Result<List<Category>>
    suspend fun findProductById(id: Int): Result<Product?>
    suspend fun findProducts(term: String): Result<List<Product?>>
    suspend fun createProduct(product: Product): Result<Unit>
    suspend fun createProducts(products: List<Product>): Result<Unit>
    suspend fun createCategory(category: Category): Result<Unit>
    suspend fun createCategories(categories: List<Category>): Result<Unit>
    suspend fun getProductsInCategory(categoryId: Int): Result<List<Product>>
    suspend fun toggleFavoriteStatus(productId: Int): Result<Unit>
    suspend fun deleteAllProducts(): Result<Unit>
    suspend fun deleteAllCategories(): Result<Unit>
}