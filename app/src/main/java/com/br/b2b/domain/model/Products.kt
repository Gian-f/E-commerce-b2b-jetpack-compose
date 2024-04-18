package com.br.b2b.domain.model

import javax.annotation.concurrent.Immutable

@Immutable
data class Products(
    val id: Int,
    val description: String,
    val price: String,
    val image: Int
)
