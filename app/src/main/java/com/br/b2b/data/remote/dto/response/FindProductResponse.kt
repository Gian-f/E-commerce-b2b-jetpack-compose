package com.br.b2b.data.remote.dto.response

import com.br.b2b.domain.model.Product

data class FindProductResponse(
    val message: String,
    val result: Product,
    val status: Boolean
)
