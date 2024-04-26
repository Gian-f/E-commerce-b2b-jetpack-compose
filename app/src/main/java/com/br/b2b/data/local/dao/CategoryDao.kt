package com.br.b2b.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.br.b2b.domain.model.Category

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createCategory(category: Category)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun createCategories(vararg categories: Category)

    @Query("SELECT * FROM categories")
    suspend fun getAllCategories(): List<Category>

    @Query("SELECT * FROM categories WHERE id LIKE :id LIMIT 1")
    fun findById(id: Int): Category
}