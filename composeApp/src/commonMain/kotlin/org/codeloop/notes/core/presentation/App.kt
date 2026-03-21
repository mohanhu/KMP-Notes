package org.codeloop.notes.core.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.codeloop.notes.core.presentation.navigation.BottomNavigation
import org.codeloop.notes.core.presentation.navigation.NavigationScreens
import org.codeloop.notes.features.notes.presentation.components.NotesPreviewScreenRoot
import org.codeloop.notes.features.notes.presentation.home.HomeScreenRoot
import org.codeloop.notes.features.notes.presentation.notes.AddEditNoteScreen
import org.codeloop.notes.features.notes.presentation.notes.NotesScreenRoot
import org.codeloop.notes.ui.theme.NotesAppTheme

@Composable
fun App() {

    NotesAppTheme {

        val navController = rememberNavController()
        val bottomNavigation = remember { BottomNavigation.createList() }
        val currentBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = currentBackStackEntry?.destination

        val showBottomNavigation = bottomNavigation.any {
            it.route.route == currentRoute?.route
        }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background,
            contentWindowInsets = WindowInsets.statusBars,
            bottomBar = {
                AnimatedVisibility(
                    visible = showBottomNavigation,
                ) {
                    BottomNavigationBar(
                        items = bottomNavigation,
                        currentRoute = currentRoute?.route,
                        onItemClick = {
                            navController.navigate(it.route) {
                                popUpTo(NavigationScreens.default.route) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        ) { paddingValues ->
            NavHost(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                ,
                navController = navController,
                startDestination = NavigationScreens.default,
                enterTransition = {
                    slideInHorizontally { it }
                },
                exitTransition = {
                    slideOutHorizontally { it }
                }
            ) {
                composable(NavigationScreens.Home.route) {
                    HomeScreenRoot(
                        modifier = Modifier.fillMaxSize(),
                        gotoAddEditNote = {
                            navController.navigate(NavigationScreens.AddEditNote())
                        },
                        notesListScreen = {
                            navController.navigate(NavigationScreens.NotesScreen)
                        }
                    )
                }
                composable(NavigationScreens.Profile.route) {
                    Text(
                        text = "Profile"
                    )
                }
                composable(NavigationScreens.Settings.route) {
                    Text(
                        text = "Settings"
                    )
                }
                composable<NavigationScreens.AddEditNote> { entry ->
//                    val taskId = entry.toRoute<NavigationScreens.AddEditNote>()
                    AddEditNoteScreen(
                        modifier = Modifier.fillMaxSize()
                    )
                }

                composable(NavigationScreens.NotesScreen.route) {
                    NotesScreenRoot(
                        modifier = Modifier.fillMaxSize(),
                        onBackClick = {
                            navController.navigateUp()
                        },
                        onNoteClick = {

                        },
                        onNewNoteClick = {

                        }
                    )
                }

                composable<NavigationScreens.EditNotes> { entry ->
                    val taskId = entry.arguments?.getString("taskId")
                    NotesPreviewScreenRoot(
                        modifier = Modifier.fillMaxSize(),
                        onBackClick = {

                        }
                    )
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    items: List<BottomNavigation>,
    currentRoute: String?,
    onItemClick: (NavigationScreens) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .padding(bottom = 26.dp)
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
            .clip(CircleShape)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.inversePrimary,
                        MaterialTheme.colorScheme.surfaceContainerLowest,
                        MaterialTheme.colorScheme.inversePrimary,
                    ),
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
            ,
            containerColor = Color.Transparent,
        ) {
            items.forEach { item ->

                val selected = item.route.route == currentRoute

                NavigationBarItem(
                    modifier = Modifier
                        .height(IntrinsicSize.Min),
                    selected = selected,
                    alwaysShowLabel = true,
                    colors = NavigationBarItemColors(
                        selectedIconColor = MaterialTheme.colorScheme.background,
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledTextColor = MaterialTheme.colorScheme.surfaceContainer,
                        disabledIconColor = MaterialTheme.colorScheme.surfaceContainer,
                        selectedIndicatorColor = MaterialTheme.colorScheme.primary,
                    ),
                    label = {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    },
                    icon = {
                        BadgedBox(
                            badge = {
                                if (item.badgeCount > 0) {
                                    Badge { Text(text = item.badgeCount.toString()) }
                                }
                            },
                            content = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.title
                                )
                            }
                        )
                    },
                    onClick = {
                        onItemClick(item.route)
                    }
                )
            }
        }
    }
}