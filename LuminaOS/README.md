# LuminaOS Launcher

A fully featured, production-ready Android launcher application built with Kotlin and Jetpack Compose. LuminaOS is designed to be lightweight, customizable, and user-friendly, featuring modern launcher capabilities including app drawer with search, home screen management, gesture navigation, and more.

## Features

- **Home Screen**: Customizable grid layout with draggable app shortcuts
- **App Drawer**: Full app list with search functionality
- **Gesture Navigation**: Swipe up to open app drawer, intuitive navigation
- **Icon Management**: Support for custom app labels and organization
- **Quick Settings**: Quick access to common launcher settings
- **Recent Apps**: View and launch recently used applications
- **Dark Theme**: Material Design dark theme for optimal viewing
- **Lightweight**: Optimized for devices with varying RAM and processing power
- **Default Launcher**: Full support for setting as default launcher

## Technical Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM with Jetpack components
- **Database**: Room for local persistence
- **Build System**: Gradle with Kotlin DSL
- **Minimum API Level**: 28
- **Target API Level**: 34

## Project Structure

```
LuminaOS/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/luminaos/launcher/
│   │   │   │   ├── MainActivity.kt              # Main activity
│   │   │   │   ├── data/                         # Data layer
│   │   │   │   │   ├── AppInfo.kt               # Data models
│   │   │   │   │   ├── Dao.kt                   # Room DAOs
│   │   │   │   │   ├── LauncherDatabase.kt      # Room database
│   │   │   │   │   ├── AppRepository.kt         # App data repository
│   │   │   │   │   └── Providers.kt             # Content providers
│   │   │   │   ├── viewmodel/                    # UI ViewModels
│   │   │   │   │   ├── AppListViewModel.kt      # App list state
│   │   │   │   │   ├── HomeScreenViewModel.kt   # Home screen state
│   │   │   │   │   └── AppDrawerViewModel.kt    # Drawer state
│   │   │   │   ├── service/                      # Background services
│   │   │   │   │   ├── AppMonitorService.kt     # App installation monitoring
│   │   │   │   │   ├── BootReceiver.kt          # Boot completion handler
│   │   │   │   │   └── WallpaperService.kt      # Wallpaper handling
│   │   │   │   ├── ui/                           # UI components
│   │   │   │   │   ├── components/
│   │   │   │   │   │   └── LauncherComponents.kt # Reusable Compose components
│   │   │   │   │   └── screens/
│   │   │   │   │       ├── HomeScreen.kt         # Home screen UI
│   │   │   │   │       ├── AppDrawerScreen.kt    # App drawer UI
│   │   │   │   │       ├── SettingsScreen.kt     # Settings UI
│   │   │   │   │       ├── RecentAppsScreen.kt   # Recent apps UI
│   │   │   │   │       └── AppInfoActivity.kt    # App info activity
│   │   │   │   └── utils/                        # Utility functions
│   │   │   │       ├── Logger.kt                 # Logging utility
│   │   │   │       └── LauncherUtils.kt          # Common utilities
│   │   │   ├── res/
│   │   │   │   ├── values/
│   │   │   │   │   ├── strings.xml               # String resources
│   │   │   │   │   ├── colors.xml                # Color definitions
│   │   │   │   │   └── styles.xml                # Theme styles
│   │   │   │   ├── drawable/                     # Drawable assets
│   │   │   │   └── xml/                          # XML configuration
│   │   │   └── AndroidManifest.xml               # App manifest
│   │   ├── test/
│   │   │   └── java/com/luminaos/launcher/       # Unit tests
│   │   └── androidTest/
│   │       └── java/com/luminaos/launcher/       # Integration tests
│   ├── build.gradle.kts                          # App build configuration
│   └── proguard-rules.pro                        # ProGuard configuration
├── build.gradle.kts                              # Root build configuration
├── settings.gradle.kts                           # Settings configuration
└── README.md                                      # This file
```

## Building the Project

### Prerequisites

- Android Studio 2022.1 or higher
- Java 17 or higher
- Android SDK API level 34
- Gradle 8.0 or higher

### Build Instructions

