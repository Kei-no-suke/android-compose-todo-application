package com.example.todoapplication.ui.screens.home

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todoapplication.R
import com.example.todoapplication.data.DisplayTaskType
import com.example.todoapplication.ui.AppViewModelProvider
import com.example.todoapplication.ui.TodoTopAppBar
import kotlinx.coroutines.launch

@Composable
fun AddNewTaskScreen(
    displayTaskState: State<DisplayTaskState>,
    taskUiState: TaskUiState,
    updateDisplayTaskState: (DisplayTaskType) -> Unit,
    onDeadlineClickConfirmButton: (Long?) -> Unit,
    onDismissRequest: () -> Unit,
    onCalenderIconClick: () -> Unit,
    onSaveClick: () -> Unit,
    onNameValueChange: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TodoTopAppBar(
                title = stringResource(id = R.string.task_entry_title),
                canNavigateBack = false
            )
        },
        bottomBar = {
            TodoBottomAppBar(
                updateDisplayTaskState = { updateDisplayTaskState(it) },
                displayTaskState = displayTaskState.value.currentDisplayTaskType
            )
        },
    ) {innerPadding ->
        FormItems(
            taskUiState = taskUiState,
            onDeadlineClickConfirmButton = { deadline -> onDeadlineClickConfirmButton(deadline) },
            onDismissRequest = { onDismissRequest() },
            onCalenderIconClick = { onCalenderIconClick() },
            onSaveClick = { onSaveClick() },
            onNameValueChange = {name -> onNameValueChange(name) },
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