package org.codeloop.notes.features.utils

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow

@Composable
fun rememberShowFab(listState: LazyListState) : Boolean {

    var showFab by remember { mutableStateOf(true) }
    var lastIndex by remember { mutableStateOf(0) }
    var lastOffset by remember { mutableStateOf(0) }

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset
        }.collect { (index, offset) ->
            showFab = if (index != lastIndex ) {
                index < lastIndex // scrolling up
            } else {
                offset < lastOffset // scrolling up
            }
            lastIndex = index
            lastOffset = offset
        }
    }
    return showFab
}

