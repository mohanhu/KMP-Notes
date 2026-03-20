package org.codeloop.notes.core.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

@Serializable
sealed class NavigationScreens(
    val route : String
) {
    @Serializable
    data object Home : NavigationScreens("home")

    @Serializable
    data object Profile : NavigationScreens("profile")

    @Serializable
    data object Settings : NavigationScreens("settings")

    @Serializable
    data class AddEditNote(val taskId : String = "") : NavigationScreens("add_edit_note")

    @Serializable
    data object NotesScreen : NavigationScreens("notes")

    companion object {
        val default get() = NotesScreen
    }
}

data class BottomNavigation (
    val title : String,
    val icon : ImageVector,
    val route : NavigationScreens,
    val badgeCount : Int = 0
) {
    companion object {
        fun createList() : List<BottomNavigation> {
            return listOf(
                BottomNavigation(
                    title = "Home",
                    icon = Icons.Default.Home,
                    route = NavigationScreens.Home
                ),
                BottomNavigation(
                    title = "Profile",
                    icon = Icons.Default.Person,
                    route = NavigationScreens.Profile
                ),
                BottomNavigation(
                    title = "Setting",
                    icon = Icons.Default.Settings,
                    route = NavigationScreens.Settings
                )
            )
        }
    }
}

