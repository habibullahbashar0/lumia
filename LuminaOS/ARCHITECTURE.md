# LuminaOS Architecture Guide

## Overview

LuminaOS follows a clean architecture pattern with clear separation between data, business logic, and UI layers. This document provides a detailed explanation of the architectural decisions and component interactions.

## Architecture Layers

### 1. Data Layer (Repository Pattern)

**Location**: `com.luminaos.launcher.data`

**Components**:
- **AppInfo.kt**: Data models and entities
  - `AppInfo`: Entity representing installed app
  - `WidgetItem`: Widget configuration
  - `ShortcutItem`: Shortcut data
  - `FolderItem`: Folder data

- **LauncherDatabase.kt**: Room database setup
  - Singleton instance management
  - Database initialization with proper threading

- **Dao.kt**: Data Access Objects
  - `AppInfoDao`: CRUD operations for apps
  - `WidgetDao`: Widget persistence
  - `ShortcutDao`: Shortcut persistence
  - `FolderDao`: Folder persistence

- **AppRepository.kt**: Repository pattern implementation
  - Abstracts database and PackageManager access
  - Handles data transformation
  - Manages data synchronization

**Design Decisions**:
- Using Room for type-safe database access
- Repository pattern hides database implementation details
- Coroutines for non-blocking database operations
- Flow<> for reactive data updates

### 2. ViewModel Layer (State Management)

**Location**: `com.luminaos.launcher.viewmodel`

**ViewModels**:

#### AppListViewModel
- Manages list of installed apps
- Handles search functionality
- Observable state through StateFlow
- Lifecycle-aware data management

#### HomeScreenViewModel
- Manages home screen state
- Handles grid layout configuration
- Drag-and-drop state management
- Recent apps tracking

#### AppDrawerViewModel
- Manages app drawer visibility
- Search query state
- Drawer animation states

**Design Decisions**:
- Using StateFlow for observable state
- ViewModels tied to Activities/Fragments lifecycle
- Repository dependency for data access
- Immutable state objects

### 3. UI Layer (Jetpack Compose)

**Location**: `com.luminaos.launcher.ui`

**Components** (`components/`):
- **LauncherComponents.kt**: Reusable Compose components
  - `AppIcon`: Individual app display
  - `SearchBar`: Search input
  - `LoadingIndicator`: Progress UI
  - `EmptyState`: Empty placeholder

**Screens** (`screens/`):
- **HomeScreen.kt**: Primary home screen
- **AppDrawerScreen.kt**: App list drawer
- **SettingsScreen.kt**: Settings interface
- **RecentAppsScreen.kt**: Recent apps view
- **AppInfoActivity.kt**: App details

**Design Decisions**:
- Declarative UI with Compose
- Reactive data binding through collectAsState()
- Modular, testable composables
- Clear separation of concerns

### 4. Service Layer (Background Operations)

**Location**: `com.luminaos.launcher.service`

**Components**:

#### AppMonitorService
- Starts on boot
- BroadcastReceiver for package events
- Updates database on app changes
- Runs as foreground service on Android O+

#### BootReceiver
- Handles BOOT_COMPLETED intent
- Listens for package events
- Delegates to AppRepository

#### WallpaperService
- Placeholder for wallpaper functionality
- Future enhancement point

**Design Decisions**:
- Service pattern for background work
- BroadcastReceiver for system events
- Coroutines for async operations
- Proper lifecycle management

### 5. Utility Layer

**Location**: `com.luminaos.launcher.utils`

**Components**:
- **Logger.kt**: Centralized logging
- **LauncherUtils.kt**: Common helper functions
  - System utilities
  - Gesture utilities
  - Animation constants
  - Unit conversion utilities

## Data Flow

### App Loading Flow
```
MainActivity
    ↓
AppListViewModel.loadApps()
    ↓
AppRepository.loadInstalledApps()
    ↓
PackageManager.getInstalledPackages()
    ↓
AppInfoDao.insertApps()
    ↓
Database storage + StateFlow emission
    ↓
UI recomposes with new data
```

### App Launch Flow
```
User clicks App Icon
    ↓
AppDrawerScreen.onAppClick()
    ↓
LauncherUtils.launchApp()
    ↓
PackageManager.getLaunchIntentForPackage()
    ↓
Context.startActivity()
    ↓
Target app launches
```

### Package Installation Flow
```
System broadcasts PACKAGE_ADDED
    ↓
BootReceiver.onReceive()
    ↓
AppRepository.onAppInstalled()
    ↓
AppInfoDao.insertApp()
    ↓
Database updated + StateFlow emitted
    ↓
UI updates automatically
```

