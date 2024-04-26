package com.br.b2b.data.local.converter

import com.br.b2b.domain.model.Product
import com.br.b2b.domain.model.ProductData

fun convertProduct(products: List<ProductData>): List<Product> {
    return products.map { product ->
        Product(
            id = product.id,
            title = product.title,
            price = product.price,
            description = product.description,
            categoryId = product.category.id,
            images = product.images,
        )
    }
}