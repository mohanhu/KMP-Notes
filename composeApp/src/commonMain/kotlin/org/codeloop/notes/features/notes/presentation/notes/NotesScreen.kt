package org.codeloop.notes.features.notes.presentation.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.codeloop.notes.features.notes.presentation.components.SearchBar
import org.codeloop.notes.features.utils.rememberShowFab

@Composable
fun NotesScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: NotesListViewModel,
    onBackClick: () -> Unit,
    onNoteClick: (Int) -> Unit,
    onNewNoteClick: () -> Unit
){
    val allList by viewModel.allList.collectAsStateWithLifecycle()
    val favList by viewModel.favouritesList.collectAsStateWithLifecycle()

    NotesScreen(
        modifier = modifier,
        state = viewModel.uiState,
        allList = allList,
        favouritesList = favList,
        uiEvent = viewModel.uiEvent,
        action = viewModel.accept,
        onBackClick = onBackClick,
        onNoteClick = onNoteClick,
        onNewNoteClick = onNewNoteClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NotesScreen(
    modifier: Modifier = Modifier,
    state: StateFlow<NotesListUiState>,
    allList: List<NotesListUiModel>,
    favouritesList: List<NotesListUiModel>,
    uiEvent: SharedFlow<NotesListUiEvent>,
    action: (NotesListUiAction) -> Unit,
    onBackClick: () -> Unit,
    onNoteClick: (Int) -> Unit,
    onNewNoteClick: () -> Unit
) {

    val uiState by state.collectAsStateWithLifecycle()

    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val snackBarHostState by remember { mutableStateOf(SnackbarHostState()) }

    val showFab = rememberShowFab(listState)

    val tabList = remember { listOf("All","Favorites") }
    val pager = rememberPagerState(initialPage = 0, initialPageOffsetFraction = 0f, pageCount = { tabList.size })

    LaunchedEffect(pager.currentPage) {
        if (pager.currentPage!=uiState.tabSelectedIndex) {
            action(NotesListUiAction.ChangeTab(pager.currentPage))
        }
    }

    Scaffold(
        modifier = modifier,
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
                    Text(
                        text = "Notes",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                            contentColor = MaterialTheme.colorScheme.onBackground
                        ),
                        onClick = onBackClick
                    ) {
                        Icon(
                            modifier = Modifier.padding(10.dp),
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "ArrowBack",
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                modifier = Modifier,
                expanded = showFab,
                onClick = onNewNoteClick,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add"
                    )
                },
                text = {
                    Text(
                        text = "New Note",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        },
        contentWindowInsets = WindowInsets.statusBars.only(WindowInsetsSides.Horizontal)
    ) { innerPadding ->

        Surface(
            modifier = Modifier.padding(innerPadding)
                .padding(horizontal = 12.dp),
            color = Color.Transparent
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top)
            ) {
                SearchBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                    ,
                    hint = "Search",
                    value = "",
                    onValueChange = {},
                    onImeAction = {},
                )

                SecondaryTabRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CircleShape)
                    ,
                    selectedTabIndex = 0,
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                    divider = {
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.fillMaxWidth(),
                            color = Color.Transparent,
                        )
                    },
                    indicator = {
                        val selection = pager.currentPage.coerceIn(tabList.indices)
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier
                                .fillMaxSize()
                                .tabIndicatorOffset(selection)
                            ,
                            color = Color.Transparent,
                        )
                    }
                ) {
                    tabList.forEachIndexed { index, tab ->
                        val selected = pager.currentPage == index

                        Tab(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(4.dp)
                                .clip(CircleShape)
                                .background(
                                    if (selected) {
                                        MaterialTheme.colorScheme.primary.copy(0.8f)
                                    }
                                    else {
                                        Color.Transparent
                                    }
                                )
                            ,
                            selected = selected,
                            onClick = {
                                scope.launch {
                                    pager.animateScrollToPage(index)
                                }
                            },
                            interactionSource = remember { MutableInteractionSource() },
                            text = {
                                Text(
                                    text = tab,
                                    style = MaterialTheme.typography.bodyMedium,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = if (selected) {
                                        MaterialTheme.colorScheme.surfaceContainerLowest
                                    } else MaterialTheme.colorScheme.onBackground
                                )
                            }
                        )

                    }
                }

                HorizontalPager(
                    state = pager,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                    ,
                ) { page ->
                    if(pager.currentPage != page) return@HorizontalPager
                    when(page) {
                        0 -> {
                            NotesListScreen(
                                modifier = Modifier.fillMaxSize(),
                                allList = allList,
                                onNoteClick = onNoteClick
                            )
                        }
                        1 -> {
                            NotesListScreen(
                                modifier = Modifier.fillMaxSize(),
                                allList = favouritesList,
                                onNoteClick = onNoteClick
                            )
                        }
                    }
                }
            }
        }
    }
}