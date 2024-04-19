package com.br.b2b.data.dummy

import com.br.b2b.domain.model.Products
import com.br.b2b.util.FormatCurrency
import com.br.jetpacktest.R
import javax.annotation.concurrent.Immutable

@Immutable
object ProductsData {
    val products = listOf(
        Products(
            id = 1,
            description = "Óculos",
            image = R.drawable.oculos_2,
            price = FormatCurrency(500.00),
        ),
        Products(
            id = 2,
            description = "Óculos",
            image = R.drawable.oculos_1,
            price = FormatCurrency(80.00),
        ),
        Products(
            id = 3,
            description = "Óculos",
            image = R.drawable.oculos_2,
            price = FormatCurrency(80.00),
        ),
        Products(
            id = 4,
            description = "Relógio",
            image = R.drawable.relogio_1,
            price = FormatCurrency(300.00),
        ),
        Products(
            id = 5,
            description = "Câmera",
            image = R.drawable.camera_1,
            price = FormatCurrency(1000.00),
        ),
        Products(
            id = 6,
            description = "Fone",
            image = R.drawable.fone_1,
            price = FormatCurrency(200.00),
        )
    )
}