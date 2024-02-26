package com.example.todoappcompose

import android.content.Context
import androidx.room.Room
import com.example.todoappcompose.data.source.DefaultTasksRepository
import com.example.todoappcompose.data.source.TasksDataSource
import com.example.todoappcompose.data.source.TasksRepository
import com.example.todoappcompose.data.source.local.TasksLocalDataSource
import com.example.todoappcompose.data.source.local.ToDoDatabase
import com.example.todoappcompose.data.source.remote.TasksRemoteDataSource
import kotlinx.coroutines.runBlocking
import org.jetbrains.annotations.VisibleForTesting

/**
 * A Service Locator for the [TasksRepository]. This is the prod version, with a
 * the "real" [TasksRemoteDataSource].
 */
object ServiceLocator {

    private val lock = Any()
    private var database: ToDoDatabase? = null
    @Volatile
    var tasksRepository: TasksRepository? = null
        @VisibleForTesting set

    fun provideTasksRepository(context: Context): TasksRepository {
        synchronized(this) {
            return tasksRepository ?: tasksRepository ?: createTasksRepository(context)
        }
    }

    private fun createTasksRepository(context: Context): TasksRepository {
        val newRepo =
            DefaultTasksRepository(TasksRemoteDataSource, createTaskLocalDataSource(context))
        tasksRepository = newRepo
        return newRepo
    }

    private fun createTaskLocalDataSource(context: Context): TasksDataSource {
        val database = database ?: createDataBase(context)
        return TasksLocalDataSource(database.taskDao())
    }

    private fun createDataBase(context: Context): ToDoDatabase {
        val result = Room.databaseBuilder(
            context.applicationContext,
            ToDoDatabase::class.java, DB_NAME
        ).build()
        database = result
        return result
    }

    @VisibleForTesting
    fun resetRepository() {
        synchronized(lock) {
            runBlocking {
                TasksRemoteDataSource.deleteAllTasks()
            }
            // Clear all data to avoid test pollution.
            database?.apply {
                clearAllTables()
                close()
            }
            database = null
            tasksRepository = null
        }
    }
}

private const val DB_NAME = "Tasks.db"