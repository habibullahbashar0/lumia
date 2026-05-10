package com.luminaos.launcher

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.luminaos.launcher.service.AppMonitorService
import com.luminaos.launcher.ui.screens.AppDrawerScreenWrapper
import com.luminaos.launcher.ui.screens.HomeScreenWrapper
import com.luminaos.launcher.ui.screens.SettingsScreen
import com.luminaos.launcher.utils.Logger
import android.content.Intent

/**
 * MainActivity - Main entry point for LuminaOS Launcher
 * Manages the launcher interface with home screen, app drawer, and settings.
 */
class MainActivity : AppCompatActivity() {
    private val logger = Logger("MainActivity")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logger.d("MainActivity created")

        // Start app monitor service
        startAppMonitorService()

        // Set the Compose UI
        setContent {
            var showAppDrawer by remember { mutableStateOf(false) }
            var showSettings by remember { mutableStateOf(false) }

            // Main launcher screen
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(R.color.background_color))
            ) {
                // Home screen
                HomeScreenWrapper(
                    onOpenAppDrawer = { showAppDrawer = true },
                    onSettingsClick = { showSettings = true },
                    context = this@MainActivity
                )

                // App drawer overlay
                AnimatedVisibility(
                    visible = showAppDrawer,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it })
                ) {
                    AppDrawerScreenWrapper(
                        onClose = { showAppDrawer = false },
                        context = this@MainActivity
                    )
                }

                // Settings overlay
                AnimatedVisibility(
                    visible = showSettings,
                    enter = slideInVertically(initialOffsetY = { it }),
                    exit = slideOutVertically(targetOffsetY = { it })
                ) {
                    SettingsScreen(
                        onBackClick = { showSettings = false }
                    )
                }
            }
        }
    }

    /**
     * Start the app monitor service.
     */
    private fun startAppMonitorService() {
        try {
            val serviceIntent = Intent(this, AppMonitorService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent)
            } else {
                @Suppress("DEPRECATION")
                startService(serviceIntent)
            }
            logger.d("App monitor service started")
        } catch (e: Exception) {
            logger.e("Error starting app monitor service: ${e.message}")
        }
    }

    override fun onResume() {
        super.onResume()
        logger.d("MainActivity resumed")
    }

    override fun onPause() {
        super.onPause()
        logger.d("MainActivity paused")
    }

    override fun onDestroy() {
        super.onDestroy()
        logger.d("MainActivity destroyed")
    }

    /**
     * Handle back press - exit app drawer if open.
     */
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        logger.d("Back pressed")
        // Don't go back from launcher home screen
        moveTaskToBack(false)
    }
}
