package com.luminaos.launcher.data

import android.graphics.drawable.Drawable
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class representing an installed application.
 * Contains all essential information about an app including name, package name, and visual properties.
 */
@Entity(tableName = "apps")
data class AppInfo(
    @PrimaryKey
    val packageName: String,
    val appName: String,
    val activityName: String,
    val isSystemApp: Boolean = false,
    val installTime: Long = System.currentTimeMillis(),
    val updateTime: Long = System.currentTimeMillis(),
    val isHidden: Boolean = false,
    val customLabel: String? = null,
    val customIconPath: String? = null
) {
    /**
     * Returns the display name for this app.
     * Uses custom label if available, otherwise uses app name.
     */
    fun getDisplayName(): String = customLabel ?: appName

    /**
     * Generates a unique identifier for this app.
     */
    fun getAppId(): String = "$packageName/$activityName"
}

/**
 * Data class for app drawer state management.
 */
data class AppDrawerState(
    val isOpen: Boolean = false,
    val searchQuery: String = "",
    val filteredApps: List<AppInfo> = emptyList(),
    val isLoading: Boolean = false
)

/**
 * Data class representing a widget instance.
 */
@Entity(tableName = "widgets")
data class WidgetItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val appWidgetId: Int,
    val packageName: String,
    val className: String,
    val gridX: Int,
    val gridY: Int,
    val gridWidth: Int,
    val gridHeight: Int,
    val label: String = "",
    val addedTime: Long = System.currentTimeMillis()
)

/**
 * Data class for home screen shortcuts.
 */
@Entity(tableName = "shortcuts")
data class ShortcutItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val packageName: String,
    val activityName: String,
    val gridX: Int,
    val gridY: Int,
    val gridWidth: Int = 1,
    val gridHeight: Int = 1,
    val label: String = "",
    val addedTime: Long = System.currentTimeMillis()
)

/**
 * Data class for folder items on home screen.
 */
@Entity(tableName = "folders")
data class FolderItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val gridX: Int,
    val gridY: Int,
    val gridWidth: Int = 1,
    val gridHeight: Int = 1,
    val icon: String = "",
    val color: Int = 0xFF6200EE.toInt(),
    val createdTime: Long = System.currentTimeMillis()
)

/**
 * Data class representing an app in a folder.
 */
@Entity(tableName = "folder_apps")
data class FolderApp(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val folderId: Int,
    val appPackageName: String,
    val position: Int
)

/**
 * Data class for theme configuration.
 */
data class ThemeConfig(
    val name: String,
    val isDark: Boolean = true,
    val primaryColor: Int,
    val secondaryColor: Int,
    val accentColor: Int,
    val fontFamily: String = "default"
)

/**
 * Data class for grid configuration.
 */
data class GridConfig(
    val columnsCount: Int = 4,
    val rowsCount: Int = 5,
    val cellSize: Int = 100
)

/**
 * Data class containing recent app history.
 */
data class RecentApp(
    val packageName: String,
    val appName: String,
    val activityName: String,
    val lastUsedTime: Long = System.currentTimeMillis()
)
