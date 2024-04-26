package com.br.b2b.data.remote.dto.response

import com.br.b2b.domain.model.ProductData

data class ProductResponse(
    val message: String,
    val result: List<ProductData>,
    val status: Boolean
)
