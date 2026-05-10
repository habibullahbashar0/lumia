package com.luminaos.launcher.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.core.content.ContextCompat
import com.luminaos.launcher.data.AppRepository
import com.luminaos.launcher.data.LauncherDatabase
import com.luminaos.launcher.utils.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Boot receiver for handling boot completion and package events.
 * Initializes launcher services and monitors app installations.
 */
class BootReceiver : BroadcastReceiver() {
    private val logger = Logger("BootReceiver")

    override fun onReceive(context: Context, intent: Intent?) {
        if (intent == null) return

        logger.d("Received action: ${intent.action}")

        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED -> {
                handleBootCompleted(context)
            }
            Intent.ACTION_PACKAGE_ADDED -> {
                handlePackageAdded(context, intent)
            }
            Intent.ACTION_PACKAGE_REMOVED -> {
                handlePackageRemoved(context, intent)
            }
            Intent.ACTION_PACKAGE_CHANGED -> {
                handlePackageChanged(context, intent)
            }
        }
    }

    /**
     * Handle boot completed event.
     */
    private fun handleBootCompleted(context: Context) {
        logger.d("Device boot completed - starting services")
        // Start app monitor service
        val serviceIntent = Intent(context, AppMonitorService::class.java)
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ContextCompat.startForegroundService(context, serviceIntent)
            } else {
                context.startService(serviceIntent)
            }
        } catch (e: Exception) {
            logger.e("Error starting app monitor service: ${e.message}")
        }
    }

    /**
     * Handle package installed event.
     */
    private fun handlePackageAdded(context: Context, intent: Intent) {
        val packageName = intent.data?.schemeSpecificPart ?: return
        logger.d("Package added: $packageName")

        CoroutineScope(Dispatchers.Default).launch {
            try {
                val database = LauncherDatabase.getInstance(context)
                val repository = AppRepository(context, database.appInfoDao())
                repository.onAppInstalled(packageName)
            } catch (e: Exception) {
                logger.e("Error handling package added: ${e.message}")
            }
        }
    }

    /**
     * Handle package removed event.
     */
    private fun handlePackageRemoved(context: Context, intent: Intent) {
        val packageName = intent.data?.schemeSpecificPart ?: return
        logger.d("Package removed: $packageName")

        CoroutineScope(Dispatchers.Default).launch {
            try {
                val database = LauncherDatabase.getInstance(context)
                val repository = AppRepository(context, database.appInfoDao())
                repository.onAppUninstalled(packageName)
            } catch (e: Exception) {
                logger.e("Error handling package removed: ${e.message}")
            }
        }
    }

    /**
     * Handle package changed event.
     */
    private fun handlePackageChanged(context: Context, intent: Intent) {
        val packageName = intent.data?.schemeSpecificPart ?: return
        logger.d("Package changed: $packageName")
    }
}

/**
 * BootCompletedReceiver entry point.
 * Delegates to BootReceiver for handling.
 */
class BootCompletedReceiver : BroadcastReceiver() {
    private val bootReceiver = BootReceiver()

    override fun onReceive(context: Context, intent: Intent?) {
        bootReceiver.onReceive(context, intent)
    }
}
