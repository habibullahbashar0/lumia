package com.luminaos.launcher.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for app information.
 * Provides database operations for AppInfo entities.
 */
@Dao
interface AppInfoDao {
    /**
     * Retrieve all apps from the database.
     */
    @Query("SELECT * FROM apps ORDER BY appName ASC")
    fun getAllApps(): Flow<List<AppInfo>>

    /**
     * Retrieve all non-hidden apps.
     */
    @Query("SELECT * FROM apps WHERE isHidden = 0 ORDER BY appName ASC")
    fun getVisibleApps(): Flow<List<AppInfo>>

    /**
     * Search apps by name.
     */
    @Query("SELECT * FROM apps WHERE appName LIKE '%' || :query || '%' AND isHidden = 0 ORDER BY appName ASC")
    fun searchApps(query: String): Flow<List<AppInfo>>

    /**
     * Get app by package name.
     */
    @Query("SELECT * FROM apps WHERE packageName = :packageName")
    suspend fun getAppByPackage(packageName: String): AppInfo?

    /**
     * Insert a new app.
     */
    @Insert
    suspend fun insertApp(app: AppInfo)

    /**
     * Insert multiple apps.
     */
    @Insert
    suspend fun insertApps(apps: List<AppInfo>)

    /**
     * Update app information.
     */
    @Update
    suspend fun updateApp(app: AppInfo)

    /**
     * Delete an app record.
     */
    @Delete
    suspend fun deleteApp(app: AppInfo)

    /**
     * Delete app by package name.
     */
    @Query("DELETE FROM apps WHERE packageName = :packageName")
    suspend fun deleteAppByPackage(packageName: String)

    /**
     * Get system apps only.
     */
    @Query("SELECT * FROM apps WHERE isSystemApp = 1 ORDER BY appName ASC")
    suspend fun getSystemApps(): List<AppInfo>

    /**
     * Get recently added apps.
     */
    @Query("SELECT * FROM apps WHERE isHidden = 0 ORDER BY installTime DESC LIMIT :limit")
    suspend fun getRecentApps(limit: Int = 20): List<AppInfo>

    /**
     * Hide an app by package name.
     */
    @Query("UPDATE apps SET isHidden = 1 WHERE packageName = :packageName")
    suspend fun hideApp(packageName: String)

    /**
     * Show an app by package name.
     */
    @Query("UPDATE apps SET isHidden = 0 WHERE packageName = :packageName")
    suspend fun showApp(packageName: String)

    /**
     * Clear all apps from database.
     */
    @Query("DELETE FROM apps")
    suspend fun clearAllApps()

    /**
     * Get count of all apps.
     */
    @Query("SELECT COUNT(*) FROM apps")
    suspend fun getAppCount(): Int
}

/**
 * Data Access Object (DAO) for widget items.
 */
@Dao
interface WidgetDao {
    /**
     * Get all widgets.
     */
    @Query("SELECT * FROM widgets ORDER BY gridY, gridX ASC")
    fun getAllWidgets(): Flow<List<WidgetItem>>

    /**
     * Insert a widget.
     */
    @Insert
    suspend fun insertWidget(widget: WidgetItem): Long

    /**
     * Update a widget.
     */
    @Update
    suspend fun updateWidget(widget: WidgetItem)

    /**
     * Delete a widget.
     */
    @Delete
    suspend fun deleteWidget(widget: WidgetItem)

    /**
     * Delete widget by ID.
     */
    @Query("DELETE FROM widgets WHERE id = :id")
    suspend fun deleteWidgetById(id: Int)

    /**
     * Get widget by app widget ID.
     */
    @Query("SELECT * FROM widgets WHERE appWidgetId = :appWidgetId")
    suspend fun getWidgetByAppWidgetId(appWidgetId: Int): WidgetItem?
}

/**
 * Data Access Object (DAO) for shortcut items.
 */
@Dao
interface ShortcutDao {
    /**
     * Get all shortcuts.
     */
    @Query("SELECT * FROM shortcuts ORDER BY gridY, gridX ASC")
    fun getAllShortcuts(): Flow<List<ShortcutItem>>

    /**
     * Insert a shortcut.
     */
    @Insert
    suspend fun insertShortcut(shortcut: ShortcutItem): Long

    /**
     * Update a shortcut.
     */
    @Update
    suspend fun updateShortcut(shortcut: ShortcutItem)

    /**
     * Delete a shortcut.
     */
    @Delete
    suspend fun deleteShortcut(shortcut: ShortcutItem)

    /**
     * Delete shortcut by ID.
     */
    @Query("DELETE FROM shortcuts WHERE id = :id")
    suspend fun deleteShortcutById(id: Int)

    /**
     * Get shortcuts by package name.
     */
    @Query("SELECT * FROM shortcuts WHERE packageName = :packageName")
    suspend fun getShortcutsByPackage(packageName: String): List<ShortcutItem>
}

/**
 * Data Access Object (DAO) for folder items.
 */
@Dao
interface FolderDao {
    /**
     * Get all folders.
     */
    @Query("SELECT * FROM folders ORDER BY gridY, gridX ASC")
    fun getAllFolders(): Flow<List<FolderItem>>

    /**
     * Insert a folder.
     */
    @Insert
    suspend fun insertFolder(folder: FolderItem): Long

    /**
     * Update a folder.
     */
    @Update
    suspend fun updateFolder(folder: FolderItem)

    /**
     * Delete a folder.
     */
    @Delete
    suspend fun deleteFolder(folder: FolderItem)

    /**
     * Delete folder by ID.
     */
    @Query("DELETE FROM folders WHERE id = :id")
    suspend fun deleteFolderById(id: Int)
}

/**
 * Data Access Object (DAO) for folder apps.
 */
@Dao
interface FolderAppDao {
    /**
     * Get all apps in a folder.
     */
    @Query("SELECT * FROM folder_apps WHERE folderId = :folderId ORDER BY position ASC")
    suspend fun getAppsInFolder(folderId: Int): List<FolderApp>

    /**
     * Insert an app into a folder.
     */
    @Insert
    suspend fun insertFolderApp(folderApp: FolderApp)

    /**
     * Delete an app from a folder.
     */
    @Delete
    suspend fun deleteFolderApp(folderApp: FolderApp)

    /**
     * Delete all apps from a folder.
     */
    @Query("DELETE FROM folder_apps WHERE folderId = :folderId")
    suspend fun deleteAllAppsFromFolder(folderId: Int)
}
