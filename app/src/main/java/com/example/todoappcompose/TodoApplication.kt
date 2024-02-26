package com.example.todoappcompose

import android.app.Application
import timber.log.Timber
import timber.log.Timber.*

class TodoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(DebugTree())
    }
}