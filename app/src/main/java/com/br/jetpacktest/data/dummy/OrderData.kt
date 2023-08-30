package com.br.jetpacktest.data.dummy

import com.br.jetpacktest.domain.model.Order
import com.br.jetpacktest.util.FormatCurrency
import com.br.jetpacktest.util.formattedDate
import java.util.Date
import java.util.UUID

object OrderData {
    val items = listOf(
        Order(
            orderId = 1555,
            requestedAt = formattedDate(Date()),
            status = "Entregue",
            total = FormatCurrency(1500.00),
            trackingNumber = UUID.randomUUID().toString().substring(0, 7)
        ),
        Order(
            orderId = 2123,
            requestedAt = formattedDate(Date()),
            status = "Pendente",
            total = FormatCurrency(1500.00),
            trackingNumber = UUID.randomUUID().toString().substring(0, 7)
        ),
        Order(
            orderId = 1235,
            requestedAt = formattedDate(Date()),
            status = "Pendente",
            total = FormatCurrency(1500.00),
            trackingNumber = UUID.randomUUID().toString().substring(0, 7)
        ),
        Order(
            orderId = 1514,
            requestedAt = formattedDate(Date()),
            status = "Pendente",
            total = FormatCurrency(1500.00),
            trackingNumber = UUID.randomUUID().toString().substring(0, 7)
        ),
        Order(
            orderId = 1235,
            requestedAt = formattedDate(Date()),
            status = "Cancelado",
            total = FormatCurrency(1500.00),
            trackingNumber = UUID.randomUUID().toString().substring(0, 7)
        ),
        Order(
            orderId = 5556,
            requestedAt = formattedDate(Date()),
            status = "Entregue",
            total = FormatCurrency(2000.00),
            trackingNumber = UUID.randomUUID().toString().substring(0, 7)
        ),
    )
}