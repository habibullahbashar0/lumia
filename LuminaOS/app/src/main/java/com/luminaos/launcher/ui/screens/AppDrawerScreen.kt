package com.luminaos.launcher.ui.screens

import androidx.compose.animation.animateAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
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
import com.luminaos.launcher.ui.components.SearchBar
import com.luminaos.launcher.utils.LauncherUtils
import com.luminaos.launcher.utils.Logger
import com.luminaos.launcher.viewmodel.AppDrawerViewModel
import com.luminaos.launcher.viewmodel.AppListViewModel

/**
 * App drawer screen composable for LuminaOS launcher.
 * Displays all installed apps with search functionality.
 */
@Composable
fun AppDrawerScreen(
    appListViewModel: AppListViewModel = viewModel(),
    drawerViewModel: AppDrawerViewModel = viewModel(),
    onClose: () -> Unit,
    onAppClick: (AppInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    val appsState by appListViewModel.appsState.collectAsState()
    val searchQuery by appListViewModel.searchQuery.collectAsState()
    var offsetY by remember { mutableFloatStateOf(0f) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.app_drawer_bg))
            .draggable(
                state = rememberDraggableState { delta ->
                    offsetY += delta
                    if (offsetY > 50f) {
                        onClose()
                        offsetY = 0f
                    }
                },
                orientation = Orientation.Vertical
            )
    ) {
        // Header with close button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(colorResource(R.color.surface_color))
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.all_apps),
                color = colorResource(R.color.text_primary),
                fontSize = 18.sp
            )
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(R.string.close),
                tint = colorResource(R.color.text_primary),
                modifier = Modifier
                    .size(24.dp)
                    .clickable(onClick = onClose)
            )
        }

        // Search bar
        SearchBar(
            query = searchQuery,
            onQueryChange = { query -> appListViewModel.searchApps(query) }
        )

        // App list
        when (appsState) {
            is com.luminaos.launcher.viewmodel.AppListState.Loading -> {
                LoadingIndicator()
            }
            is com.luminaos.launcher.viewmodel.AppListState.Empty -> {
                EmptyState(message = stringResource(R.string.no_apps_found))
            }
            is com.luminaos.launcher.viewmodel.AppListState.Error -> {
                val errorState = appsState as com.luminaos.launcher.viewmodel.AppListState.Error
                EmptyState(message = errorState.message)
            }
            is com.luminaos.launcher.viewmodel.AppListState.Success -> {
                val successState = appsState as com.luminaos.launcher.viewmodel.AppListState.Success
                AppGridList(
                    apps = successState.apps,
                    onAppClick = onAppClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(8.dp)
                )
            }
        }
    }
}

/**
 * Grid list for displaying apps.
 */
@Composable
fun AppGridList(
    apps: List<AppInfo>,
    onAppClick: (AppInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(apps.chunked(4)) { rowApps ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowApps.forEach { app ->
                    AppIcon(
                        label = app.getDisplayName(),
                        onClick = { onAppClick(app) },
                        modifier = Modifier.weight(1f)
                    )
                }
                // Fill remaining space if row is not full
                repeat(4 - rowApps.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

/**
 * App drawer wrapper with ViewModel initialization.
 */
@Composable
fun AppDrawerScreenWrapper(
    onClose: () -> Unit,
    context: android.content.Context,
    modifier: Modifier = Modifier
) {
    val logger = remember { Logger("AppDrawer") }
    val appListViewModel: AppListViewModel = viewModel()
    val drawerViewModel: AppDrawerViewModel = viewModel()

    // Initialize app list on first composition
    LaunchedEffect(Unit) {
        appListViewModel.loadApps()
    }

    val handleAppClick = { app: AppInfo ->
        logger.d("App clicked: ${app.appName}")
        LauncherUtils.launchApp(context, app.packageName, app.activityName)
        onClose()
    }

    AppDrawerScreen(
        appListViewModel = appListViewModel,
        drawerViewModel = drawerViewModel,
        onClose = onClose,
        onAppClick = handleAppClick,
        modifier = modifier
    )
}
