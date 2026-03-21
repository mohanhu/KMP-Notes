package org.codeloop.notes.utils.permission

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import org.codeloop.notes.features.notes.presentation.components.dialog.AlertDialogs

@Composable
actual fun PermissionManager(): PermissionHelper {

    val context = LocalContext.current
    var permissionResult : ((Boolean) -> Unit) ?= null


    var showAlertSheet: PermissionType? by remember { mutableStateOf(null) }

    if(showAlertSheet!=null) {

        AlertDialogs(
            title = "Permission Required",
            message = "You have denied the ${showAlertSheet?.name?.lowercase()?:""} permission. Please enable it in the app settings.",
            positiveButtonText = "Go to Settings",
            negativeButtonText = "Cancel",
            onDismiss = {
                showAlertSheet = null
            },
            onPermissionGranted = {
                showAlertSheet = null
                val intent =
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                context.startActivity(intent)
            },
            onNegativeClick = {
                showAlertSheet = null
            }
        )
    }

    val permissionLaunch = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->

        val isGranted = result.all { it.value }

        permissionResult?.invoke(isGranted)

        result.forEach { (permission, isGranted) ->
           if (!isGranted) {
               if (ActivityCompat.checkSelfPermission(context,permission) != PackageManager.PERMISSION_GRANTED) {
                   showAlertSheet = PermissionType.entries.firstOrNull { type -> type.toPermissionList().any { it == permission} }
               }
           }
        }
    }

    return remember {
        AndroidPermissionManager(
            context = context,
            launch = { types, onResult ->
                permissionResult = onResult
                permissionLaunch.launch(
                    types.toPermissionList().toTypedArray()
                )
            }
        )
    }
}

class AndroidPermissionManager(
    private val context: Context,
    private val launch: (PermissionType,(Boolean) -> Unit) -> Unit,
) : PermissionHelper {

    override fun requestPermission(
        permission: PermissionType,
        onResult: (Boolean) -> Unit
    ) {
        launch.invoke(permission,onResult)
    }

    override fun onPermissionGranted(permission: PermissionType): Boolean {
        return permission.toPermissionList().all {
            ActivityCompat.checkSelfPermission(context,it) == PackageManager.PERMISSION_GRANTED
        }
    }
}
 fun PermissionType.toPermissionList(): List<String> {
    return when (this) {
        PermissionType.MICROPHONE -> {
            listOf(
                Manifest.permission.RECORD_AUDIO
            )
        }

        PermissionType.CAMERA -> {
            listOf(
                Manifest.permission.CAMERA
            )
        }

        PermissionType.STORAGE -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                listOf()
            } else {
                listOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            }
        }

        PermissionType.NOTIFICATION -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                listOf(
                    Manifest.permission.POST_NOTIFICATIONS
                )
            } else {
                listOf()
            }
        }

        PermissionType.PHONE -> {
            listOf(
                Manifest.permission.READ_PHONE_STATE
            )
        }
    }
}