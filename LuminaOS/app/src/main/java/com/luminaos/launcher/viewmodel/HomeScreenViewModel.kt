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
 * ViewModel for managing home screen state.
 * Handles grid layout, gestures, and drag-and-drop functionality.
 */
class HomeScreenViewModel(application: Application) : AndroidViewModel(application) {
    private val logger = Logger("HomeScreenViewModel")
    private val database = LauncherDatabase.getInstance(application)
    private val repository = AppRepository(application, database.appInfoDao())

    // State flows
    private val _gridColumns = MutableStateFlow(4)
    val gridColumns: StateFlow<Int> = _gridColumns.asStateFlow()

    private val _gridRows = MutableStateFlow(6)
    val gridRows: StateFlow<Int> = _gridRows.asStateFlow()

    private val _homeScreenState = MutableStateFlow<HomeScreenState>(HomeScreenState.Idle)
    val homeScreenState: StateFlow<HomeScreenState> = _homeScreenState.asStateFlow()

    private val _recentApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val recentApps: StateFlow<List<AppInfo>> = _recentApps.asStateFlow()

    init {
        loadRecentApps()
    }

    /**
     * Load recent apps.
     */
    private fun loadRecentApps() {
        viewModelScope.launch {
            try {
                val apps = repository.getRecentApps(10)
                _recentApps.value = apps
                logger.d("Loaded ${apps.size} recent apps")
            } catch (e: Exception) {
                logger.e("Error loading recent apps: ${e.message}")
            }
        }
    }

    /**
     * Update grid layout.
     */
    fun updateGridLayout(columns: Int, rows: Int) {
        _gridColumns.value = columns
        _gridRows.value = rows
        logger.d("Grid layout updated: $columns x $rows")
    }

    /**
     * Start drag operation.
     */
    fun startDrag(appInfo: AppInfo) {
        _homeScreenState.value = HomeScreenState.Dragging(appInfo)
    }

    /**
     * Drop app to home screen.
     */
    fun dropApp(x: Int, y: Int) {
        if (_homeScreenState.value is HomeScreenState.Dragging) {
            _homeScreenState.value = HomeScreenState.Idle
            logger.d("App dropped at ($x, $y)")
        }
    }

    /**
     * Cancel drag operation.
     */
    fun cancelDrag() {
        _homeScreenState.value = HomeScreenState.Idle
    }

    /**
     * Show home screen options.
     */
    fun showOptions() {
        _homeScreenState.value = HomeScreenState.ShowingOptions
    }

    /**
     * Hide home screen options.
     */
    fun hideOptions() {
        _homeScreenState.value = HomeScreenState.Idle
    }
}

/**
 * Sealed class representing home screen state.
 */
sealed class HomeScreenState {
    object Idle : HomeScreenState()
    object ShowingOptions : HomeScreenState()
    data class Dragging(val appInfo: AppInfo) : HomeScreenState()
    data class Error(val message: String) : HomeScreenState()
}
