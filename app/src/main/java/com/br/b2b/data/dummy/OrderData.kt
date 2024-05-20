package com.br.b2b.data.dummy

import com.br.b2b.domain.model.Order
import com.br.b2b.util.FormatCurrency
import com.br.b2b.util.formattedDate
import java.util.Date
import java.util.UUID

object OrderData {
    val items = listOf(
        Order(
            id = 1555,
            userId = 1.toString(),
            requestedAt = formattedDate(Date()),
            status = "Entregue",
            total = FormatCurrency(1500.00),
            trackingNumber = UUID.randomUUID().toString().substring(0, 7)
        ),
        Order(
            id = 2123,
            userId = 1.toString(),
            requestedAt = formattedDate(Date()),
            status = "Pendente",
            total = FormatCurrency(1500.00),
            trackingNumber = UUID.randomUUID().toString().substring(0, 7)
        ),
        Order(
            id = 2123,
            userId = 1.toString(),
            requestedAt = formattedDate(Date()),
            status = "Pendente",
            total = FormatCurrency(1500.00),
            trackingNumber = UUID.randomUUID().toString().substring(0, 7)
        ),
        Order(
            id = 1514,
            userId = 1.toString(),
            requestedAt = formattedDate(Date()),
            status = "Pendente",
            total = FormatCurrency(1500.00),
            trackingNumber = UUID.randomUUID().toString().substring(0, 7)
        ),
        Order(
            id = 1235,
            userId = 1.toString(),
            requestedAt = formattedDate(Date()),
            status = "Cancelado",
            total = FormatCurrency(1500.00),
            trackingNumber = UUID.randomUUID().toString().substring(0, 7)
        ),
        Order(
            id = 5556,
            userId = 1.toString(),
            requestedAt = formattedDate(Date()),
            status = "Entregue",
            total = FormatCurrency(2000.00),
            trackingNumber = UUID.randomUUID().toString().substring(0, 7)
        ),
    )
}