package com.example.todoapplication.ui.screens.home

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todoapplication.R
import com.example.todoapplication.ui.AppViewModelProvider
import com.example.todoapplication.ui.TodoTopAppBar
import kotlinx.coroutines.launch

@Composable
fun AddNewTaskScreen(
    viewModel: TodoHomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TodoTopAppBar(
                title = stringResource(id = R.string.task_entry_title),
                canNavigateBack = true
            )
        },
        bottomBar = {
            TodoBottomAppBar(
                updateHomeScreenState = { viewModel.updateHomeArchivedState(it) },
                homeScreenState = viewModel.homeArchivedState.homeScreenState
            )
        },
    ) {innerPadding ->
        FormItems(
            taskUiState = viewModel.taskUiState,
            onDeadlineClickConfirmButton = { deadline ->
                viewModel.updateDeadline(deadline)
                viewModel.updateIsDisplayDeadlineDatePicker()
            },
            onDismissRequest = { viewModel.updateIsDisplayDeadlineDatePicker() },
            onCalenderIconClick = { viewModel.updateIsDisplayDeadlineDatePicker() },
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveTask()
                    viewModel.updateHomeArchivedState(HomeScreenState.Unarchived.name)
                }
            },
            onNameValueChange = {name -> viewModel.updateName(name) },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun FormItems(
    taskUiState: TaskUiState,
    onDeadlineClickConfirmButton: (deadline: Long?) -> Unit,
    onDismissRequest: () -> Unit,
    onCalenderIconClick: () -> Unit,
    onSaveClick: () -> Unit,
    onNameValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
){
    LazyColumn(modifier = modifier){
        item{
            TextFieldArea(
                labelResId = R.string.task_name_label,
                inputFieldText = taskUiState.taskDetail.name,
                onValueChange = onNameValueChange
            )
            DatePickerDialogArea(
                labelResId = R.string.form_item_deadline_text,
                inputFieldText = taskUiState.taskDetail.deadline,
                isDisplay = taskUiState.isDisplayDeadlineDatePicker,
                onClickConfirmButton = {deadline -> onDeadlineClickConfirmButton(deadline)},
                onDismissRequest = onDismissRequest,
                onCalenderIconClick = onCalenderIconClick
            )
            // セーブボタン
            Button(onClick = onSaveClick) {
                Text(text = stringResource(id = R.string.save_button_text))
            }
        }
    }

}