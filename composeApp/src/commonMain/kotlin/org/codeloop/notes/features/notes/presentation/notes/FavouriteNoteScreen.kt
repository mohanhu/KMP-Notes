package org.codeloop.notes.features.notes.presentation.notes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import org.codeloop.notes.features.notes.presentation.components.NotesListCardItem
import kotlin.random.Random

@Composable
fun FavouriteNoteScreen(
    modifier: Modifier,
    onNoteClick: (String) -> Unit
) {

    val listState = rememberLazyStaggeredGridState()
    val scope = rememberCoroutineScope()
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
        contentWindowInsets = WindowInsets.statusBars.only(WindowInsetsSides.Horizontal)
    ) { innerPadding ->
        Surface(
            modifier = Modifier.padding(innerPadding),
            color = Color.Transparent
        ) {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.Start),
                verticalItemSpacing = 8.dp,
                state = listState,
                contentPadding = PaddingValues(bottom = 150.dp)
            ) {
                items((1..30).toList()) {
                    NotesListCardItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = (Random.nextInt(160,300)).dp)
                            .height(IntrinsicSize.Min)
                        ,
                    )
                }
            }
        }
    }
}