package com.br.b2b.domain.model

import androidx.room.PrimaryKey

data class ProductData(
@PrimaryKey
val id: Int,
val title: String,
val description: String,
val price: Double,
val category: Category,
val images: List<String>,
var isFavorited: Boolean = false
)
