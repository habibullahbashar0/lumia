package com.luminaos.launcher.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.luminaos.launcher.data.AppInfo
import com.luminaos.launcher.utils.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for managing app drawer state.
 * Handles app drawer visibility, search, and filtering.
 */
class AppDrawerViewModel(application: Application) : AndroidViewModel(application) {
    private val logger = Logger("AppDrawerViewModel")

    // State flows
    private val _isOpen = MutableStateFlow(false)
    val isOpen: StateFlow<Boolean> = _isOpen.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _drawerState = MutableStateFlow<AppDrawerState>(AppDrawerState.Collapsed)
    val drawerState: StateFlow<AppDrawerState> = _drawerState.asStateFlow()

    /**
     * Open app drawer.
     */
    fun openDrawer() {
        viewModelScope.launch {
            _isOpen.value = true
            _drawerState.value = AppDrawerState.Expanded
            logger.d("App drawer opened")
        }
    }

    /**
     * Close app drawer.
     */
    fun closeDrawer() {
        viewModelScope.launch {
            _isOpen.value = false
            _drawerState.value = AppDrawerState.Collapsed
            _searchQuery.value = ""
            logger.d("App drawer closed")
        }
    }

    /**
     * Toggle app drawer.
     */
    fun toggleDrawer() {
        if (_isOpen.value) {
            closeDrawer()
        } else {
            openDrawer()
        }
    }

    /**
     * Update search query.
     */
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    /**
     * Peek app drawer.
     */
    fun peekDrawer() {
        _drawerState.value = AppDrawerState.Peeked
    }
}

/**
 * Enum representing app drawer states.
 */
enum class AppDrawerState {
    Collapsed, Peeked, Expanded
}
