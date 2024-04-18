package com.br.b2b.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

fun formattedDate(date: Date): String {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return dateFormat.format(date)
}

fun formatDateAgo(date: Date): String {
    val currentTime = System.currentTimeMillis()
    val diffMillis = currentTime - date.time
    val diffHours = TimeUnit.MILLISECONDS.toHours(diffMillis)

    return if (diffHours < 1) {
        "Agora mesmo"
    } else {
        "$diffHours horas atrás"
    }
}