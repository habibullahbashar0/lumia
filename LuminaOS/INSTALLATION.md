# LuminaOS Installation Guide

## System Requirements

- **Android Version**: 9.0 (API 28) or higher
- **Minimum RAM**: 2 GB
- **Storage Space**: ~50 MB for installation + 10 MB for app data
- **Internet**: Required for initial setup (optional for runtime)

## Installation Methods

### Method 1: Via Google Play Store (When Available)

1. Open Google Play Store
2. Search for "LuminaOS Launcher"
3. Tap "Install"
4. Wait for installation to complete
5. Open the app
6. Follow initial setup

### Method 2: Manual APK Installation

#### From Android Studio (Recommended for Development)
```bash
./gradlew installDebug
```

#### From Command Line
```bash
adb install app/build/outputs/apk/debug/app-debug.apk
```

#### From File Manager
1. Download LuminaOS APK
2. Open file manager
3. Navigate to Downloads folder
4. Tap the APK file
5. Confirm installation
6. Grant necessary permissions

### Method 3: Build from Source

```bash
# Clone repository
git clone https://github.com/luminaos/launcher.git
cd LuminaOS

# Build APK
./gradlew assembleRelease

# Install
adb install app/build/outputs/apk/release/app-release.apk
```

## Initial Setup

### Step 1: App Launch
1. Open LuminaOS from app drawer
2. Wait for app initialization
3. Grant required permissions

### Step 2: Set as Default Launcher
1. Open LuminaOS
2. Navigate to Settings (gear icon)
3. Tap "Set as Default Launcher"
4. Choose "LuminaOS" from the system dialog
5. Confirm selection

### Step 3: Customize (Optional)
1. Long-press on home screen to access options
2. Adjust grid size if desired
3. Organize apps as needed

## Required Permissions

LuminaOS requires the following permissions:

| Permission | Purpose |
|-----------|---------|
| QUERY_ALL_PACKAGES | Access to app list |
| INTERNET | Connectivity features |
| BIND_APPWIDGET | Widget support |
| SET_WALLPAPER | Wallpaper management |
| READ_EXTERNAL_STORAGE | Access to media files |
| WRITE_EXTERNAL_STORAGE | Save configurations |
| RECEIVE_BOOT_COMPLETED | Auto-start on device boot |

## Granting Permissions

### Android 6.0+ (Runtime Permissions)
1. Open Settings > Apps > LuminaOS
2. Tap "Permissions"
3. Grant required permissions
4. Restart LuminaOS

### Android 5.1 and Below
Permissions are granted during installation.

## Using LuminaOS

### Basic Navigation
- **Home Screen**: Main launcher interface
- **App Drawer**: Swipe up from home screen
- **App Launch**: Tap any app icon
- **Return Home**: Swipe down or press back
- **Recent Apps**: Recent apps on home screen

### Menu Options
- **Settings**: Gear icon in top right
- **Search**: Search bar in app drawer
- **Grid Size**: Change layout in settings
- **Theme**: Switch between themes (if available)

### Long-Press Actions
- **Home Screen**: Access customization options
- **App Icon**: See app options (coming soon)

## Troubleshooting

### Issue: "App Not Installed"
**Solution**: 
- Ensure sufficient storage space
- Try uninstalling previous version
- Grant installation permission

### Issue: "Default Launcher Not Set"
**Solution**:
1. Go to Settings > Apps > Default apps (or similar)
2. Select "Home app"
3. Choose "LuminaOS"

### Issue: "Crashes on Launch"
**Solution**:
1. Clear app cache: Settings > Apps > LuminaOS > Storage > Clear Cache
2. Clear app data (note: this will reset all customizations)
3. Reinstall the app
4. Update Android OS

### Issue: "Slow Performance"
**Solution**:
- Reduce grid size (smaller grids = better performance)
- Close background apps
- Check device storage (free up space if low)
- Restart device

### Issue: "Apps Not Appearing"
**Solution**:
1. Refresh app list manually
2. Restart LuminaOS
3. Reboot device
4. Clear app cache

## Uninstallation

### To Remove LuminaOS

1. **If Set as Default Launcher**:
   - Go to Settings > Apps > Default apps
   - Select another launcher before uninstalling

2. **Standard Uninstallation**:
   - Settings > Apps > LuminaOS
   - Tap "Uninstall"
   - Confirm uninstallation

3. **Via ADB**:
   ```bash
   adb uninstall com.luminaos.launcher
   ```

## Backup and Restore

### Backup Settings
```bash
adb backup -apk com.luminaos.launcher
```

### Restore Settings
```bash
adb restore backup.ab
```

## Updates

### Automatic Updates (Google Play)
- Navigate to LuminaOS in Play Store
- Tap "Update" if available
- Installation happens automatically

### Manual Updates
1. Download latest APK
2. Install same as initial installation
3. Old version automatically replaced

## Performance Tips

1. **Optimize Home Screen**:
   - Reduce number of shortcuts
   - Use folders to organize apps
   - Limit grid size for older devices

2. **Manage Background Processing**:
   - Close unused apps
   - Disable animations in settings
   - Monitor available RAM

3. **Storage Management**:
   - Clear app cache regularly
   - Remove unused apps
   - Keep 1-2 GB free space

## Battery Usage

Expected battery impact:
- Minimal impact on battery life
- ~1-2% additional drain vs system launcher
- Optimized for efficiency

To reduce battery impact:
1. Disable animations (Settings)
2. Reduce refresh frequency
3. Use dark theme (on OLED screens)

## Data Usage

Expected data usage:
- Minimal during normal operation
- App list refresh: <1 MB
- Setting sync: <100 KB

## Storage Usage

Typical storage requirements:
- Installation size: ~40-50 MB
- Cache size: ~5-10 MB
- Configuration data: ~1-2 MB

## Contacting Support

For issues or questions:
- Create issue on GitHub
- Check FAQ in app
- Review logs for debugging

---

**Note**: For development installation instructions, see [DEVELOPMENT.md](DEVELOPMENT.md)

**Version**: 1.0.0
**Last Updated**: 2024
