package com.luminaos.launcher.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.luminaos.launcher.R
import com.luminaos.launcher.utils.Logger

/**
 * Recent apps screen composable.
 */
@Composable
fun RecentAppsScreen(
    onClose: () -> Unit,
    onAppClick: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val logger = remember { Logger("RecentAppsScreen") }

    BackHandler(onBack = onClose)

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
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.recent_apps),
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

        // Recent apps list placeholder
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.empty_state),
                color = colorResource(R.color.text_secondary),
                fontSize = 14.sp
            )
        }
    }
}
