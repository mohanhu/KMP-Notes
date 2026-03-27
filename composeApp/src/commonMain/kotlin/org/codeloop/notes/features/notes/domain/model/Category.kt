package org.codeloop.notes.features.notes.domain.model

import androidx.compose.ui.graphics.Color

enum class Category(
    val title: String,
    val emoji: String,
    val color: Color
) {

    GENERAL(
        title = "General",
        emoji = "📝",
        color = Color(0xFF9E9E9E)
    ),

    WORK(
        title = "Work",
        emoji = "💼",
        color = Color(0xFF1976D2)
    ),

    STUDY(
        title = "Study",
        emoji = "📚",
        color = Color(0xFF388E3C)
    ),

    PERSONAL(
        title = "Personal",
        emoji = "🏠",
        color = Color(0xFFF57C00)
    ),

    IDEAS(
        title = "Ideas",
        emoji = "💡",
        color = Color(0xFFFFC107)
    ),

    TASKS(
        title = "Tasks",
        emoji = "✅",
        color = Color(0xFF7B1FA2)
    ),

    IMPORTANT(
        title = "Important",
        emoji = "📌",
        color = Color(0xFFD32F2F)
    ),

    HEALTH(
        title = "Health",
        emoji = "❤️",
        color = Color(0xFFE91E63)
    ),

    FINANCE(
        title = "Finance",
        emoji = "💰",
        color = Color(0xFF2E7D32)
    ),

    TRAVEL(
        title = "Travel",
        emoji = "✈️",
        color = Color(0xFF0288D1)
    );
}
