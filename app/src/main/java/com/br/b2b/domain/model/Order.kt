package com.br.b2b.domain.model

import javax.annotation.concurrent.Immutable

@Immutable
data class Order(
    val id: Long = 0,
    val orderId: Long,
    val requestedAt: String,
    val status: String,
    val total: String,
    val trackingNumber: String,
)
