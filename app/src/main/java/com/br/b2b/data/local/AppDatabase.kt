package com.br.b2b.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.br.b2b.data.local.converter.ImageListConverter
import com.br.b2b.data.local.dao.CartItemDao
import com.br.b2b.data.local.dao.CategoryDao
import com.br.b2b.data.local.dao.ProductDao
import com.br.b2b.data.local.dao.UserDao
import com.br.b2b.domain.model.CartItem
import com.br.b2b.domain.model.Category
import com.br.b2b.domain.model.Product
import com.br.b2b.domain.model.User

@Database(
    entities = [
        Product::class,
        Category::class,
        CartItem::class,
        User::class
    ],
    version = 1, exportSchema = false
)
@TypeConverters(ImageListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun cartItemDao(): CartItemDao
    abstract fun categoryDao(): CategoryDao
    abstract fun userDao(): UserDao
}