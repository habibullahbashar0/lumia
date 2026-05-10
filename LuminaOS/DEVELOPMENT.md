# LuminaOS Development Guide

## Setup Instructions

### 1. Prerequisites
- Android Studio 2022.1 or newer
- Android SDK 34 (or latest)
- Java 17
- Git

### 2. Initial Setup

```bash
# Clone repository
git clone <repo-url>
cd LuminaOS

# Open in Android Studio
open -a "Android Studio" .

# Or using terminal
android-studio .
```

### 3. Configuration

**SDK Setup:**
1. Tools > SDK Manager
2. Select API 34 (or latest)
3. Download Android SDK Platform

**Gradle Sync:**
1. File > Sync Now
2. Wait for gradle sync to complete
3. Resolve any dependency conflicts

## Development Workflow

### Running the App

**Debug Build:**
```bash
./gradlew installDebug
```

**Run on Emulator:**
```bash
./gradlew runDebug
```

**Run Tests:**
```bash
./gradlew test                    # Unit tests
./gradlew connectedAndroidTest    # Integration tests
./gradlew testDebug               # Debug unit tests
```

### Building Release APK

```bash
# Build unsigned APK
./gradlew assembleRelease

# Build signed APK (if keystore configured)
./gradlew releaseKeyStore
```

## Code Organization Guidelines

### Naming Conventions

**Files:**
- Activities: `*Activity.kt`
- Composables: `*Screen.kt` or `*Component.kt`
- ViewModels: `*ViewModel.kt`
- Repositories: `*Repository.kt`
- Services: `*Service.kt`
- Tests: `*Test.kt`

**Classes & Interfaces:**
- Use PascalCase: `AppListViewModel`
- Use descriptive names: `getAllUserApps` instead of `getApps`

**Functions:**
- Use camelCase: `launchApp()`, `getAppInfo()`
- Prefix boolean functions with `is` or `has`: `isSystemApp()`, `hasPermission()`

**Variables:**
- Use camelCase: `appList`, `isLoading`
- Use meaningful names: `installedApps` instead of `list`

### Package Organization

```
com.luminaos.launcher
├── data              # Data layer
│   ├── dao/         # Database access objects
│   ├── entity/      # Database entities
│   └── repository/  # Repository classes
├── ui               # UI layer (Compose)
│   ├── components/  # Reusable components
│   ├── screens/     # Screen composables
│   └── theme/       # Theme and styling
├── viewmodel        # ViewModel classes
├── service          # Background services
└── utils            # Utility and helper classes
```

## Adding New Features

### Example: Adding Custom Theme Support

**1. Create Data Model:**
```kotlin
// data/ThemeConfig.kt
data class ThemeConfig(
    val name: String,
    val colors: ColorScheme
)
```

**2. Create DAO:**
```kotlin
// data/ThemeDao.kt
@Dao
interface ThemeDao {
    @Query("SELECT * FROM themes")
    fun getAllThemes(): Flow<List<ThemeConfig>>
}
```

**3. Create ViewModel:**
```kotlin
// viewmodel/ThemeViewModel.kt
class ThemeViewModel(app: Application) : AndroidViewModel(app) {
    // Implementation
}
```

**4. Create Composable:**
```kotlin
// ui/ThemeSelector.kt
@Composable
fun ThemeSelector(viewModel: ThemeViewModel) {
    // Composable UI
}
```

**5. Add Tests:**
```kotlin
// ThemeViewModelTest.kt
class ThemeViewModelTest {
    @Test
    fun testThemeLoading() {
        // Test implementation
    }
}
```

## Testing Guidelines

### Unit Testing Best Practices

```kotlin
@RunWith(AndroidJUnit4::class)
class AppRepositoryTest {
    
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()
    
    private lateinit var repository: AppRepository
    private val mockDao = mock<AppInfoDao>()
    
    @Before
    fun setUp() {
        repository = AppRepository(mockDao)
    }
    
    @Test
    fun testSearchFunctionality() {
        // AAA Pattern: Arrange, Act, Assert
        
        // Arrange
        val query = "test"
        val expectedApps = listOf(
            AppInfo("com.test.app", "Test App", "MainActivity", false)
        )
        
        // Act
        val result = repository.searchApps(query)
        
        // Assert
        assertEquals(expectedApps, result)
    }
}
```

### Integration Testing

