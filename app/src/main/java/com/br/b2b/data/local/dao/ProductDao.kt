package com.br.b2b.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.br.b2b.domain.model.Product

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createProduct(product: Product)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun createProducts(vararg products: Product)

    @Query("SELECT * FROM products")
    suspend fun getAllProducts(): List<Product>

    @Query("SELECT * FROM products WHERE id LIKE :id LIMIT 1")
    fun findById(id: Int): Product
}