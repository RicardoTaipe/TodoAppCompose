package com.example.todoappcompose.statistics

import androidx.activity.ComponentActivity
import androidx.compose.material3.Surface
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.todoappcompose.R
import com.example.todoappcompose.data.Task
import com.example.todoappcompose.data.source.FakeAndroidTestRepository
import com.example.todoappcompose.ui.theme.TodoAppComposeTheme
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class StatisticsScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private val activity get() = composeTestRule.activity

    @Test
    fun tasks_showsNonEmptyMessage() = runTest {
        // Given some tasks
        val repository = FakeAndroidTestRepository().apply {
            saveTask(Task("Title1", "Description1", false))
            saveTask(Task("Title2", "Description2", true))
        }

        composeTestRule.setContent {
            TodoAppComposeTheme {
                Surface {
                    StatisticsScreen(
                        openDrawer = { }, viewModel = StatisticsViewModel(repository)
                    )
                }
            }
        }

        val expectedActiveTaskText = activity.getString(R.string.statistics_active_tasks, 50.0f)
        val expectedCompletedTaskText =
            activity.getString(R.string.statistics_completed_tasks, 50.0f)

        // check that both info boxes are displayed and contain the correct info
        composeTestRule.onNodeWithText(expectedActiveTaskText).assertIsDisplayed()
        composeTestRule.onNodeWithText(expectedCompletedTaskText).assertIsDisplayed()
    }

}