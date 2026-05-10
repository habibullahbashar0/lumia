package com.luminaos.launcher.service

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import androidx.core.content.ContextCompat
import com.luminaos.launcher.data.AppRepository
import com.luminaos.launcher.data.LauncherDatabase
import com.luminaos.launcher.utils.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * Service for monitoring app installations, uninstallations, and updates.
 * Maintains a live database of installed applications.
 */
class AppMonitorService : Service() {
    private val logger = Logger("AppMonitorService")
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var packageReceiver: BroadcastReceiver? = null

    override fun onCreate() {
        super.onCreate()
        logger.d("AppMonitorService created")
        setupPackageListener()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        logger.d("AppMonitorService started")

        // Load installed apps on service start
        serviceScope.launch {
            try {
                val database = LauncherDatabase.getInstance(applicationContext)
                val repository = AppRepository(applicationContext, database.appInfoDao())
                repository.loadInstalledApps()
                logger.d("Apps loaded in service")
            } catch (e: Exception) {
                logger.e("Error loading apps in service: ${e.message}")
            }
        }

        return START_STICKY
    }

    /**
     * Setup broadcast receiver for package changes.
     */
    private fun setupPackageListener() {
        packageReceiver = PackageChangeReceiver()
        val intentFilter = IntentFilter().apply {
            addAction(Intent.ACTION_PACKAGE_ADDED)
            addAction(Intent.ACTION_PACKAGE_REMOVED)
            addAction(Intent.ACTION_PACKAGE_CHANGED)
            addDataScheme("package")
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ContextCompat.registerReceiver(
                    this,
                    packageReceiver,
                    intentFilter,
                    ContextCompat.RECEIVER_EXPORTED
                )
            } else {
                @Suppress("DEPRECATION")
                registerReceiver(packageReceiver, intentFilter)
            }
            logger.d("Package change receiver registered")
        } catch (e: Exception) {
            logger.e("Error registering package receiver: ${e.message}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        logger.d("AppMonitorService destroyed")
        
        try {
            if (packageReceiver != null) {
                unregisterReceiver(packageReceiver)
                logger.d("Package receiver unregistered")
            }
        } catch (e: Exception) {
            logger.w("Error unregistering package receiver: ${e.message}")
        }

        serviceScope.cancel()
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}

/**
 * Broadcast receiver for package change events.
 */
private class PackageChangeReceiver : BroadcastReceiver() {
    private val logger = Logger("PackageChangeReceiver")

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent == null) return

        val packageName = intent.data?.schemeSpecificPart ?: return
        logger.d("Package event: ${intent.action} for $packageName")

        when (intent.action) {
            Intent.ACTION_PACKAGE_ADDED -> {
                handlePackageAdded(context, packageName)
            }
            Intent.ACTION_PACKAGE_REMOVED -> {
                handlePackageRemoved(context, packageName)
            }
            Intent.ACTION_PACKAGE_CHANGED -> {
                handlePackageChanged(context, packageName)
            }
        }
    }

    private fun handlePackageAdded(context: Context, packageName: String) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val database = LauncherDatabase.getInstance(context)
                val repository = AppRepository(context, database.appInfoDao())
                repository.onAppInstalled(packageName)
                logger.d("App installed: $packageName")
            } catch (e: Exception) {
                logger.e("Error handling package added: ${e.message}")
            }
        }
    }

    private fun handlePackageRemoved(context: Context, packageName: String) {
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val database = LauncherDatabase.getInstance(context)
                val repository = AppRepository(context, database.appInfoDao())
                repository.onAppUninstalled(packageName)
                logger.d("App uninstalled: $packageName")
            } catch (e: Exception) {
                logger.e("Error handling package removed: ${e.message}")
            }
        }
    }

    private fun handlePackageChanged(context: Context, packageName: String) {
        logger.d("App updated: $packageName")
    }
}
