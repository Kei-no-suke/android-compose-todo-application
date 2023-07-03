package com.example.todoapplication.ui.screens.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todoapplication.R
import com.example.todoapplication.data.DisplayTaskType
import com.example.todoapplication.ui.AppViewModelProvider
import com.example.todoapplication.ui.TodoTopAppBar

@Composable
fun ArchivedTaskScreen(
    uiState: State<List<HomeUiState>>,
    updateDisplayTaskState: (DisplayTaskType) -> Unit,
    displayTaskState: State<DisplayTaskState>,
    onArchiveButtonClick: (Int) -> Unit
){
    Scaffold(
        topBar = {
            TodoTopAppBar(
                title = stringResource(id = R.string.archive_task_title),
                canNavigateBack = false
            )
        },
        bottomBar = {
            TodoBottomAppBar(
                updateDisplayTaskState = { updateDisplayTaskState(it) },
                displayTaskState = displayTaskState.value.currentDisplayTaskType
            )
        }
    ) {innerPadding ->
        if (uiState.value.size == 0){
            Text(
                text = stringResource(id = R.string.task_none_text),
                modifier = Modifier.padding(innerPadding)
            )
        }else{
            ArchivedTaskCardList(
                modifier = Modifier.padding(innerPadding),
                homeUiStateList = uiState.value,
                onArchiveButtonClick = { onArchiveButtonClick(it) }
            )
        }

    }
}