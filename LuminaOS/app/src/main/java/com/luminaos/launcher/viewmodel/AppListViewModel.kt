package com.luminaos.launcher.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.luminaos.launcher.data.AppInfo
import com.luminaos.launcher.data.AppRepository
import com.luminaos.launcher.data.LauncherDatabase
import com.luminaos.launcher.utils.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for managing app list state.
 * Handles loading, searching, and managing app data.
 */
class AppListViewModel(application: Application) : AndroidViewModel(application) {
    private val logger = Logger("AppListViewModel")
    private val database = LauncherDatabase.getInstance(application)
    private val repository = AppRepository(application, database.appInfoDao())

    // State flow for app list
    private val _appsState = MutableStateFlow<AppListState>(AppListState.Loading)
    val appsState: StateFlow<AppListState> = _appsState.asStateFlow()

    // State flow for search
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        loadApps()
    }

    /**
     * Load all apps.
     */
    fun loadApps() {
        viewModelScope.launch {
            try {
                _appsState.value = AppListState.Loading
                repository.loadInstalledApps()
                logger.d("Apps loaded successfully")
            } catch (e: Exception) {
                logger.e("Error loading apps: ${e.message}")
                _appsState.value = AppListState.Error(e.localizedMessage ?: "Unknown error")
            }
        }

        // Collect apps from repository
        viewModelScope.launch {
            repository.getVisibleAppsFlow().collect { apps ->
                _appsState.value = if (apps.isEmpty()) {
                    AppListState.Empty
                } else {
                    AppListState.Success(apps)
                }
            }
        }
    }

    /**
     * Search apps by query.
     */
    fun searchApps(query: String) {
        _searchQuery.value = query
        viewModelScope.launch {
            repository.searchAppsFlow(query).collect { apps ->
                _appsState.value = if (query.isBlank()) {
                    AppListState.Success(apps)
                } else if (apps.isEmpty()) {
                    AppListState.Empty
                } else {
                    AppListState.Success(apps)
                }
            }
        }
    }

    /**
     * Hide an app.
     */
    fun hideApp(packageName: String) {
        viewModelScope.launch {
            try {
                repository.hideApp(packageName)
                logger.d("App hidden: $packageName")
            } catch (e: Exception) {
                logger.e("Error hiding app: ${e.message}")
            }
        }
    }

    /**
     * Show a hidden app.
     */
    fun showApp(packageName: String) {
        viewModelScope.launch {
            try {
                repository.showApp(packageName)
                logger.d("App shown: $packageName")
            } catch (e: Exception) {
                logger.e("Error showing app: ${e.message}")
            }
        }
    }

    /**
     * Clear search query.
     */
    fun clearSearch() {
        _searchQuery.value = ""
    }
}

/**
 * Sealed class representing app list state.
 */
sealed class AppListState {
    object Loading : AppListState()
    object Empty : AppListState()
    data class Success(val apps: List<AppInfo>) : AppListState()
    data class Error(val message: String) : AppListState()
}
