package com.luminaos.launcher.data

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Build
import com.luminaos.launcher.utils.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * Repository class for managing app information.
 * Provides a clean API for accessing and managing app data from database and package manager.
 */
class AppRepository(
    private val context: Context,
    private val appInfoDao: AppInfoDao
) {
    private val packageManager: PackageManager = context.packageManager
    private val logger = Logger("AppRepository")

    /**
     * Get all visible apps as a Flow.
     */
    fun getVisibleAppsFlow(): Flow<List<AppInfo>> {
        return appInfoDao.getVisibleApps()
    }

    /**
     * Search apps by query string.
     */
    fun searchAppsFlow(query: String): Flow<List<AppInfo>> {
        return if (query.isBlank()) {
            appInfoDao.getVisibleApps()
        } else {
            appInfoDao.searchApps(query.trim())
        }
    }

    /**
     * Get all apps from package manager and sync with database.
     */
    suspend fun loadInstalledApps() {
        withContext(Dispatchers.IO) {
            try {
                val installedApps = mutableListOf<AppInfo>()

                val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    PackageManager.GET_META_DATA or PackageManager.MATCH_ALL
                } else {
                    @Suppress("DEPRECATION")
                    PackageManager.GET_META_DATA or PackageManager.MATCH_UNINSTALLED_PACKAGES
                }

                val packages = packageManager.getInstalledPackages(flags)

                for (packageInfo in packages) {
                    try {
                        val appInfo = packageInfo.applicationInfo ?: continue

                        // Skip installer and package manager apps
                        if (appInfo.packageName.let { it.contains("installer") || it.contains("package") || it.contains("store") }) {
                            continue
                        }

                        val appName = try {
                            packageManager.getApplicationLabel(appInfo).toString()
                        } catch (e: Exception) {
                            appInfo.packageName
                        }

                        val isSystemApp = (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0

                        val appData = AppInfo(
                            packageName = appInfo.packageName,
                            appName = appName,
                            activityName = getLaunchActivityName(appInfo.packageName),
                            isSystemApp = isSystemApp,
                            installTime = packageInfo.firstInstallTime,
                            updateTime = packageInfo.lastUpdateTime
                        )

                        installedApps.add(appData)
                    } catch (e: Exception) {
                        logger.e("Error loading app: ${e.message}")
                    }
                }

                // Clear and insert all apps
                appInfoDao.clearAllApps()
                appInfoDao.insertApps(installedApps)
                logger.d("Loaded ${installedApps.size} apps")
            } catch (e: Exception) {
                logger.e("Error loading installed apps: ${e.message}")
            }
        }
    }

    /**
     * Get launch activity name for an app.
     */
    private fun getLaunchActivityName(packageName: String): String {
        return try {
            val intent = packageManager.getLaunchIntentForPackage(packageName)
            intent?.component?.className ?: packageName
        } catch (e: Exception) {
            logger.w("Error getting launch activity for $packageName: ${e.message}")
            packageName
        }
    }

    /**
     * Hide an app.
     */
    suspend fun hideApp(packageName: String) {
        withContext(Dispatchers.IO) {
            appInfoDao.hideApp(packageName)
        }
    }

    /**
     * Show a hidden app.
     */
    suspend fun showApp(packageName: String) {
        withContext(Dispatchers.IO) {
            appInfoDao.showApp(packageName)
        }
    }

    /**
     * Update app custom label.
     */
    suspend fun updateAppLabel(packageName: String, customLabel: String) {
        withContext(Dispatchers.IO) {
            val app = appInfoDao.getAppByPackage(packageName)
            if (app != null) {
                appInfoDao.updateApp(app.copy(customLabel = customLabel))
            }
        }
    }

    /**
     * Get recent apps.
     */
    suspend fun getRecentApps(limit: Int = 10): List<AppInfo> {
        return withContext(Dispatchers.IO) {
            appInfoDao.getRecentApps(limit)
        }
    }

    /**
     * Get total app count.
     */
    suspend fun getAppCount(): Int {
        return withContext(Dispatchers.IO) {
            appInfoDao.getAppCount()
        }
    }

    /**
     * Handle app installation.
     */
    suspend fun onAppInstalled(packageName: String) {
        withContext(Dispatchers.IO) {
            try {
                val appInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
                val appName = try {
                    packageManager.getApplicationLabel(appInfo).toString()
                } catch (e: Exception) {
                    packageName
                }

                val isSystemApp = (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0

                val app = AppInfo(
                    packageName = packageName,
                    appName = appName,
                    activityName = getLaunchActivityName(packageName),
                    isSystemApp = isSystemApp,
                    installTime = System.currentTimeMillis()
                )

                appInfoDao.insertApp(app)
                logger.d("App installed: $packageName")
            } catch (e: Exception) {
                logger.e("Error handling app installation: ${e.message}")
            }
        }
    }

    /**
     * Handle app uninstallation.
     */
    suspend fun onAppUninstalled(packageName: String) {
        withContext(Dispatchers.IO) {
            appInfoDao.deleteAppByPackage(packageName)
            logger.d("App uninstalled: $packageName")
        }
    }
}
