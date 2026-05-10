package com.luminaos.launcher.ui.screens

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.luminaos.launcher.R
import com.luminaos.launcher.data.AppInfo
import com.luminaos.launcher.ui.components.AppIcon
import com.luminaos.launcher.ui.components.EmptyState
import com.luminaos.launcher.ui.components.LoadingIndicator
import com.luminaos.launcher.ui.components.QuickSettingsBar
import com.luminaos.launcher.utils.LauncherUtils
import com.luminaos.launcher.utils.Logger
import com.luminaos.launcher.viewmodel.HomeScreenViewModel

/**
 * Home screen composable for LuminaOS launcher.
 * Displays app icons in a grid layout with gesture support.
 */
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = viewModel(),
    onOpenAppDrawer: () -> Unit,
    onSettingsClick: () -> Unit,
    onAppClick: (AppInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    val gridColumns by viewModel.gridColumns.collectAsState()
    val recentApps by viewModel.recentApps.collectAsState()

    var offsetY by remember { mutableFloatStateOf(0f) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.home_screen_bg))
            .draggable(
                state = rememberDraggableState { delta ->
                    offsetY += delta
                    if (offsetY < -50f) {
                        onOpenAppDrawer()
                        offsetY = 0f
                    }
                },
                orientation = Orientation.Vertical
            )
    ) {
        // Quick Settings Bar
        QuickSettingsBar(onSettingsClick = onSettingsClick)

        // Home Screen Grid
        Spacer(modifier = Modifier.height(16.dp))

        if (recentApps.isEmpty()) {
            EmptyState(
                message = stringResource(R.string.empty_state),
                modifier = Modifier.weight(1f)
            )
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(gridColumns),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(recentApps) { app ->
                    AppIcon(
                        label = app.getDisplayName(),
                        onClick = { onAppClick(app) }
                    )
                }
            }
        }

        // Bottom gesture indicator
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .background(colorResource(R.color.divider_color)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.app_drawer),
                color = colorResource(R.color.text_secondary),
                fontSize = 10.sp
            )
        }
    }
}

/**
 * Home screen wrapper with ViewModel initialization.
 */
@Composable
fun HomeScreenWrapper(
    onOpenAppDrawer: () -> Unit,
    onSettingsClick: () -> Unit,
    context: android.content.Context,
    modifier: Modifier = Modifier
) {
    val logger = remember { Logger("HomeScreen") }

    val handleAppClick = { app: AppInfo ->
        logger.d("App clicked: ${app.appName}")
        LauncherUtils.launchApp(context, app.packageName, app.activityName)
    }

    HomeScreen(
        onOpenAppDrawer = onOpenAppDrawer,
        onSettingsClick = onSettingsClick,
        onAppClick = handleAppClick,
        modifier = modifier
    )
}
