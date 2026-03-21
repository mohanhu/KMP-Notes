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
import org.codeloop.notes.ui.theme.toColor
import kotlin.random.Random

@Composable
fun NotesListCardItem(
    modifier: Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(
                min = (Random.nextInt(160,300)).dp,
                max = 400.dp
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
                text = "Daily Journey Entry",
                maxLines = 1,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "Before your app can request access to privacy-sensitive data like the camera, location, or contacts, you must provide a clear, user-friendly explanation of why your app needs that access. This is done by adding a specific key and string value to your project's Info.plist file. \n" +
                        "Apple Developer\n" +
                        "Apple Developer\n" +
                        " +2\n" +
                        "Open your project in Xcode and navigate to the Info.plist file.\n" +
                        "Add a new key from the dropdown list.\n" +
                        "Select the appropriate Privacy - Usage Description key (e.g., Privacy - Camera Usage Description or NSCameraUsageDescription in the source code).\n" +
                        "In the Value field, enter a concise message for the user, such as \"This app needs camera access to take photos\"",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f),
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "2 days ago".take(10),
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                modifier = Modifier.align(Alignment.End),
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }

}