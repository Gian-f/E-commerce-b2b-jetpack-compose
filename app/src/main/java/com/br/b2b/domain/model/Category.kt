package com.br.b2b.domain.model

import androidx.compose.runtime.Immutable

@Immutable
data class Category(
    val id: Int,
    val name: String,
    val image: String,
    val creationAt: String,
    val updatedAt: String
)
