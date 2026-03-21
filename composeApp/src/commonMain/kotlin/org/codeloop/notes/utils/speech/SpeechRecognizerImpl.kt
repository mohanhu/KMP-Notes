package org.codeloop.notes.utils.speech

import androidx.compose.runtime.Composable

@Composable
expect fun createSpeechRecognizer() : SpeechRecognizerHelper


interface SpeechRecognizerHelper {
    fun startListening(onResult: (String) -> Unit)
    fun stopListening()
}
