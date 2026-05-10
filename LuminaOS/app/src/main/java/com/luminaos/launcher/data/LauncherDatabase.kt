package com.luminaos.launcher.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Room database for LuminaOS launcher.
 * Manages persistence of app information, widgets, shortcuts, and folders.
 */
@Database(
    entities = [
        AppInfo::class,
        WidgetItem::class,
        ShortcutItem::class,
        FolderItem::class,
        FolderApp::class
    ],
    version = 1,
    exportSchema = true
)
abstract class LauncherDatabase : RoomDatabase() {
    abstract fun appInfoDao(): AppInfoDao
    abstract fun widgetDao(): WidgetDao
    abstract fun shortcutDao(): ShortcutDao
    abstract fun folderDao(): FolderDao
    abstract fun folderAppDao(): FolderAppDao

    companion object {
        @Volatile
        private var INSTANCE: LauncherDatabase? = null

        /**
         * Get or create the database instance.
         * Uses double-checked locking pattern for thread safety.
         */
        fun getInstance(context: Context): LauncherDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LauncherDatabase::class.java,
                    "lumina_launcher_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
