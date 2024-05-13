package com.br.b2b.util

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

fun applyCpfMask(input: TextFieldValue, cursorPosition: Int): TextFieldValue {
    val digits = input.text.filter { it.isDigit() }
    val formatted = buildString {
        for (i in digits.indices) {
            if (i == 3 || i == 6) {
                append('.')
            } else if (i == 9) {
                append('-')
            }
            append(digits[i])
        }
    }

    val newCursorPosition = when {
        cursorPosition <= 3 -> cursorPosition
        cursorPosition <= 7 -> cursorPosition + 1
        else -> cursorPosition + 2
    }

    return TextFieldValue(
        text = formatted,
        selection = TextRange(newCursorPosition.coerceIn(0, formatted.length))
    )
}


fun applyCpfMask(input: String, cursorPosition: Int): Pair<String, Int> {
    val digits = input.filter { it.isDigit() }
    val formatted = buildString {
        for (i in digits.indices) {
            if (i == 3 || i == 6) {
                append('.')
            } else if (i == 9) {
                append('-')
            }
            append(digits[i])
        }
    }

    val newCursorPosition = when {
        cursorPosition <= 3 -> cursorPosition
        cursorPosition <= 7 -> cursorPosition + 1
        else -> cursorPosition + 2
    }

    return Pair(formatted, newCursorPosition.coerceIn(0, formatted.length))
}
