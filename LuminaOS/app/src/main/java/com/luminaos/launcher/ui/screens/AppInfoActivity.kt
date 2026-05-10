package com.luminaos.launcher.ui.screens

import android.app.Activity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.luminaos.launcher.utils.Logger

/**
 * AppInfoActivity - Displays detailed information about a specific app.
 * Allows users to open app settings or uninstall the app.
 */
class AppInfoActivity : AppCompatActivity() {
    private val logger = Logger("AppInfoActivity")

    companion object {
        const val EXTRA_APP_NAME = "app_name"
        const val EXTRA_PACKAGE_NAME = "package_name"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logger.d("AppInfoActivity created")

        val appName = intent.getStringExtra(EXTRA_APP_NAME) ?: "Unknown App"
        val packageName = intent.getStringExtra(EXTRA_PACKAGE_NAME) ?: ""

        setContent {
            AppInfoScreen(
                appName = appName,
                packageName = packageName,
                onBackClick = { finish() },
                onUninstallClick = { handleUninstall(packageName) }
            )
        }
    }

    /**
     * Handle app uninstallation.
     */
    private fun handleUninstall(packageName: String) {
        logger.d("Uninstalling: $packageName")
        // This would be handled by system uninstall intent
        finish()
    }
}
