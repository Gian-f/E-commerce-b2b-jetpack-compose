package com.br.b2b.util

import java.text.NumberFormat
import java.util.Locale

fun FormatCurrency(amount: Double): String {
    val format: NumberFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    return format.format(amount)
}