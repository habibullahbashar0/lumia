package com.luminaos.launcher.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.provider.Settings
import androidx.core.content.ContextCompat

/**
 * Utility object for system and app-related functions.
 */
object LauncherUtils {
    private val logger = Logger("LauncherUtils")

    /**
     * Check if the launcher is set as default.
     */
    fun isDefaultLauncher(context: Context): Boolean {
        return try {
            val packages = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                context.packageManager.getHomeActivities(mutableListOf())
            } else {
                @Suppress("DEPRECATION")
                context.packageManager.getPreferredActivities(
                    mutableListOf(),
                    mutableListOf(),
                    null,
                    mutableListOf(),
                    mutableListOf(),
                    PackageManager.MATCH_DEFAULT_ONLY
                )
                listOf<android.content.pm.ResolveInfo>()
            }

            val launcherPackage = context.packageName
            packages.any { it.activityInfo.packageName == launcherPackage }
        } catch (e: Exception) {
            logger.e("Error checking default launcher: ${e.message}")
            false
        }
    }

    /**
     * Set this launcher as default.
     */
    fun setAsDefaultLauncher(context: Context) {
        try {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.setPackage(context.packageName)
            context.startActivity(intent)
        } catch (e: Exception) {
            logger.e("Error setting as default launcher: ${e.message}")
        }
    }

    /**
     * Launch an app by package name.
     */
    fun launchApp(context: Context, packageName: String, activityName: String): Boolean {
        return try {
            val intent = Intent()
            intent.component = android.content.ComponentName(packageName, activityName)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            logger.e("Error launching app $packageName: ${e.message}")
            false
        }
    }

    /**
     * Get app icon.
     */
    fun getAppIcon(context: Context, packageName: String): Drawable? {
        return try {
            context.packageManager.getApplicationIcon(packageName)
        } catch (e: Exception) {
            logger.w("Error getting app icon for $packageName: ${e.message}")
            null
        }
    }

    /**
     * Open app settings.
     */
    fun openAppSettings(context: Context, packageName: String): Boolean {
        return try {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.data = android.net.Uri.fromParts("package", packageName, null)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            true
        } catch (e: Exception) {
            logger.e("Error opening app settings: ${e.message}")
            false
        }
    }

    /**
     * Check if permission is granted.
     */
    fun isPermissionGranted(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Get grid dimensions for home screen.
     * Returns a pair of (columns, rows).
     */
    fun getGridDimensions(context: Context): Pair<Int, Int> {
        // Get from preferences or use defaults
        val columns = 4
        val rows = 6
        return Pair(columns, rows)
    }

    /**
     * Convert DP to pixels.
     */
    fun dpToPx(context: Context, dp: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    /**
     * Convert pixels to DP.
     */
    fun pxToDp(context: Context, px: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (px / scale + 0.5f).toInt()
    }
}

/**
 * Utility object for gesture-related functions.
 */
object GestureUtils {
    private val logger = Logger("GestureUtils")

    /**
     * Check if device supports gesture navigation.
     */
    fun supportsGestureNavigation(): Boolean {
        return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R
    }

    /**
     * Get gesture navigation bar height.
     */
    fun getGestureNavigationBarHeight(context: Context): Int {
        val navBarHeight = getNavigationBarHeight(context)
        return if (supportsGestureNavigation()) {
            // Gesture navigation bar is typically around 48dp
            LauncherUtils.dpToPx(context, 48f)
        } else {
            navBarHeight
        }
    }

    /**
     * Get navigation bar height.
     */
    private fun getNavigationBarHeight(context: Context): Int {
        val resources = context.resources
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) resources.getDimensionPixelSize(resourceId) else 0
    }
}

/**
 * Utility object for animation-related functions.
 */
object AnimationUtils {
    private val logger = Logger("AnimationUtils")

    const val ANIMATION_DURATION_SHORT = 150L
    const val ANIMATION_DURATION_DEFAULT = 300L
    const val ANIMATION_DURATION_LONG = 500L
}
