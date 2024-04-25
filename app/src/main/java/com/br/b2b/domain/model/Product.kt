package com.br.b2b.domain.model

import javax.annotation.concurrent.Immutable

@Immutable
data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val category: Category,
    val images: List<String>,
    var isFavorited: Boolean = false
)
