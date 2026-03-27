package org.codeloop.notes.features.notes.presentation.notes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.codeloop.notes.features.notes.domain.model.NotesItem
import org.codeloop.notes.features.notes.presentation.components.NotesListCardItem
import org.codeloop.notes.utils.zonetimer.ZoneTimer

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NotesListScreen(
    modifier: Modifier,
    allList: List<NotesListUiModel>,
    onNoteClick: (Int) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.Top)
    ) {
        allList.forEach { item ->
            when (item) {
                NotesListUiModel.Footer -> {}

                is NotesListUiModel.Item -> {
                    NotesItem(
                        modifier = Modifier
                            .fillMaxSize(),
                        notesItem = item.notesItem,
                        onNoteClick = onNoteClick
                    )
                }

                NotesListUiModel.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularWavyProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(32.dp),
                            trackColor = MaterialTheme.colorScheme.background,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }

                NotesListUiModel.PlaceHolder -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(
                            8.dp,
                            Alignment.CenterVertically
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No Result Found !",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            maxLines = 1
                        )

                        TextButton(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary.copy(0.4f),
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            onClick = {

                            }
                        ) {
                            Text(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp),
                                text = "Retry",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onBackground,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NotesItem(
    modifier: Modifier,
    notesItem: List<NotesItem>,
    onNoteClick: (Int) -> Unit
) {
    val staggeredGridState = rememberLazyStaggeredGridState()

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
        verticalItemSpacing = 8.dp,
        state = staggeredGridState,
    ) {
        notesItem
            .sortedByDescending { it.createdAt }
            .groupBy { ZoneTimer.formatDate(it.createdAt) }
            .forEach { (date, notes) ->
                item(span = StaggeredGridItemSpan.FullLine) {
                    Text(
                        text = date,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }
                items(notes) { note ->
                    NotesListCardItem(
                        modifier = Modifier
                            .fillMaxWidth(),
                        notesItem = note,
                        onNoteClick = {
                            onNoteClick(note.id)
                        }
                    )
                }
            }

        item (
            span = StaggeredGridItemSpan.FullLine
        ){
            Spacer(
                modifier = Modifier.fillMaxWidth().height(100.dp)
            )
        }
    }
}