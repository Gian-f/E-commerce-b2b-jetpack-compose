package com.br.b2b.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.br.b2b.domain.model.Product

@Dao
interface ProductDao {
    @Query("SELECT * FROM products")
    suspend fun getAllProducts(): List<Product>

    @Query("SELECT * FROM products ORDER BY price DESC LIMIT 10")
    suspend fun getAllRecommendedProducts(): List<Product>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createProduct(product: Product)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createProducts(vararg products: Product)

    @Query("SELECT * FROM products WHERE id LIKE :id")
    suspend fun findById(id: Int): Product

    @Query("SELECT p.*, c.name AS categoryDescription FROM products p JOIN categories c ON p.categoryId = c.id WHERE p.title LIKE :term OR p.description LIKE :term OR p.price LIKE :term OR c.name LIKE :term")
    suspend fun findProducts(term: String): List<Product>

    @Query("SELECT * FROM products WHERE categoryId = :categoryId")
    suspend fun getProductsByCategory(categoryId: Int): List<Product>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateProduct(product: Product)

    @Query("UPDATE products SET isFavorited = :isFavorite WHERE id = :productId")
    suspend fun updateFavoriteStatus(productId: Int, isFavorite: Boolean)

    @Query("SELECT * FROM products WHERE isFavorited = 1")
    suspend fun getAllFavoriteProducts(): List<Product>

    @Delete
    suspend fun deleteProduct(product: Product)
}