package com.br.jetpacktest.data.dummy

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.ImageNotSupported
import com.br.jetpacktest.R
import com.br.jetpacktest.domain.model.Products
import com.br.jetpacktest.util.FormatCurrency

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