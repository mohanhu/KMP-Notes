package org.codeloop.notes.features.notes.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import notes.composeapp.generated.resources.Res
import notes.composeapp.generated.resources.mic_search
import org.codeloop.notes.utils.permission.PermissionManager
import org.codeloop.notes.utils.permission.PermissionType
import org.codeloop.notes.utils.speech.createSpeechRecognizer
import org.jetbrains.compose.resources.painterResource

@Composable
fun TextInputBar(
    modifier: Modifier,
    hint : String = "",
    value : String? = "",
    onValueChange : (String?) -> Unit = {},
    onImeAction : () -> Unit = {},
    singleLine : Boolean = true,
    error : String? = null,
    maxChar : Int = 0,
    validation : (String?) -> Boolean = { true }
) {
    var titleField by remember { mutableStateOf(TextFieldValue(value?:"", selection = TextRange((value?:"").length)))}

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.Top),
        horizontalAlignment = Alignment.Start
    ) {
        CompositionLocalProvider(
            LocalTextSelectionColors provides TextSelectionColors(
                handleColor = MaterialTheme.colorScheme.primary,
                backgroundColor = MaterialTheme.colorScheme.primary.copy(0.4f)
            )
        )
        {
            OutlinedTextField(
                value = titleField,
                onValueChange = {
                    titleField = it
                    onValueChange(it.text)
                },
                singleLine = singleLine,
                textStyle = MaterialTheme.typography.bodyMedium,
                shape = RoundedCornerShape(20.dp),
                placeholder = {
                    Text(
                        text = hint,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(0.4f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                keyboardOptions = KeyboardOptions(
                    autoCorrectEnabled = true,
                    keyboardType = KeyboardType.Text,
                    showKeyboardOnFocus = true,
                    imeAction = if (singleLine) ImeAction.Done else ImeAction.Unspecified
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        onImeAction()
                    }
                ),
                colors = OutlinedTextFieldDefault.defaultTextField(),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .height(IntrinsicSize.Min)
                ,
            )
        }

        Row (
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.Start),
        ){
            if (validation.invoke(value) && error?.isNotEmpty() == true) {
                Text(
                    text = "$error *",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Red.copy(alpha = 0.4f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Spacer(Modifier.weight(1f))

            if (maxChar > 0) {
                Text(
                    text = "${titleField.text.length}/$maxChar",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = if (maxChar > titleField.text.length) MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                    else MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier,
    hint : String = "",
    value : String? = "",
    onValueChange : (String?) -> Unit = {},
    onImeAction : () -> Unit = {},
) {

    var titleField by remember { mutableStateOf(TextFieldValue(value?:"", selection = TextRange((value?:"").length)))}

    val speechRecognizer = createSpeechRecognizer()
    val permissionHelper = PermissionManager()

    val permissionLaunch : (PermissionType) -> Unit = { permission ->
        permissionHelper.requestPermission(
            permission = permission,
            onResult = {
                speechRecognizer.startListening {
                    titleField = TextFieldValue(it, selection = TextRange(it.length))
                    onValueChange(it)
                }
            }
        )
    }

    LaunchedEffect(Unit) {
        if (!permissionHelper.onPermissionGranted(PermissionType.MICROPHONE)) {
        }
    }

    val launch : (Boolean) -> Unit = { enable ->
        if (enable) {
            permissionLaunch.invoke(PermissionType.MICROPHONE)
        }
        else {
            speechRecognizer.stopListening()
        }
    }

    CompositionLocalProvider(
        LocalTextSelectionColors provides TextSelectionColors(
            handleColor = MaterialTheme.colorScheme.primary,
            backgroundColor = MaterialTheme.colorScheme.primary.copy(0.4f)
        )
    )
    {
        OutlinedTextField(
            value = titleField,
            onValueChange = {
                titleField = it
                onValueChange(it.text)
            },
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium,
            shape = RoundedCornerShape(32.dp),
            placeholder = {
                Text(
                    text = hint,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(0.4f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            keyboardOptions = KeyboardOptions(
                autoCorrectEnabled = true,
                keyboardType = KeyboardType.Text,
                showKeyboardOnFocus = true,
                imeAction = ImeAction.Search
            ),
            leadingIcon = {
                Icon(
                    modifier = Modifier.padding(10.dp),
                    imageVector = Icons.Default.Search,
                    tint = MaterialTheme.colorScheme.onBackground,
                    contentDescription = "Search"
                )
            },
            trailingIcon = {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AnimatedVisibility(
                        visible = titleField.text?.isNotEmpty() == true
                    ) {
                        IconButton(
                            modifier = Modifier
                                .padding(8.dp),
                            onClick = {
                                titleField = TextFieldValue("")
                                onValueChange.invoke("")
                            },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainer
                            )
                        ) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                    IconButton(
                        modifier = Modifier
                            .padding(8.dp),
                        onClick = {
                            launch.invoke(true)
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer
                        )
                    ) {
                        Image(
                            modifier = Modifier
                                .padding(8.dp)
                                .size(24.dp)
                            ,
                            painter = painterResource(Res.drawable.mic_search),
                            colorFilter = ColorFilter.tint(
                                MaterialTheme.colorScheme.onBackground
                            ),
                            contentDescription = "mic_search"
                        )
                    }
                }
            },
            keyboardActions = KeyboardActions(
                onNext = {
                    onImeAction()
                }
            ),
            colors = OutlinedTextFieldDefault
                .defaultTextField()
                .copy(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                )
            ,
            modifier = modifier
                .fillMaxWidth(),
        )
    }
}

object OutlinedTextFieldDefault {
    @Composable
    fun defaultTextField() : TextFieldColors {
        return OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            disabledContainerColor = Color.Transparent,
            disabledBorderColor = Color.Transparent,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedTextColor = MaterialTheme.colorScheme.onBackground,
            unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
            disabledTextColor = MaterialTheme.colorScheme.onBackground,
            selectionColors = TextSelectionColors(
                handleColor = MaterialTheme.colorScheme.primary,
                backgroundColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
            ),
        )
    }
}