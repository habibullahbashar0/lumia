package com.luminaos.launcher.data

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.luminaos.launcher.utils.Logger

/**
 * Content Provider for exposing app information to other apps.
 * Allows external apps to query available applications.
 */
class AppProvider : ContentProvider() {
    private val logger = Logger("AppProvider")

    companion object {
        const val AUTHORITY = "com.luminaos.launcher.provider"
        private const val APPS = 1
        private const val APPS_ID = 2
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, "apps", APPS)
            addURI(AUTHORITY, "apps/#", APPS_ID)
        }
    }

    override fun onCreate(): Boolean {
        logger.d("AppProvider created")
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        logger.d("Query: $uri")
        // Return null for now - would need actual implementation with database
        return null
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            APPS -> "vnd.android.cursor.dir/vnd.luminaos.app"
            APPS_ID -> "vnd.android.cursor.item/vnd.luminaos.app"
            else -> null
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        logger.d("Insert: $uri")
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        logger.d("Delete: $uri")
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        logger.d("Update: $uri")
        return 0
    }
}

/**
 * Content Provider for shortcuts.
 * Provides access to launcher shortcuts.
 */
class ShortcutProvider : ContentProvider() {
    private val logger = Logger("ShortcutProvider")

    companion object {
        const val AUTHORITY = "com.luminaos.launcher.shortcuts"
        private const val SHORTCUTS = 1
        private const val SHORTCUTS_ID = 2
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI(AUTHORITY, "shortcuts", SHORTCUTS)
            addURI(AUTHORITY, "shortcuts/#", SHORTCUTS_ID)
        }
    }

    override fun onCreate(): Boolean {
        logger.d("ShortcutProvider created")
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        logger.d("Query: $uri")
        return null
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher.match(uri)) {
            SHORTCUTS -> "vnd.android.cursor.dir/vnd.luminaos.shortcut"
            SHORTCUTS_ID -> "vnd.android.cursor.item/vnd.luminaos.shortcut"
            else -> null
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        logger.d("Insert: $uri")
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        logger.d("Delete: $uri")
        return 0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        logger.d("Update: $uri")
        return 0
    }
}
