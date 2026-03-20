package org.codeloop.notes.features.utils

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter

@Composable
fun SubGlideAsyncImage(
    model: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    loading: @Composable () -> Unit = {},
    error: @Composable () -> Unit = {}
) {

    val painter = rememberAsyncImagePainter(model = model)
    val state by painter.state.collectAsState()

    Box(modifier = modifier) {
        AnimatedContent(targetState = state) { currentState ->

            println("AnimatedContent State: $currentState")

            when (currentState) {

                is AsyncImagePainter.State.Loading -> loading()

                is AsyncImagePainter.State.Error -> error()

                is AsyncImagePainter.State.Success -> {
                    Image(
                        painter = painter,
                        contentDescription = contentDescription,
                        contentScale = contentScale,
                        modifier = Modifier.matchParentSize()
                    )
                }

                is AsyncImagePainter.State.Empty -> {
                    loading()
                }
            }
        }
    }
}