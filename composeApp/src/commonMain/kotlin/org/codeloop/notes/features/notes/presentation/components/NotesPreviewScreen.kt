package org.codeloop.notes.features.notes.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun NotesPreviewScreenRoot(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {
    NotesPreviewScreen(
        modifier = modifier,
        onBackClick = onBackClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NotesPreviewScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val snackBarHostState by remember { mutableStateOf(SnackbarHostState()) }

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
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .imePadding()
                ,
                onClick = {

                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ){
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = "Done"
                )
            }
        },
        contentWindowInsets = WindowInsets.statusBars.only(WindowInsetsSides.Horizontal)
    ) { innerPadding ->

        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 12.dp),
            color = Color.Transparent
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding()
                ,
                state = listState,
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top),
            ) {
                item {
                    NotesEditTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                        ,
                        hint = "Enter your title here",
                        value = "",
                        onValueChange = {

                        },
                        textStyle = MaterialTheme.typography.titleLarge,
                        singleLine = true,
                    )
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp)
                        ,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Category to group related notes.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground.copy(0.6f),
                            maxLines = 1,
                            modifier = Modifier.weight(1f),
                            overflow = TextOverflow.Ellipsis
                        )

                        CategoryCardChip(
                            text = "\uD83D\uDCA1 Ideas",
                            selected = false,
                            onClick = {

                            }
                        )
                    }
                }

                item {
                    NotesEditTextField(
                        modifier = Modifier
                            .fillParentMaxHeight()
                        ,
                        hint = "Capture everything you don’t want to forget.",
                        value = "",
                        onValueChange = {

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
    }

}

@Composable
private fun NotesEditTextField(
    modifier: Modifier,
    hint : String = "",
    value : String? = "",
    textStyle: TextStyle,
    onValueChange : (String?) -> Unit = {},
    singleLine : Boolean = true,
) {
    var titleField by remember { mutableStateOf(TextFieldValue(value?:"", selection = TextRange((value?:"").length)))}

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