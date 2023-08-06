package com.keinosuke.todoapplication.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.keinosuke.todoapplication.R
import com.keinosuke.todoapplication.data.DisplayTaskType
import com.keinosuke.todoapplication.ui.TodoTopAppBar

@Composable
fun UnarchivedTaskScreen(
    uiState: State<List<HomeUiState>>,
    updateDisplayTaskState: (DisplayTaskType) -> Unit,
    displayTaskState: State<DisplayTaskState>,
    onClickCheckbox: (Boolean, Int) -> Unit,
    onArchiveButtonClick: (Int) -> Unit,
    navigateToEditScreen: (Int, Int) -> Unit,
    resetAddScreen: () -> Unit
){
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TodoTopAppBar(
                title = stringResource(id = R.string.task_list_title),
                canNavigateBack = false
            )
        },
        bottomBar = {
            TodoBottomAppBar(
                updateDisplayTaskState = { updateDisplayTaskState(it) },
                displayTaskState = displayTaskState.value.currentDisplayTaskType,
                resetAddScreen = resetAddScreen
            )
        }
    ) {innerPadding ->
        if (uiState.value.isEmpty()){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ){
                Text(
                    text = stringResource(id = R.string.task_none_text),
                    modifier = Modifier.padding(innerPadding)
                )
            }

        }else{
            TaskCardList(
                modifier = Modifier.padding(innerPadding),
                homeUiStateList = uiState.value,
                onClickCheckbox = { flag, id -> onClickCheckbox(flag, id) },
                onArchiveButtonClick = { id -> onArchiveButtonClick(id) },
                navigateToEditScreen = { id, progress -> navigateToEditScreen(id, progress) }
            )
        }

    }
}