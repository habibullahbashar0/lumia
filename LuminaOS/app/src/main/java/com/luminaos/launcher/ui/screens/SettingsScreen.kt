package com.luminaos.launcher.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luminaos.launcher.R
import com.luminaos.launcher.utils.LauncherUtils
import com.luminaos.launcher.utils.Logger

/**
 * Settings screen composable.
 */
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val logger = remember { Logger("SettingsScreen") }
    val context = LocalContext.current

    BackHandler(onBack = onBackClick)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.background_color))
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(colorResource(R.color.surface_color))
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.settings),
                color = colorResource(R.color.text_primary),
                fontSize = 18.sp,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = null,
                tint = colorResource(R.color.accent_color),
                modifier = Modifier.size(24.dp)
            )
        }

        // Settings Items
        SettingItem(
            title = stringResource(R.string.set_as_default),
            description = "Set LuminaOS as default launcher",
            onClick = {
                logger.d("Setting as default launcher")
                LauncherUtils.setAsDefaultLauncher(context)
            }
        )

        SettingItem(
            title = stringResource(R.string.grid_size),
            description = "4x6 Grid",
            onClick = { logger.d("Grid size clicked") }
        )

        SettingItem(
            title = stringResource(R.string.theme),
            description = "Dark Theme",
            onClick = { logger.d("Theme clicked") }
        )

        SettingItem(
            title = stringResource(R.string.animation),
            description = "Smooth animations enabled",
            onClick = { logger.d("Animation clicked") }
        )

        SettingItem(
            title = stringResource(R.string.about),
            description = "LuminaOS v1.0.0",
            onClick = { logger.d("About clicked") }
        )

        Spacer(modifier = Modifier.weight(1f))

        // Back button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(colorResource(R.color.surface_color))
                .clickable(onClick = onBackClick)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Back",
                color = colorResource(R.color.text_primary),
                fontSize = 16.sp
            )
        }
    }
}

/**
 * Settings item component.
 */
@Composable
fun SettingItem(
    title: String,
    description: String = "",
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(colorResource(R.color.surface_color))
            .padding(16.dp)
            .border(bottom = 1.dp, color = colorResource(R.color.divider_color))
    ) {
        Text(
            text = title,
            color = colorResource(R.color.text_primary),
            fontSize = 16.sp
        )
        if (description.isNotEmpty()) {
            Text(
                text = description,
                color = colorResource(R.color.text_secondary),
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

/**
 * Border modifier extension.
 */
private fun Modifier.border(bottom: Int, color: androidx.compose.ui.graphics.Color): Modifier {
    return this.then(
        Modifier.padding(bottom = 1.dp)
    )
}

/**
 * App info activity for displaying app details and options.
 */
@Composable
fun AppInfoScreen(
    appName: String,
    packageName: String,
    onBackClick: () -> Unit,
    onUninstallClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val logger = remember { Logger("AppInfoScreen") }

    BackHandler(onBack = onBackClick)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.background_color))
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(colorResource(R.color.surface_color))
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = appName,
                color = colorResource(R.color.text_primary),
                fontSize = 18.sp,
                modifier = Modifier.weight(1f)
            )
        }

        // App Info
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Package: $packageName",
                color = colorResource(R.color.text_secondary),
                fontSize = 12.sp
            )
        }

        // Action buttons
        SettingItem(
            title = stringResource(R.string.app_info),
            onClick = {
                logger.d("Opening app info for $packageName")
                LauncherUtils.openAppSettings(context, packageName)
            }
        )

        SettingItem(
            title = stringResource(R.string.uninstall),
            onClick = {
                logger.d("Uninstalling $packageName")
                onUninstallClick()
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        // Back button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(colorResource(R.color.surface_color))
                .clickable(onClick = onBackClick)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Back",
                color = colorResource(R.color.text_primary),
                fontSize = 16.sp
            )
        }
    }
}
