package org.codeloop.notes.utils.speech

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun createSpeechRecognizer() : SpeechRecognizerHelper  {

    val  context = LocalContext.current

    return remember(context) { SpeechRecognizerImpl(context) }
}


class SpeechRecognizerImpl(
    private val context: Context
) : SpeechRecognizerHelper{

    private val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)

    override fun startListening(onResult: (String) -> Unit) {

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
        }

        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(p0: Bundle?) {

            }

            override fun onBeginningOfSpeech() {

            }

            override fun onRmsChanged(p0: Float) {

            }

            override fun onBufferReceived(p0: ByteArray?) {

            }

            override fun onEndOfSpeech() {

            }

            override fun onError(p0: Int) {

            }

            override fun onResults(p0: Bundle?) {
                val result = p0?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                onResult.invoke(result?.get(0) ?: "")
            }

            override fun onPartialResults(p0: Bundle?) {

            }

            override fun onEvent(p0: Int, p1: Bundle?) {

            }
        })

        speechRecognizer.startListening(intent)
    }

    override fun stopListening() {
        speechRecognizer.stopListening()
    }
}