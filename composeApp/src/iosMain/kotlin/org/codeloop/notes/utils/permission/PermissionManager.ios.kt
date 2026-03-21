package org.codeloop.notes.utils.permission

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import platform.AVFAudio.AVAudioSession
import platform.AVFAudio.AVAudioSessionRecordPermissionGranted
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.authorizationStatusForMediaType
import platform.AVFoundation.requestAccessForMediaType
import platform.Photos.PHAuthorizationStatusAuthorized
import platform.Photos.PHAuthorizationStatusLimited
import platform.Photos.PHPhotoLibrary
import platform.UserNotifications.UNAuthorizationOptionAlert
import platform.UserNotifications.UNAuthorizationOptionBadge
import platform.UserNotifications.UNAuthorizationOptionSound
import platform.UserNotifications.UNUserNotificationCenter

@Composable
actual fun PermissionManager() : PermissionHelper {

    var showAlertSheet: PermissionType? by remember { mutableStateOf(null) }

    val permissionHelper = remember {
        IosPermissionManager(
            launch = { permission, onResult ->

                when (permission) {
                    PermissionType.MICROPHONE ->{
                        AVAudioSession.sharedInstance()
                            .requestRecordPermission {
                                if (!it) showAlertSheet = permission
                                onResult.invoke(it)
                            }
                    }

                    PermissionType.CAMERA ->
                        AVCaptureDevice.requestAccessForMediaType(
                            AVMediaTypeVideo
                        ) {
                            if (!it) showAlertSheet = permission
                            onResult.invoke(it)
                        }

                    PermissionType.STORAGE -> {
                        PHPhotoLibrary.requestAuthorization { status ->
                            val granted = status == PHAuthorizationStatusAuthorized || status == PHAuthorizationStatusLimited
                            if (!granted) showAlertSheet = permission
                            onResult(granted)
                        }
                    }

                    PermissionType.NOTIFICATION -> {
                        UNUserNotificationCenter.currentNotificationCenter()
                            .requestAuthorizationWithOptions(
                                options = UNAuthorizationOptionAlert or
                                        UNAuthorizationOptionSound or
                                        UNAuthorizationOptionBadge
                            ) { granted, _ ->
                                if (!granted) showAlertSheet = permission
                                onResult(granted)
                            }
                    }

                    PermissionType.PHONE -> {
                        onResult(false)
                    }
                }

            }
        )
    }

    return permissionHelper
}

class IosPermissionManager(
    private val launch: (PermissionType,(Boolean) -> Unit) -> Unit,
) : PermissionHelper {
    override fun requestPermission(
        permission: PermissionType,
        onResult: (Boolean) -> Unit
    ) {
        launch.invoke(permission,onResult)
    }

    override fun onPermissionGranted(permission: PermissionType): Boolean {
        return when(permission) {
            PermissionType.MICROPHONE -> {
                AVAudioSession.sharedInstance().recordPermission() == AVAudioSessionRecordPermissionGranted
            }

            PermissionType.CAMERA -> {
                AVCaptureDevice.authorizationStatusForMediaType(
                    AVMediaTypeVideo
                ) == PHAuthorizationStatusAuthorized
            }
            PermissionType.STORAGE ->  {
                val status = PHPhotoLibrary.authorizationStatus()
                status == PHAuthorizationStatusAuthorized ||
                        status == PHAuthorizationStatusLimited
            }
            PermissionType.NOTIFICATION -> false
            PermissionType.PHONE -> false
        }
    }
}