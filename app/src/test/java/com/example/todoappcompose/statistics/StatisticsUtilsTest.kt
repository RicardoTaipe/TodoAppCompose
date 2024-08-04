package com.example.todoappcompose.statistics

import com.example.todoappcompose.data.Task
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class StatisticsUtilsTest {
    @Test
    fun getActiveAndCompleteStats_noCompleted() {
        val tasks =
            listOf(Task(id = "id", title = "title", description = "desc", isCompleted = false))
        // When the list of tasks is computed with an active task
        val result = getActiveAndCompletedStats(tasks)
        assertThat(result.activeTasksPercent, `is`(100f))
    }

    @Test
    fun getActiveAndCompletedStats_noActive() {
        val tasks = listOf(
            Task(
                id = "id",
                title = "title",
                description = "desc",
                isCompleted = true,
            )
        )
        // When the list of tasks is computed with a completed task
        val result = getActiveAndCompletedStats(tasks)

        // Then the percentages are 0 and 100
        assertThat(result.activeTasksPercent, `is`(0f))
        assertThat(result.completedTasksPercent, `is`(100f))
    }

    @Test
    fun getActiveAndCompletedStats_both() {
        // Given 3 completed tasks and 2 active tasks
        val tasks = listOf(
            Task(id = "1", title = "title", description = "desc", isCompleted = true),
            Task(id = "2", title = "title", description = "desc", isCompleted = true),
            Task(id = "3", title = "title", description = "desc", isCompleted = true),
            Task(id = "4", title = "title", description = "desc", isCompleted = false),
            Task(id = "5", title = "title", description = "desc", isCompleted = false),
        )
        // When the list of tasks is computed
        val result = getActiveAndCompletedStats(tasks)

        // Then the result is 40-60
        assertThat(result.activeTasksPercent, `is`(40f))
        assertThat(result.completedTasksPercent, `is`(60f))
    }

    @Test
    fun getActiveAndCompletedStats_empty() {
        // When there are no tasks
        val result = getActiveAndCompletedStats(emptyList())

        // Both active and completed tasks are 0
        assertThat(result.activeTasksPercent, `is`(0f))
        assertThat(result.completedTasksPercent, `is`(0f))
    }
}