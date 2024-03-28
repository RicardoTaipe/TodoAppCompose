package com.example.todoappcompose

import android.os.Bundle
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.example.todoappcompose.data.source.TasksRepository
import com.example.todoappcompose.statistics.StatisticsViewModel

/**
 * Factory for all ViewModels.
 */
@Suppress("UNCHECKED_CAST")
class ViewModelFactory constructor(
    private val tasksRepository: TasksRepository,
    owner: SavedStateRegistryOwner,
    defaultArgs: Bundle? = null
) : AbstractSavedStateViewModelFactory(owner, defaultArgs) {

    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ) = with(modelClass) {
        when {
            isAssignableFrom(StatisticsViewModel::class.java) ->
                StatisticsViewModel(tasksRepository)
//            isAssignableFrom(TaskDetailViewModel::class.java) ->
//                TaskDetailViewModel(tasksRepository, handle)
//            isAssignableFrom(AddEditTaskViewModel::class.java) ->
//                AddEditTaskViewModel(tasksRepository, handle)
//            isAssignableFrom(TasksViewModel::class.java) ->
//                TasksViewModel(tasksRepository, handle)
            else ->
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    } as T
}