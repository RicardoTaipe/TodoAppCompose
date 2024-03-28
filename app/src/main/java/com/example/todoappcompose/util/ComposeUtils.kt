package com.example.todoappcompose.util

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import com.example.todoappcompose.TodoApplication
import com.example.todoappcompose.ViewModelFactory
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

/**
 * Obtain the [ViewModelFactory] for ViewModels in this app
 */
@Composable
fun getViewModelFactory(defaultArgs: Bundle? = null): ViewModelFactory {
    val repository = (LocalContext.current.applicationContext as TodoApplication).taskRepository
    return ViewModelFactory(repository, LocalSavedStateRegistryOwner.current, defaultArgs)
}

/**
 * Display an initial empty state or swipe to refresh content.
 *
 * @param loading (state) when true, display a loading spinner over [content]
 * @param empty (state) when true, display [emptyContent]
 * @param emptyContent (slot) the content to display for the empty state
 * @param onRefresh (event) event to request refresh
 * @param modifier the modifier to apply to this layout.
 * @param content (slot) the main content to show
 */
@Composable
fun LoadingContent(
    loading: Boolean,
    empty: Boolean,
    emptyContent: @Composable () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    if (empty) {
        emptyContent()
    } else {
        SwipeRefresh(
            state = rememberSwipeRefreshState(loading),
            onRefresh = onRefresh,
            modifier = modifier,
            content = content,
        )
    }
}