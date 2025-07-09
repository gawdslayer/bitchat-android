package com.bitchat

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Main application class for Bitchat Android client.
 * Uses Hilt for dependency injection.
 */
@HiltAndroidApp
class BitchatApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize any application-wide configurations here
        // For example, WorkManager, crash reporting, etc.
    }
} 