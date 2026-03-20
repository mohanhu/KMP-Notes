package org.codeloop.notes.features.notes.presentation.notes

import androidx.compose.animation.core.repeatable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import notes.composeapp.generated.resources.Res
import notes.composeapp.generated.resources.create_note
import org.codeloop.notes.features.notes.presentation.components.CategoryCardChip
import org.codeloop.notes.features.notes.presentation.components.CircularButton
import org.codeloop.notes.features.notes.presentation.components.TextInputBar
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    modifier: Modifier
) {

    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val snackBarHostState by remember { mutableStateOf(SnackbarHostState()) }

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
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(Res.string.create_note),
                        style = MaterialTheme.typography.titleMedium,
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
                        onClick = {

                        }
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
        bottomBar = {
            CircularButton(
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(bottom = 20.dp)
                    .imePadding()
                    .fillMaxWidth()
                    .height(54.dp)
                ,
                isLoading = false,
                isEnabled = true,
                text = "Submit",
                onClick = {

                }
            )
        }
    ) { innerPadding ->

        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 12.dp)
            ,
            color = Color.Transparent
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 100.dp)
                ,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp, Alignment.Top)
            ) {

                AddNoteTitleDec(
                    modifier = Modifier.fillMaxWidth(),
                    icon = "M",
                    title = "What’s this note about?",
                    description = "A short title helps you find it faster later."
                ) {
                    TextInputBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                        ,
                        hint = "Type a title...",
                        value = null,
                        onImeAction = {

                        },
                        onValueChange = {

                        },
                        error = "Title cannot be empty",
                        validation = {
                            it?.trim()?.isEmpty() ?: false
                        },
                        maxChar = 10,
                        singleLine = true,
                    )
                }

                AddNoteTitleDec(
                    modifier = Modifier.fillMaxWidth(),
                    icon = "D",
                    title = "Add details",
                    description = "Capture everything you don’t want to forget."
                ) {
                    TextInputBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 160.dp, max = 200.dp)
                        ,
                        hint = "Start writing your note...",
                        value = null,
                        onImeAction = {

                        },
                        onValueChange = {

                        },
                        error = "Description cannot be empty",
                        validation = {
                            it?.trim()?.isEmpty() ?: false
                        },
                        maxChar = 2000,
                        singleLine = false,
                    )
                }

                AddNoteTitleDec(
                    modifier = Modifier.fillMaxWidth(),
                    icon = "C",
                    title = "Category",
                    description = "Use category to group related notes."
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(14) {
                            CategoryCardChip(
                                text = "\uD83D\uDCA1 Ideas",
                                selected = false,
                                onClick = {

                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AddNoteTitleDec(
    modifier: Modifier = Modifier.fillMaxWidth(),
    icon : String = "",
    title: String = "",
    description: String = "",
    content : @Composable ColumnScope.() -> Unit = {}
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(14.dp, Alignment.Top)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp, Alignment.Start),
        )
        {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.Red.copy(0.4f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = icon,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(1.dp, Alignment.Top)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        content()
    }

}











