1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   cd LuminaOS
   ```

2. **Open in Android Studio:**
   - Open Android Studio
   - Select "Open an Existing Project"
   - Navigate to the LuminaOS directory

3. **Configure SDK:**
   - Go to File > Project Structure
   - Select SDK and API level (minimum API 28)
   - Ensure Build Tools version is compatible

4. **Build the APK:**
   ```bash
   ./gradlew build
   ```

5. **Generate Release APK:**
   ```bash
   ./gradlew assembleRelease
   ```

6. **Install on Device:**
   ```bash
   ./gradlew installDebug
   ```

## Running the App

### On Emulator:
```bash
./gradlew runDebug
```

### On Physical Device:
1. Enable USB debugging on your Android device
2. Connect device via USB
3. Run: `./gradlew installDebug`
4. Or use Android Studio's Run button

### Set as Default Launcher:
1. Open LuminaOS app
2. Go to Settings
3. Tap "Set as Default Launcher"
4. Select LuminaOS from the chooser dialog

## Architecture

LuminaOS follows the MVVM (Model-View-ViewModel) architecture pattern with clear separation of concerns:

### Data Layer
- **Models**: Data classes define the structure of app information, widgets, shortcuts
- **Room Database**: Local storage with DAOs for database operations
- **Repository**: Abstraction layer managing data access, syncing with PackageManager

### ViewModel Layer
- **AppListViewModel**: Manages app list state, search, and filtering
- **HomeScreenViewModel**: Manages home screen layout and gestures
- **AppDrawerViewModel**: Manages app drawer state and visibility

### UI Layer
- **Compose Screens**: Declarative UI components for each screen
- **Components**: Reusable Compose components for common UI elements
- **Activities**: Traditional Android Activities for specific purposes

### Service Layer
- **AppMonitorService**: Monitors app installations/uninstallations
- **BootReceiver**: Handles device boot and package events
- **WallpaperService**: Manages wallpaper-related functionality

## Key Components

### MainActivity
- **Purpose**: Entry point and main screen orchestrator
- **Responsibilities**: 
  - Display home screen and app drawer
  - Handle navigation between screens
  - Manage overlay states
  - Initialize services

### AppRepository
- **Purpose**: Central data access for app information
- **Capabilities**:
  - Load installed apps from PackageManager
  - Search and filter apps
  - Hide/show apps
  - Handle package installation/uninstallation events

### AppMonitorService
- **Purpose**: Background service for monitoring app changes
- **Features**:
  - Listens for package install/uninstall/update events
  - Maintains database sync with device apps
  - Runs as background service

### UI Components
- **AppIcon**: Individual app icon display with label
- **SearchBar**: Search input for app filtering
- **LoadingIndicator**: Progress indicator
- **EmptyState**: Placeholder for empty states

## Gesture Support

LuminaOS supports intuitive gesture navigation:

- **Swipe Up**: Open app drawer from home screen
- **Swipe Down**: Return to home screen from app drawer
- **Long Press**: Access app options menu (design ready)
- **Drag and Drop**: Rearrange apps on home screen (framework ready)

## Customization

### Grid Layout
Users can customize home screen grid size:
- Default: 4 columns × 6 rows
- Customizable via GridConfig

### Theme
- Dark theme included by default
- Theme colors defined in `colors.xml`
- Easy to extend for additional themes

### App Labels
- Custom labels for any app
- Automatic label caching in database

## Testing

The project includes unit and integration tests:

### Unit Tests
- `AppListViewModelTest.kt`: Tests app list functionality
- `AppDrawerViewModelTest.kt`: Tests drawer state management
- `LauncherUtilsTest.kt`: Tests utility functions

### Integration Tests
- `MainActivityInstrumentedTest.kt`: Tests launcher UI interactions

### Running Tests
```bash
# Unit tests
./gradlew test

# Integration tests
./gradlew connectedAndroidTest

# All tests
./gradlew testDebug connectedAndroidTest
```

## Performance Considerations

1. **Database Optimization**:
   - Indexed queries for fast app lookup
   - Efficient filtering and searching

2. **Memory Management**:
   - Lazy loading of app icons
   - View recycling in lists
   - Proper resource cleanup in services

3. **UI Responsiveness**:
   - Compose lazy layouts for efficient rendering
   - Coroutines for non-blocking operations
   - StateFlow for efficient state updates

4. **Battery Impact**:
   - Efficient background monitoring
   - Minimal wake locks
   - Optimized broadcast receivers

## Error Handling

- Comprehensive error handling with try-catch blocks
- Logger utility for debugging
- Graceful fallbacks for missing apps
- User-friendly error messages

## Dependencies

Key gradle dependencies:
- **Jetpack Compose**: UI framework
- **Jetpack Lifecycle**: State management
- **Room**: Database persistence
- **Coroutines**: Asynchronous operations
- **Material Design**: Design components
- **Timber**: Advanced logging

## Permissions

Required permissions in `AndroidManifest.xml`:
- `INTERNET`: Network access
- `QUERY_ALL_PACKAGES`: Access to app list
- `BIND_APPWIDGET`: Widget support
- `RECEIVE_BOOT_COMPLETED`: Boot completio handling
- `SET_WALLPAPER`: Wallpaper management
- `READ/WRITE_EXTERNAL_STORAGE`: File access

## Future Enhancements

Potential features for future versions:
- Widget support with AppWidgetHost
- Custom icon packs
- Cloud sync for settings
- Theming engine with multiple themes
- Gesture customization
- App shortcuts integration
- Notification badges on app icons
- Home screen backup/restore

## Contributing

When contributing to LuminaOS:
1. Follow Kotlin style guidelines
2. Add tests for new features
3. Update documentation
4. Ensure code compiles without warnings
5. Test on multiple API levels

## License

LuminaOS is released under the MIT License. See LICENSE file for details.

## Support

For issues, questions, or suggestions, please create an issue on the project repository.

## Authors

LuminaOS Development Team

---

**Note**: This is a fully functional launcher ready for further development and distribution. Ensure thorough testing on various devices before production release.
