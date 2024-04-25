package com.br.b2b.data.remote.dto.response

import com.br.b2b.domain.model.Product

data class ProductResponse(
    val message: String,
    val result: List<Product>,
    val status: Boolean
)
