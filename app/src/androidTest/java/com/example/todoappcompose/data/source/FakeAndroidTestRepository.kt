package com.example.todoappcompose.data.source

import com.example.todoappcompose.data.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import org.jetbrains.annotations.VisibleForTesting

class FakeAndroidTestRepository : TasksRepository {
    private var shouldReturnError = false

    private val _savedTasks = MutableStateFlow(LinkedHashMap<String, Task>())
    val savedTasks: StateFlow<LinkedHashMap<String, Task>> = _savedTasks.asStateFlow()

    private val observableTasks: Flow<com.example.todoappcompose.data.Result<List<Task>>> =
        savedTasks.map {
            if (shouldReturnError) {
                com.example.todoappcompose.data.Result.Error(Exception())
            } else {
                com.example.todoappcompose.data.Result.Success(it.values.toList())
            }
        }

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override suspend fun refreshTasks() {
        // Tasks already refreshed
    }

    override suspend fun refreshTask(taskId: String) {
        refreshTasks()
    }

    override fun getTasksStream(): Flow<com.example.todoappcompose.data.Result<List<Task>>> =
        observableTasks

    override fun getTaskStream(taskId: String): Flow<com.example.todoappcompose.data.Result<Task>> {
        return observableTasks.map { tasks ->
            when (tasks) {
                is com.example.todoappcompose.data.Result.Error -> com.example.todoappcompose.data.Result.Error(
                    tasks.exception
                )

                is com.example.todoappcompose.data.Result.Success -> {
                    val task = tasks.data.firstOrNull { it.id == taskId }
                        ?: return@map com.example.todoappcompose.data.Result.Error(Exception("Not found"))
                    com.example.todoappcompose.data.Result.Success(task)
                }
            }
        }
    }

    override suspend fun getTask(
        taskId: String,
        forceUpdate: Boolean
    ): com.example.todoappcompose.data.Result<Task> {
        if (shouldReturnError) {
            return com.example.todoappcompose.data.Result.Error(Exception("Test exception"))
        }
        savedTasks.value[taskId]?.let {
            return com.example.todoappcompose.data.Result.Success(it)
        }
        return com.example.todoappcompose.data.Result.Error(Exception("Could not find task"))
    }

    override suspend fun getTasks(forceUpdate: Boolean): com.example.todoappcompose.data.Result<List<Task>> {
        if (shouldReturnError) {
            return com.example.todoappcompose.data.Result.Error(Exception("Test exception"))
        }
        return observableTasks.first()
    }

    override suspend fun saveTask(task: Task) {
        _savedTasks.update { tasks ->
            val newTasks = LinkedHashMap<String, Task>(tasks)
            newTasks[task.id] = task
            newTasks
        }
    }

    override suspend fun completeTask(task: Task) {
        val completedTask = Task(task.title, task.description, true, task.id)
        _savedTasks.update { tasks ->
            val newTasks = LinkedHashMap<String, Task>(tasks)
            newTasks[task.id] = completedTask
            newTasks
        }
    }

    override suspend fun completeTask(taskId: String) {
        // Not required for the remote data source.
        throw NotImplementedError()
    }

    override suspend fun activateTask(task: Task) {
        val activeTask = Task(task.title, task.description, false, task.id)
        _savedTasks.update { tasks ->
            val newTasks = LinkedHashMap<String, Task>(tasks)
            newTasks[task.id] = activeTask
            newTasks
        }
    }

    override suspend fun activateTask(taskId: String) {
        throw NotImplementedError()
    }

    override suspend fun clearCompletedTasks() {
        _savedTasks.update { tasks ->
            tasks.filterValues {
                !it.isCompleted
            } as LinkedHashMap<String, Task>
        }
    }

    override suspend fun deleteTask(taskId: String) {
        _savedTasks.update { tasks ->
            val newTasks = LinkedHashMap<String, Task>(tasks)
            newTasks.remove(taskId)
            newTasks
        }
    }

    override suspend fun deleteAllTasks() {
        _savedTasks.update {
            LinkedHashMap()
        }
    }

    @VisibleForTesting
    fun addTasks(vararg tasks: Task) {
        _savedTasks.update { oldTasks ->
            val newTasks = LinkedHashMap<String, Task>(oldTasks)
            for (task in tasks) {
                newTasks[task.id] = task
            }
            newTasks
        }
    }
}