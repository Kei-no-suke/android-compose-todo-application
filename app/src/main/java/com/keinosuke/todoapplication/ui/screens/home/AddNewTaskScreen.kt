package com.keinosuke.todoapplication.ui.screens.home

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.keinosuke.todoapplication.R
import com.keinosuke.todoapplication.data.DisplayTaskType
import com.keinosuke.todoapplication.ui.TodoTopAppBar

@Composable
fun AddNewTaskScreen(
    formUiState: FormUiState,
    displayTaskState: State<DisplayTaskState>,
    taskUiState: TaskUiState,
    updateDisplayTaskState: (DisplayTaskType) -> Unit,
    onDeadlineClickConfirmButton: (Long?) -> Unit,
    onDismissRequest: () -> Unit,
    onCalenderIconClick: () -> Unit,
    onSaveClick: () -> Unit,
    onNameValueChange: (String) -> Unit,
    resetAddScreen: () -> Unit
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
                displayTaskState = displayTaskState.value.currentDisplayTaskType,
                resetAddScreen = resetAddScreen
            )
        },
    ) {innerPadding ->
        FormItems(
            formUiState = formUiState,
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
    formUiState: FormUiState,
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
                onValueChange = onNameValueChange,
                isDisplayNeeded = true
            )
            DatePickerDialogArea(
                labelResId = R.string.form_item_deadline_text,
                inputFieldLong = taskUiState.taskDetail.deadline,
                isDisplay = taskUiState.isDisplayDeadlineDatePicker,
                onClickConfirmButton = {deadline -> onDeadlineClickConfirmButton(deadline)},
                onDismissRequest = onDismissRequest,
                onCalenderIconClick = onCalenderIconClick
            )
            Row{
                Text(
                    text = "*",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(start = 18.dp)
                )
                Text(text = "は必須項目です")
            }
            if(formUiState.alertDisplay){
                Text(
                    text = stringResource(R.string.task_validate_error_message),
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(start = 18.dp)
                )
            }
            // セーブボタン
            Button(onClick = onSaveClick,modifier = Modifier.padding(8.dp)) {
                Text(text = stringResource(id = R.string.save_button_text))
            }
        }
    }

}