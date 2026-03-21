package org.codeloop.notes.features.notes.presentation.components.dialog

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.codeloop.notes.ui.theme.toColor

@Composable
fun AlertDialogs(
    title: String = "",
    message: String = "",
    positiveButtonText: String = "",
    negativeButtonText: String = "",
    onDismiss: () -> Unit,
    onPermissionGranted: () -> Unit,
    onNegativeClick: () -> Unit
) {

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            TextButton(
                onClick = {
                    onPermissionGranted.invoke()
                },
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    text =  positiveButtonText,
                    fontWeight = FontWeight.Normal,
                    style = MaterialTheme.typography.titleSmall,
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onNegativeClick,
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    text = negativeButtonText,
                    fontWeight = FontWeight.Normal,
                    style = MaterialTheme.typography.titleSmall,
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        iconContentColor = MaterialTheme.colorScheme.primary,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        textContentColor = MaterialTheme.colorScheme.onSurface.copy(0.4f),
    )

}