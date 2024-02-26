package com.example.todoappcompose

import android.app.Application
import com.example.todoappcompose.data.source.TasksRepository
import timber.log.Timber
import timber.log.Timber.*

class TodoApplication : Application() {

    val taskRepository: TasksRepository
        get() = ServiceLocator.provideTasksRepository(this)

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(DebugTree())
    }
}