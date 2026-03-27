package org.codeloop.notes.features.notes.presentation.notes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import org.codeloop.notes.features.notes.domain.model.Category
import org.codeloop.notes.features.notes.presentation.components.CategoryCardChip
import org.codeloop.notes.features.notes.presentation.components.dialog.AlertDialogs
import org.codeloop.notes.features.notes.presentation.components.dialog.MenuSheetDialogs
import org.codeloop.notes.features.notes.presentation.components.dialog.MenuSheetItem
import org.codeloop.notes.utils.loadstate.LoadState
import org.codeloop.notes.utils.zonetimer.ZoneTimer
import kotlin.time.Clock

@Composable
fun NotesPreviewScreenRoot(
    modifier: Modifier = Modifier,
    viewModel: NotesPreviewViewModel,
    onBackClick: () -> Unit,
) {
    NotesPreviewScreen(
        modifier = modifier,
        viewModel.uiState,
        viewModel.uiEvent,
        viewModel.accept,
        onBackClick = onBackClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NotesPreviewScreen(
    modifier: Modifier = Modifier,
    state: StateFlow<NotesPreviewUiState>,
    uiEvent: SharedFlow<NotesPreviewUiEvent>,
    accept: (NotesPreviewUiAction) -> Unit,
    onBackClick: () -> Unit,
) {

    val uiState by state.collectAsStateWithLifecycle(minActiveState = Lifecycle.State.STARTED)

    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val snackBarHostState by remember { mutableStateOf(SnackbarHostState()) }
    val keyBoardController = LocalSoftwareKeyboardController.current


    val progressLoader = uiState.loadStates.let {
        it.action is LoadState.Loading || it.refresh is LoadState.Loading || it.prepend is LoadState.Loading || it.append is LoadState.Loading
    }

    LaunchedEffect(uiState.notesCreated) {
        if (uiState.notesCreated) {
            snackBarHostState.showSnackbar("Note created successfully 🚀", duration = SnackbarDuration.Short)
            onBackClick()
        }
    }

    LaunchedEffect(uiState.notesDeleted) {
        if (uiState.notesDeleted) {
            snackBarHostState.showSnackbar("Note deleted successfully 🚀", duration = SnackbarDuration.Short)
            onBackClick()
        }
    }

    /*Dialogs*/
    var showDeleteAlert: Boolean by remember { mutableStateOf(false) }
    var showCategory: Category? by remember { mutableStateOf(null) }

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
                            .padding(bottom = 50.dp)
                        ,
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    ) {
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ){
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
                },
                actions = {
                    AnimatedVisibility(
                        visible = uiState.notesList != null && !progressLoader
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            IconButton(
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                                    contentColor = MaterialTheme.colorScheme.onBackground
                                ),
                                onClick = {
                                    accept.invoke(NotesPreviewUiAction.FavoriteNote)
                                }
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .padding(10.dp),
                                    imageVector = if (uiState.notesList?.isFavourite == true) Icons.Default.Favorite
                                    else Icons.Default.FavoriteBorder
                                    ,
                                    contentDescription = "Favorite",
                                    tint = if (uiState.notesList?.isFavourite == true) Color.Red
                                    else Color.Red.copy(0.4f)
                                )
                            }
                            IconButton(
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                                    contentColor = MaterialTheme.colorScheme.onBackground
                                ),
                                onClick = {
                                    keyBoardController?.hide()
                                    showDeleteAlert = true
                                }
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .padding(10.dp),
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = Color.Red.copy(0.7f)
                                )
                            }
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = uiState.title?.trim()?.isNotEmpty() == true
                        &&
                        uiState.description?.trim()?.isNotEmpty() == true
                        && !progressLoader,
                enter = scaleIn(
                    initialScale = 0.5f,
                    animationSpec = tween(500)
                ),
                exit = scaleOut(
                    targetScale = 0.5f,
                    animationSpec = tween(500)
                )
            ) {
                FloatingActionButton(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .imePadding(),
                    onClick = {
                        keyBoardController?.hide()
                        accept(NotesPreviewUiAction.SaveNote)
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ) {
                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = "Done"
                    )
                }
            }
        },
        contentWindowInsets = WindowInsets.statusBars.only(WindowInsetsSides.Horizontal)
    ) { innerPadding ->

        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 12.dp),
            color = Color.Transparent
        )
        {

            val transition = rememberInfiniteTransition()
            val progress by transition.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000,),
                    repeatMode = RepeatMode.Restart
                )
            )

            AnimatedVisibility(
                visible = progressLoader
            ) {
                LinearWavyProgressIndicator(
                    progress = {
                        progress
                    },
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.primary.copy(0.4f),
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding(),
                state = listState,
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.Top),
            ) {
                item {
                    NotesEditTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min),
                        hint = "Enter your title here",
                        value = uiState.title,
                        onValueChange = {
                            accept(NotesPreviewUiAction.OnTypeTitle(it))
                        },
                        textStyle = MaterialTheme.typography.titleLarge,
                        singleLine = true,
                    )
                }

                item {
                    Text(
                        text = Clock.System.now().toEpochMilliseconds().let {
                            ZoneTimer.formatDate(it) + " - " + ZoneTimer.formatTime(it)
                        } + "  " + "${uiState.description?.length} Characters",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(0.6f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp),
                    )
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Category to group related notes.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(0.6f),
                            maxLines = 2,
                            modifier = Modifier.weight(1f),
                            overflow = TextOverflow.Ellipsis
                        )

                        CategoryCardChip(
                            text = uiState.category.let { it.emoji + "  " + it.title },
                            selected = false,
                            onClick = {
                                showCategory = uiState.category
                            }
                        )
                    }
                }

                item {
                    NotesEditTextField(
                        modifier = Modifier
                            .fillParentMaxHeight(),
                        hint = "Capture everything you don’t want to forget.",
                        value = uiState.description,
                        onValueChange = {
                            accept(NotesPreviewUiAction.OnTypeDescription(it))
                        },
                        textStyle = MaterialTheme.typography.bodyMedium,
                        singleLine = false,
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(100.dp))
                }
            }
        }


        if (showDeleteAlert) {
            AlertDialogs(
                title = "Delete Note",
                message = "Are you sure you want to delete this note?",
                positiveButtonText = "Delete",
                negativeButtonText = "Cancel",
                onDismiss = {
                    showDeleteAlert = false
                },
                onPermissionGranted = {
                    accept(NotesPreviewUiAction.DeleteNote)
                    showDeleteAlert = false
                },
                onNegativeClick = {
                    showDeleteAlert = false
                }
            )
        }

        if (showCategory!=null) {
            MenuSheetDialogs(
                menuSheetItems = Category.entries.map {
                    MenuSheetItem(
                        title = it.title,
                        isSelected = showCategory?.title == it.title
                    )
                },
                onItemClick = { item ->
                    val selected = Category.entries.find { it.title == item.title }?:return@MenuSheetDialogs
                    accept(NotesPreviewUiAction.ChangeCategory(selected))
                    showCategory = null
                },
                onDismiss = {
                    showCategory = null
                }
            )
        }
    }
}


