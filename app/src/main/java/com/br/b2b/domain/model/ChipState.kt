package com.br.b2b.domain.model

data class ChipState(
    val inProgress: Boolean = true,
    val delivered: Boolean = false,
    val cancelled: Boolean = false
)