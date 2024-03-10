package com.br.jetpacktest.domain.model

import javax.annotation.concurrent.Immutable

@Immutable
data class Products(
    val id: Int,
    val description: String,
    val price: String,
    val image: Int
)