## Threading Model

- **Main Thread**: UI rendering and composition
- **IO Thread**: Database operations (Dispatchers.IO)
- **Default Thread**: CPU-intensive operations
- **Main Thread**: State updates from coroutines

Coroutine usage:
```kotlin
viewModelScope.launch {
    withContext(Dispatchers.IO) {
        // Database operation
        repository.loadInstalledApps()
    }
    // Back to Main dispatcher automatically
    _appsState.value = AppListState.Success(apps)
}
```

## State Management Strategy

### Reactive State with StateFlow
```
@Composable
fun AppDrawer() {
    val apps by viewModel.apps.collectAsState()
    // Recomposes when apps StateFlow emits
}
```

### Immutable State Objects
```
sealed class AppListState {
    object Loading : AppListState()
    data class Success(val apps: List<AppInfo>) : AppListState()
    data class Error(val message: String) : AppListState()
}
```

## Composition and Activity Structure

### MainActivity
- Single Activity pattern
- Navigation via Compose state
- AnimatedVisibility for screen transitions
- BackHandler for system back button

```kotlin
Box(modifier = Modifier.fillMaxSize()) {
    HomeScreenWrapper()
    
    AnimatedVisibility(visible = showAppDrawer) {
        AppDrawerScreenWrapper()
    }
    
    AnimatedVisibility(visible = showSettings) {
        SettingsScreen()
    }
}
```

## Database Schema

### Apps Table
```sql
CREATE TABLE apps (
    packageName TEXT PRIMARY KEY,
    appName TEXT,
    activityName TEXT,
    isSystemApp INTEGER,
    installTime INTEGER,
    updateTime INTEGER,
    isHidden INTEGER,
    customLabel TEXT,
    customIconPath TEXT
)
```

### Widgets Table
```sql
CREATE TABLE widgets (
    id INTEGER PRIMARY KEY,
    appWidgetId INTEGER,
    packageName TEXT,
    className TEXT,
    gridX INTEGER, gridY INTEGER,
    gridWidth INTEGER, gridHeight INTEGER,
    addedTime INTEGER
)
```

### Shortcuts Table
```sql
CREATE TABLE shortcuts (
    id INTEGER PRIMARY KEY,
    title TEXT,
    packageName TEXT,
    activityName TEXT,
    gridX INTEGER, gridY INTEGER,
    gridWidth INTEGER, gridHeight INTEGER,
    addedTime INTEGER
)
```

## Lifecycle Management

### Activity Lifecycle
```
onCreate()
  ├─ Initialize Compose UI
  ├─ Start AppMonitorService
  └─ Load initial app list

onResume()
  └─ Refresh app list if needed

onPause()
  └─ Pause non-critical operations

onDestroy()
  └─ Clean up services
```

### ViewModel Lifecycle
```
Created when Fragment/Activity is created
  ↓
Repository initialized
  ↓
Data flow starts emitting
  ↓
Cleared when Activity destroyed (ViewModelScope canceled)
```

## Error Handling

### Database Operations
```kotlin
try {
    repository.loadInstalledApps()
} catch (e: Exception) {
    _appsState.value = AppListState.Error(e.localizedMessage)
    logger.e("Error loading apps: ${e.message}")
}
```

### Service Operations
```kotlin
try {
    startForegroundService(serviceIntent)
} catch (e: Exception) {
    logger.e("Error starting service: ${e.message}")
}
```

## Performance Optimizations

1. **Database Indexing**: Indexed queries for fast lookups
2. **Lazy Loading**: Compose LazyGrid for efficient rendering
3. **Coroutines**: Non-blocking operations
4. **Resource Cleanup**: Proper lifecycle management
5. **Memory**: Flow collection cleanup on lifecycle end

## Testing Strategy

### Unit Tests
- ViewModels with mock repositories
- Utility function validation
- State transition testing

### Integration Tests
- Activity launch verification
- UI interaction testing
- Full component integration

```kotlin
@RunWith(AndroidJUnit4::class)
class AppListViewModelTest {
    @Test
    fun testAppLoadingAndSearch() {
        // Test implementation
    }
}
```

## Future Architecture Improvements

1. **Dependency Injection**: Hilt integration for better testability
2. **Modularization**: Separate feature modules
3. **Navigation**: Jetpack Navigation Compose
4. **Data Layer**: Enhancement with local + remote sync
5. **Performance**: Multi-threading optimization

---

For implementation details, refer to inline code documentation throughout the codebase.
