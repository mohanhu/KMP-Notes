package org.codeloop.notes.utils.permission

import androidx.compose.runtime.Composable

@Composable
expect fun PermissionManager() : PermissionHelper

interface PermissionHelper {
    fun requestPermission(permission: PermissionType, onResult: (Boolean) -> Unit) {}

    fun onPermissionGranted(permission: PermissionType) : Boolean
}

enum class PermissionType {
    MICROPHONE,
    CAMERA,
    STORAGE,
    NOTIFICATION,
    PHONE
}

