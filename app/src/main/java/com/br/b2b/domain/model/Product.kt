package com.br.b2b.domain.model

import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.br.b2b.data.local.converter.ImageListConverter

@Immutable
@Entity(tableName = "products")
data class Product(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    @ColumnInfo(name = "categoryId") val categoryId: Int,
    @TypeConverters(ImageListConverter::class) val images: List<String>,
    var isFavorited: Boolean = false
)
