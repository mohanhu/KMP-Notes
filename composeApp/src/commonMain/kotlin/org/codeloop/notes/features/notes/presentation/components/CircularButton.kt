package org.codeloop.notes.features.notes.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CircularButton(
    modifier: Modifier,
    isLoading : Boolean,
    isEnabled : Boolean,
    text : String,
    onClick : () -> Unit
) {

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {

        if (isLoading) {
            CircularWavyProgressIndicator(
                modifier = Modifier.size(50.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)
            )
        }
        else {
            Button(
                onClick = {
                    if (isEnabled) {
                        onClick.invoke()
                    }
                },
                modifier = Modifier.matchParentSize(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(
                        alpha = if (isEnabled) 1f else 0.5f
                    )
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}