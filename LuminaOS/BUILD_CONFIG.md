# LuminaOS Build Configuration

## Build Environment

### Android Gradle Plugin
- Version: 8.2.0
- Requires: Gradle 8.0+

### Kotlin Version
- Version: 1.9.22
- Compiler: kotlinc

### Java Version
- Source Compatibility: Java 17
- Target Compatibility: Java 17

## Build Configurations

### Debug Build
```bash
./gradlew assembleDebug
```
- Debugging enabled
- Minification disabled
- Optimizations off
- Fast build times

### Release Build
```bash
./gradlew assembleRelease
```
- Debugging disabled
- ProGuard minification enabled
- Optimizations enabled
- Signing required for distribution

## Build Variants

### Debug
- Configuration: `debugBuildType`
- Optimization: None
- Use case: Development and testing

### Release
- Configuration: `releaseBuildType`
- Optimization: ProGuard R8
- Use case: Production distribution

## Build Dependencies

### Core Android
- androidx.core:core-ktx:1.12.0
- androidx.lifecycle:lifecycle-runtime-ktx:2.7.0
- androidx.activity:activity-compose:1.8.1

### Compose
- androidx.compose.ui:ui
- androidx.compose.material3:material3:1.1.2
- androidx.compose.foundation:foundation:1.6.1

### Persistence
- androidx.room:room-runtime:2.6.1
- androidx.room:room-ktx:2.6.1

### Navigation
- androidx.navigation:navigation-compose:2.7.6

### Async
- org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3
- org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3

### Testing
- junit:junit:4.13.2
- androidx.test.ext:junit:1.1.5
- androidx.test.espresso:espresso-core:3.5.1
- org.mockito:mockito-core:5.7.0

## Build Performance

### Optimization Tips

1. **Gradle Daemon**
   - Speeds up consecutive builds
   - Already enabled by default

2. **Incremental Compilation**
   - Only compiles changed files
   - Enabled automatically

3. **Minification (Release)**
   - Rules in proguard-rules.pro
   - Reduces APK size by ~30-40%

4. **Lint**
   - Run before release: `./gradlew lint`
   - Identifies potential issues

## Troubleshooting

### Clear Build Cache
```bash
./gradlew clean
```

### Force Gradle Sync
```bash
./gradlew sync --refresh-dependencies
```

### Run with Verbose Output
```bash
./gradlew build --info
```

### Check Dependency Tree
```bash
./gradlew dependencies
```

## APK Output Locations

### Debug APK
```
app/build/outputs/apk/debug/app-debug.apk
```

### Release APK
```
app/build/outputs/apk/release/app-release.apk
```

### Bundle
```
app/build/outputs/bundle/release/app-release.aab
```

## Signing Configuration

### Generate Keystore
```bash
keytool -genkey -v -keystore release.keystore \
  -keyalg RSA -keysize 2048 -validity 10000
```

### Configure Signing
In `build.gradle.kts`:
```kotlin
signingConfigs {
    release {
        storeFile = file("path/to/release.keystore")
        storePassword = "password"
        keyAlias = "alias"
        keyPassword = "password"
    }
}
```

## Continuous Integration

For CI/CD pipelines (GitHub Actions, GitLab CI, etc.):

```yaml
# Example GitHub Actions
- name: Build Debug APK
  run: ./gradlew assembleDebug

- name: Run Tests
  run: ./gradlew test

- name: Build Release APK
  run: ./gradlew assembleRelease
```

---

For detailed build instructions, see [README.md](README.md)
