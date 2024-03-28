package com.example.todoappcompose.util

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.todoappcompose.R
import com.example.todoappcompose.ui.theme.TodoAppComposeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsTopAppBar(openDrawer: () -> Unit) {
    TopAppBar(
        title = { Text(text = stringResource(id = R.string.statistics_title)) },
        navigationIcon = {
            IconButton(onClick = openDrawer) {
                Icon(Icons.Filled.Menu, stringResource(id = R.string.open_drawer))
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}


@Preview
@Composable
private fun StatisticsTopAppBarPreview() {
    TodoAppComposeTheme {
        Surface {
            StatisticsTopAppBar { }
        }
    }
}