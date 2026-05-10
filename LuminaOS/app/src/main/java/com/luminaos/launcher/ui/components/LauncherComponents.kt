package com.luminaos.launcher.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apps
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontSize
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luminaos.launcher.R

/**
 * Floating action button component for quick access to app drawer.
 */
@Composable
fun FloatingActionButton(
    onClick: () -> Unit,
    onLongClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {
        Icon(
            imageVector = Icons.Default.Apps,
            contentDescription = stringResource(R.string.app_drawer),
            tint = Color.White
        )
    }
) {
    Box(
        modifier = modifier
            .size(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(colorResource(R.color.primary_color))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

/**
 * App icon item component for grid layout.
 */
@Composable
fun AppIcon(
    label: String,
    onClick: () -> Unit,
    onLongClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(colorResource(R.color.app_icon_background)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Apps,
                contentDescription = label,
                tint = colorResource(R.color.accent_color),
                modifier = Modifier.size(28.dp)
            )
        }
        Text(
            text = label,
            fontSize = 12.sp,
            color = colorResource(R.color.text_primary),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 4.dp)
                .widthIn(max = 60.dp),
            maxLines = 2
        )
    }
}

/**
 * Search bar component for app drawer.
 */
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(colorResource(R.color.search_bar_bg), RoundedCornerShape(24.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Box(modifier = Modifier.weight(1f)) {
            if (query.isEmpty()) {
                Text(
                    text = stringResource(R.string.search_hint),
                    color = colorResource(R.color.text_secondary),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            androidx.compose.material3.TextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = androidx.compose.material3.TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                textStyle = androidx.compose.ui.text.TextStyle(
                    color = colorResource(R.color.text_primary),
                    fontSize = 14.sp
                )
            )
        }

        if (query.isNotEmpty()) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(R.string.close),
                tint = colorResource(R.color.text_secondary),
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .clickable { onQueryChange("") }
                    .size(20.dp)
            )
        }
    }
}

/**
 * Settings button component.
 */
@Composable
fun SettingsButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(colorResource(R.color.app_icon_background))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = stringResource(R.string.settings),
            tint = colorResource(R.color.accent_color),
            modifier = Modifier.size(24.dp)
        )
    }
}

/**
 * Quick settings bar component.
 */
@Composable
fun QuickSettingsBar(
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(colorResource(R.color.surface_color))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.app_name),
            color = colorResource(R.color.text_primary),
            fontSize = 14.sp,
            modifier = Modifier.weight(1f)
        )
        SettingsButton(onSettingsClick = onSettingsClick)
    }
}

/**
 * Loading indicator component.
 */
@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.background_color)),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.material3.CircularProgressIndicator(
            color = colorResource(R.color.primary_color)
        )
    }
}

/**
 * Empty state component.
 */
@Composable
fun EmptyState(
    message: String = stringResource(R.string.no_apps_found),
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.background_color)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Apps,
                contentDescription = null,
                tint = colorResource(R.color.text_secondary),
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                color = colorResource(R.color.text_secondary),
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * App menu item component for long-press actions.
 */
@Composable
fun AppMenuItemGrid(
    items: List<String>,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        items.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clickable { onItemClick(item) }
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item,
                    color = colorResource(R.color.text_primary),
                    fontSize = 14.sp
                )
            }
        }
    }
}
