package com.br.b2b.data.remote.dto.response

import com.br.b2b.domain.model.Category

data class CategoryResponse(
    val message: String,
    val result: List<Category>,
    val status: Boolean
)
