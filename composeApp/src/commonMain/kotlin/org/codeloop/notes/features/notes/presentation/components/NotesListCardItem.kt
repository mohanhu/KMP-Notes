package org.codeloop.notes.features.notes.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.codeloop.notes.features.notes.domain.model.NotesItem
import org.codeloop.notes.ui.theme.toColor
import org.codeloop.notes.utils.zonetimer.ZoneTimer

@Composable
fun NotesListCardItem(
    modifier: Modifier,
    notesItem: NotesItem,
    onNoteClick: (Int) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(
                min = 50.dp,
                max = 300.dp
            )
            .height(IntrinsicSize.Min)
        ,
        colors = CardDefaults.cardColors(
            containerColor = "#dceefa".toColor(),
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        border = BorderStroke(
            1.dp, MaterialTheme.colorScheme.surfaceContainerLowest
        ),
        onClick = {
            onNoteClick(notesItem.id)
        },
        shape = RoundedCornerShape(14.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 10.dp)
                .height(IntrinsicSize.Min),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = notesItem.title,
                maxLines = 1,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = notesItem.description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f),
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = (ZoneTimer.formatDate(notesItem.createdAt)).take(10),
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                modifier = Modifier.align(Alignment.End),
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }

}