```kotlin
@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    
    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java)
    
    @Test
    fun testHomeScreenDisplay() {
        // Test home screen is displayed
        onView(withText(R.string.app_name)).check(matches(isDisplayed()))
    }
}
```

## Code Style

### Kotlin Style Guide Adherence

**1. Maximum Line Length:** 100 characters

**2. Indentation:** 4 spaces (no tabs)

**3. Naming:**
```kotlin
// Good
val userList = getUserList()
fun calculateTotal() {}

// Bad
val ul = getUserList()
fun calc() {}
```

**4. Comments:**
```kotlin
// Single line comment
val app = getApp() // Inline comment

/**
 * Multi-line comment.
 * Provides detailed explanation.
 */
fun processApp(): AppInfo {
    // Implementation
}
```

**5. Formatting:**
```kotlin
// Good
val result = when (value) {
    1 -> "One"
    2 -> "Two"
    else -> "Other"
}

// Bad
val result = when(value) { 1 -> "One"; 2 -> "Two"; else -> "Other" }
```

## Logging

Using the custom Logger utility:

```kotlin
val logger = Logger("ClassName")

logger.d("Debug message")
logger.i("Info message")
logger.w("Warning message")
logger.e("Error message", throwable)
```

## Performance Profiling

### Using Android Studio Profiler

1. Run > Profile 'app'
2. Monitor: CPU, Memory, Network, Energy
3. Identify bottlenecks
4. Optimize accordingly

### Memory Leaks Detection

```bash
# Generate heap dump
./gradlew debugHeapDump

# Analyze with Android Studio
Profile > Memory > Export heap dump
```

## Debugging Tips

### Enable Logging
In `Logger.kt`:
```kotlin
if (BuildConfig.DEBUG) {
    Log.d(tag, message)
}
```

### Breakpoints in Compose
```kotlin
@Composable
fun MyComposable() {
    // Set breakpoint here to debug composition
    val state by viewModel.state.collectAsState()
}
```

### Database Inspection
```bash
# List databases
adb shell run-as com.luminaos.launcher ls /data/data/com.luminaos.launcher/databases

# Pull database
adb pull /data/data/com.luminaos.launcher/databases/lumina_launcher_db.db
```

## Common Issues & Solutions

### Issue 1: Gradle Sync Failure
**Solution:**
```bash
./gradlew clean
./gradlew sync
```

### Issue 2: Compose Recomposition Issues
**Solution:** Check StateFlow emissions and ensure proper key parameters in LazyGrid/LazyColumn

### Issue 3: Database Not Updating
**Solution:** Verify DAO queries and ensure migrations are handled

### Issue 4: Service Not Starting
**Solution:** Check Android version (O+ requires foregroundService)

## Release Checklist

Before releasing to production:

- [ ] All tests passing (`./gradlew test`)
- [ ] No lint warnings (`./gradlew lint`)
- [ ] ProGuard rules configured
- [ ] Memory leaks checked
- [ ] Performance profiled
- [ ] Version code incremented
- [ ] Release notes prepared
- [ ] APK signed
- [ ] Tested on multiple devices/API levels

## Useful Commands

```bash
# Clean build
./gradlew clean build

# Run tests with coverage
./gradlew test jacocoTestReport

# Static analysis
./gradlew lint

# Format code
./gradlew spotlessApply

# Check dependencies
./gradlew dependencies

# Upgrade dependencies
./gradlew dependencyUpdates

# Build and install
./gradlew installDebug

# Build APK
./gradlew assembleDebug

# Generate signed APK
./gradlew assembleRelease
```

## Documentation Standards

All public functions should include KDoc:

```kotlin
/**
 * Loads all installed applications from PackageManager and caches them.
 *
 * This function performs blocking I/O operations and should be called
 * from a background thread or within a coroutine.
 *
 * @return List of installed applications
 * @throws IOException if database operations fail
 * @see AppInfo
 */
suspend fun loadInstalledApps(): List<AppInfo>
```

## Contributing Checklist

Before submitting a pull request:

- [ ] Code follows style guidelines
- [ ] Comments and documentation added
- [ ] Tests written and passing
- [ ] No lint warnings
- [ ] Tested on multiple API levels
- [ ] Performance considered
- [ ] No new warnings introduced

---

For more information, refer to:
- [Architecture Guide](ARCHITECTURE.md)
- [Official Android Documentation](https://developer.android.com)
- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Kotlin Documentation](https://kotlinlang.org/docs)
