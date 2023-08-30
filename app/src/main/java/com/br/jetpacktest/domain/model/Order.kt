package com.br.jetpacktest.domain.model

data class Order(
    val id: Long = 0,
    val orderId: Long,
    val requestedAt: String,
    val status: String,
    val total: String,
    val trackingNumber: String,
)
