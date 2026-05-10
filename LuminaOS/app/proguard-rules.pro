# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.kts.

# For more details, see http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
-keepattributes SourceFile,LineNumberTable

# Keep Compose classes
-keep class androidx.compose.** { *; }

# Keep all public and protected classes of the app
-keep public class com.luminaos.launcher.** { public protected *; }

# Keep all services
-keep class * extends android.app.Service
-keep class * extends android.content.BroadcastReceiver

# Keep Kotlin metadata
-keepattributes RuntimeVisibleAnnotations
-keep class kotlin.** { *; }
-keepclassmembers class kotlin.** { *; }

# Keep Timber logging
-keep class com.jakewharton.timber.** { *; }
