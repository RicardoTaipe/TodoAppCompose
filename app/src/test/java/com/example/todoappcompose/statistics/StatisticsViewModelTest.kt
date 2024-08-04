package com.example.todoappcompose.statistics

import com.example.todoappcompose.MainCoroutineRule
import com.example.todoappcompose.data.Task
import com.example.todoappcompose.data.source.FakeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class StatisticsViewModelTest {
    // Subject under test
    private lateinit var statisticsViewModel: StatisticsViewModel

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    // Use a fake repository to be injected into the viewmodel
    private lateinit var tasksRepository: FakeRepository

    @Before
    fun setupStatisticsViewModel() {
        tasksRepository = FakeRepository()
        statisticsViewModel = StatisticsViewModel(tasksRepository)
    }

    @Test
    fun loadEmptyTasksFromRepository_EmptyResults() = runTest {
        // Given an initialized StatisticsViewModel with no tasks

        // Then the results are empty
        val uiState = statisticsViewModel.uiState.first()
        assertThat(uiState.isEmpty, `is`(true))
    }

    @Test
    fun loadNonEmptyTasksFromRepository_NonEmptyResults() = runTest {
        // We initialise the tasks to 3, with one active and two completed
        val task1 = Task("Title1", "Description1")
        val task2 = Task("Title2", "Description2", true)
        val task3 = Task("Title3", "Description3", true)
        val task4 = Task("Title4", "Description4", true)
        tasksRepository.addTasks(task1, task2, task3, task4)

        // Then the results are not empty
        val uiState = statisticsViewModel.uiState.first()
        assertThat(uiState.isEmpty, `is`(false))
        assertThat(uiState.activeTasksPercent, `is`(25f))
        assertThat(uiState.completedTasksPercent, `is`(75f))
        assertThat(uiState.isLoading, `is`(false))
    }

    @Test
    fun loadTasks_loading() = runTest {
        // Set Main dispatcher to not run coroutines eagerly, for just this one test
        Dispatchers.setMain(StandardTestDispatcher())

        var isLoading: Boolean? = true
        val job = launch {
            statisticsViewModel.uiState.collect {
                isLoading = it.isLoading
            }
        }

        // Then progress indicator is shown
        assertThat(isLoading, `is`(true))

        // Execute pending coroutines actions
        advanceUntilIdle()

        // Then progress indicator is hidden
        assertThat(isLoading, `is`(false))
        job.cancel()
    }

}