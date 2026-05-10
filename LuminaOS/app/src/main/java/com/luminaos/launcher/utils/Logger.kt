package com.luminaos.launcher.utils

import android.util.Log

/**
 * Simple logging utility with tag prefix for LuminaOS launcher.
 * Provides convenient debug, info, warning, and error logging methods.
 */
class Logger(private val tag: String) {
    private val prefix = "LuminaOS"

    /**
     * Log debug message.
     */
    fun d(message: String, throwable: Throwable? = null) {
        Log.d(getTag(), message, throwable)
    }

    /**
     * Log info message.
     */
    fun i(message: String, throwable: Throwable? = null) {
        Log.i(getTag(), message, throwable)
    }

    /**
     * Log warning message.
     */
    fun w(message: String, throwable: Throwable? = null) {
        Log.w(getTag(), message, throwable)
    }

    /**
     * Log error message.
     */
    fun e(message: String, throwable: Throwable? = null) {
        Log.e(getTag(), message, throwable)
    }

    /**
     * Get full tag with prefix.
     */
    private fun getTag(): String = "$prefix/$tag"
}

/**
 * Extension function to create Logger instance.
 */
fun createLogger(tag: String) = Logger(tag)