@Composable
private fun NotesEditTextField(
    modifier: Modifier,
    hint: String = "",
    value: String? = "",
    textStyle: TextStyle,
    onValueChange: (String?) -> Unit = {},
    singleLine: Boolean = true,
) {

    var titleField by remember(value) {
        mutableStateOf(
            TextFieldValue(
                value ?: "",
                selection = TextRange((value ?: "").length)
            )
        )
    }

    CompositionLocalProvider(
        LocalTextSelectionColors provides TextSelectionColors(
            handleColor = MaterialTheme.colorScheme.primary,
            backgroundColor = MaterialTheme.colorScheme.primary.copy(0.4f)
        )
    ) {
        OutlinedTextField(
            modifier = modifier,
            value = titleField,
            onValueChange = {
                titleField = it
                onValueChange(it.text)
            },
            placeholder = {
                Text(
                    text = hint,
                    style = textStyle,
                    color = MaterialTheme.colorScheme.onBackground.copy(0.4f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            },
            keyboardActions = KeyboardActions(

            ),
            keyboardOptions = KeyboardOptions(
                autoCorrectEnabled = true,
                showKeyboardOnFocus = true,
                keyboardType = KeyboardType.Text,
                imeAction = if (singleLine) ImeAction.Done else ImeAction.Unspecified
            ),
            singleLine = singleLine,
            textStyle = textStyle,
            shape = RoundedCornerShape(20.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                disabledBorderColor = Color.Transparent,
                errorBorderColor = Color.Transparent,
            ),
        )
    }
}