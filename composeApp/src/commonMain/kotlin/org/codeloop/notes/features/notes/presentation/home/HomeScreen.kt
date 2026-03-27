package org.codeloop.notes.features.notes.presentation.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import notes.composeapp.generated.resources.Res
import notes.composeapp.generated.resources.mic_search
import notes.composeapp.generated.resources.recent_notes
import notes.composeapp.generated.resources.search
import notes.composeapp.generated.resources.see_all
import org.codeloop.notes.features.notes.domain.model.NotesItem
import org.codeloop.notes.features.notes.presentation.components.HomeStufferCard
import org.codeloop.notes.features.notes.presentation.components.TaskListCardItem
import org.codeloop.notes.features.utils.SubGlideAsyncImage
import org.codeloop.notes.features.utils.rememberShowFab
import org.codeloop.notes.ui.theme.toColor
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel,
    notesListScreen: () -> Unit,
    gotoAddEditTask: () -> Unit = {},
    gotoAddEditNote: (NotesItem?) -> Unit = {}
) {
    HomeScreen(
        modifier = modifier,
        notesListScreen = notesListScreen,
        gotoAddEditTask = gotoAddEditTask,
        gotoAddEditNote = gotoAddEditNote
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    modifier: Modifier,
    gotoAddEditNote: (NotesItem?) -> Unit = {},
    gotoAddEditTask: () -> Unit = {},
    notesListScreen: () -> Unit
) {

    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val snackBarHostState by remember { mutableStateOf(SnackbarHostState()) }

    val showFab = rememberShowFab(listState)
    var isFabExpanded by remember { mutableStateOf(false)}

    LaunchedEffect(listState.firstVisibleItemIndex) {
        if (listState.isScrollInProgress) {
            isFabExpanded = false
        }
    }

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets.statusBars.only(WindowInsetsSides.Horizontal),
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                snackbar = { snackBarData ->
                    Snackbar(
                        modifier = Modifier.fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    ) {
                        Row {
                            Text(
                                text = snackBarData.visuals.message,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            snackBarData.visuals.actionLabel?.let {
                                Text(
                                    text = it,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onBackground,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .padding(start = 8.dp)
                                        .clickable {
                                            snackBarData.performAction()
                                        }
                                )
                            }
                        }
                    }
                }
            )
        },
        topBar = {
            TopAppBar(
                modifier = Modifier.fillMaxWidth(),
                windowInsets = WindowInsets.statusBars.only(WindowInsetsSides.Horizontal),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent
                ),
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(
                            8.dp,
                            Alignment.CenterHorizontally
                        )
                    )
                    {
                        SubGlideAsyncImage(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape),
                            model = "https://img.freepik.com/free-photo/closeup-scarlet-macaw-from-side-view-scarlet-macaw-closeup-head_488145-3540.jpg?semt=ais_hybrid&w=740&q=80",
                            contentDescription = "image",
                            loading = {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(30.dp),
                                    trackColor = MaterialTheme.colorScheme.primary,
                                    color = MaterialTheme.colorScheme.background
                                )
                            },
                            error = {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(30.dp),
                                    trackColor = MaterialTheme.colorScheme.primary,
                                    color = MaterialTheme.colorScheme.background
                                )
                            }
                        )

                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "Mohan",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onBackground,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )

                            Text(
                                text = "Developer 🚀",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                },
                actions = {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(
                            8.dp,
                            Alignment.CenterHorizontally
                        )
                    ) {
                        IconButton(
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                                contentColor = MaterialTheme.colorScheme.onBackground
                            ),
                            onClick = {

                            }
                        ) {
                            Icon(
                                modifier = Modifier.padding(10.dp),
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings",
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            val rotation by animateFloatAsState(
                targetValue = if (isFabExpanded) 45f else 0f,
                animationSpec = tween(300),
                label = "rotation"
            )

            Column (
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Bottom)
            ){
               if (isFabExpanded) {
                   FloatingActionButton(
                       onClick = {
                           gotoAddEditTask()
                       },
                       containerColor = MaterialTheme.colorScheme.primary,
                   ){
                       Text(
                           modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                           text = "New Task",
                           style = MaterialTheme.typography.bodyMedium
                       )
                   }

                   FloatingActionButton(
                       onClick = {
                           gotoAddEditNote(null)
                       },
                       containerColor = MaterialTheme.colorScheme.primary,
                   ){
                       Text(
                           modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                           text = "New Note",
                           style = MaterialTheme.typography.bodyMedium
                       )
                   }
               }

                ExtendedFloatingActionButton(
                    expanded = showFab,
                    onClick = {
                        isFabExpanded = !isFabExpanded
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    icon = {
                        AnimatedContent(
                            targetState = isFabExpanded,
                            label = "icon",
                            transitionSpec = {
                                fadeIn(animationSpec = tween(200)) + scaleIn() togetherWith
                                        fadeOut(animationSpec = tween(200)) + scaleOut()
                            }
                        ) { expanded ->
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add",
                                modifier = Modifier.graphicsLayer{
                                    rotationZ = rotation
                                }
                            )
                        }

                    },
                    text = {
                        Text(
                            text = if (isFabExpanded) "Close" else "Create",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
            }
        }
    ) { innerPadding ->

        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 12.dp),
            color = Color.Transparent
        ) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp, Alignment.Top)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .background(MaterialTheme.colorScheme.surfaceContainerLowest),
                    horizontalArrangement = Arrangement.spacedBy(
                        8.dp,
                        Alignment.CenterHorizontally
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        modifier = Modifier.padding(10.dp),
                        imageVector = Icons.Default.Search,
                        tint = MaterialTheme.colorScheme.onBackground,
                        contentDescription = "Search"
                    )

                    Text(
                        modifier = Modifier.weight(1f),
                        text = stringResource(Res.string.search),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(0.4f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Image(
                        modifier = Modifier
                            .padding(8.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceContainer)
                            .padding(8.dp)
                            .size(24.dp),
                        painter = painterResource(Res.drawable.mic_search),
                        colorFilter = ColorFilter.tint(
                            MaterialTheme.colorScheme.onBackground
                        ),
                        contentDescription = "mic_search"
                    )
                }

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    state = listState,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top)
                ) {

                    item {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            horizontalArrangement = Arrangement.spacedBy(
                                8.dp,
                                Alignment.CenterHorizontally
                            ),
                            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top),
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = (160 * 2).dp)
                        ) {
                            itemsIndexed(listOf<String>("Text Note", "Task")) { index, item ->

                                val isSpecial = index == 1

                                HomeStufferCard(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(if (isSpecial) 8.dp else 0.dp)
                                        .height(IntrinsicSize.Min)
                                        .then(
                                            if (isSpecial) {
                                                Modifier.graphicsLayer {
                                                    rotationZ = -8f
                                                    clip = false
                                                }.background(
                                                    Brush.radialGradient(
                                                        listOf(Color.White, "#4c9aff".toColor())
                                                    ),
                                                    shape = RoundedCornerShape(16.dp)
                                                )
                                            } else {
                                                Modifier.background(
                                                    color = MaterialTheme.colorScheme.surfaceContainerLowest,
                                                    shape = RoundedCornerShape(16.dp)
                                                )
                                            }
                                        ),
                                    isSpecial = isSpecial,
                                    title = item
                                )

                            }
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(
                                8.dp,
                                Alignment.CenterHorizontally
                            ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(Res.string.recent_notes),
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onBackground,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )

                            TextButton(
                                onClick = {

                                }
                            ) {
                                Text(
                                    text = stringResource(Res.string.see_all),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    maxLines = 1,
                                    fontWeight = FontWeight.Bold,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }
                        }
                    }

                    items(
                        (1..20).toList()
                    ) {
                        TaskListCardItem(
                            modifier = modifier
                                .fillMaxWidth()
                                .heightIn(min = 150.dp)
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }
}



































































