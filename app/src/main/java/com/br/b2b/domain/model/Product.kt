package com.br.b2b.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.br.b2b.data.local.converter.ImageListConverter

@Entity(
    tableName = "products",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["categoryId"])]
)
data class Product(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    @TypeConverters(ImageListConverter::class) val images: List<String>,
    var isFavorited: Boolean = false,
    val categoryId: Int,
    @ColumnInfo(name = "category_description") val categoryDescription: String? = null
)