package org.codeloop.notes.ui.theme

import androidx.compose.ui.graphics.Color

fun String.toColor(): Color {
    val hex = removePrefix("#")
    val color = hex.toLong(16)
    return if (hex.length == 6) {
        Color(0xFF000000 or color)
    } else {
        Color(color)
    }
}
