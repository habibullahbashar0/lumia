<!-- Proguard Configuration Details -->

-verbose

# Keep all classes in the launcher package
-keep public class com.luminaos.launcher.** { *; }

# Keep all Activities
-keep class * extends android.app.Activity {
    public <init>();
    public <init>(android.content.Context);
}

# Keep all Services
-keep class * extends android.app.Service {
    public <init>();
}

# Keep all BroadcastReceivers
-keep class * extends android.content.BroadcastReceiver {
    public <init>();
}

# Keep all ContentProviders
-keep class * extends android.content.ContentProvider {
    public <init>();
}

# Keep Android system classes
-dontwarn android.**
-keep class android.** { *; }

# Keep Compose classes and interfaces
-keep class androidx.compose.** { *; }
-keep interface androidx.compose.** { *; }

# Keep all ViewModels
-keep class androidx.lifecycle.ViewModel { *; }
-keep class * extends androidx.lifecycle.ViewModel {
    <init>();
}

# Keep Room classes
-keep class androidx.room.** { *; }
-keep interface androidx.room.** { *; }
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao interface * { *; }

# Keep Lifecycle classes
-keep class androidx.lifecycle.** { *; }
-keep interface androidx.lifecycle.** { *; }

# Keep Coroutines
-keep class kotlinx.coroutines.** { *; }
-keep interface kotlinx.coroutines.** { *; }

# Keep data model classes
-keep class com.luminaos.launcher.data.** { *; }

# Keep enum classes
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep native methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep custom view constructors
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

# Keep R class
-keepclassmembers class **.R$* {
    public static <fields>;
}

# Timber logging
-keep class com.jakewharton.timber.** { *; }
-keep interface com.jakewharton.timber.** { *; }

# Remove logging in release builds
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}

# Keep custom exceptions
-keep class com.luminaos.launcher.** extends java.lang.Exception { *; }
-keep class com.luminaos.launcher.** extends java.lang.RuntimeException { *; }

# Optimization flags
-optimizationpasses 5
-dontskipnonpubliclibraryclass
-verbose

# Rename source file attribute
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable

# Keep line numbers for debugging
-keepattributes LineNumberTable
