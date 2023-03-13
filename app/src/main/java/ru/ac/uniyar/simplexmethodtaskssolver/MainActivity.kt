package ru.ac.uniyar.simplexmethodtaskssolver

import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import ru.ac.uniyar.simplexmethodtaskssolver.ui.navigation.Content
import ru.ac.uniyar.simplexmethodtaskssolver.ui.navigation.MainRouter
import ru.ac.uniyar.simplexmethodtaskssolver.ui.theme.SimplexMethodTasksSolverTheme

class MainActivity : ComponentActivity() {
    companion object {
        lateinit var instance: MainActivity
            private set
    }

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = this

        val root = MainRouter(
            componentContext = defaultComponentContext(),
            storeFactory = DefaultStoreFactory()
        )

        setContent {
            SimplexMethodTasksSolverTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Content(router = root)
                }
            }
//            val cameraPermission = rememberPermissionState(permission = Manifest.permission.USE_BIOMETRIC)
//
//            Button(
//                enabled = (cameraPermission.status != PermissionStatus.Granted), // if the permission is NOT granted, enable the button
//                onClick = {
//                    cameraPermission.launchPermissionRequest()
//                }) {
//                Text(if (cameraPermission.status == PermissionStatus.Granted) "Permission Granted" else "Даун")
//            }
        }
    }
}
