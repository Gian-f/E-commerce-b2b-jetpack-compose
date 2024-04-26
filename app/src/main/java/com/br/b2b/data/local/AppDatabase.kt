package com.br.b2b.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.br.b2b.data.local.converter.ImageListConverter
import com.br.b2b.data.local.dao.CategoryDao
import com.br.b2b.data.local.dao.ProductDao
import com.br.b2b.domain.model.Category
import com.br.b2b.domain.model.Product

@Database(
    entities = [
        Product::class,
        Category::class,
    ],
    version = 1, exportSchema = false
)
@TypeConverters(ImageListConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao

    abstract fun categoryDao(): CategoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "b2bDatabase.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}