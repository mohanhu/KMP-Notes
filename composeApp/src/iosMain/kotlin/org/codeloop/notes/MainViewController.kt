package org.codeloop.notes

import androidx.compose.ui.window.ComposeUIViewController
import org.codeloop.notes.core.presentation.App
import org.codeloop.notes.features.notes.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}