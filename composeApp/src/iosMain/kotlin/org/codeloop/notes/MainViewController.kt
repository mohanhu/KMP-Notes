package org.codeloop.notes

import androidx.compose.ui.window.ComposeUIViewController
import org.codeloop.notes.core.presentation.App

fun MainViewController() = ComposeUIViewController { App() }