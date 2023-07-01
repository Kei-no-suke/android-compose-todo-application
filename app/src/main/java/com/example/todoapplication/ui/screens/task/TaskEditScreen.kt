package com.example.todoapplication.ui.screens.task

import androidx.compose.foundation.layout.Column
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
import com.example.todoapplication.ui.navigation.NavigationDestination
import kotlinx.coroutines.launch

object TaskEditDestination : NavigationDestination {
    override val route: String = "task_edit"
    override val titleRes: Int = R.string.task_edit_title
    const val taskIdArg = "taskId"
    val routeWithArgs = "$route/{$taskIdArg}"
}

@Composable
fun TaskEditScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TaskEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TodoTopAppBar(
                title = stringResource(id = TaskEditDestination.titleRes),
                canNavigateBack = true,
                navigateUp = onNavigateUp
            )
        }
    ) {innerPadding ->
        EditFormItem(
            nameText = viewModel.taskUiState.taskDetail.name,
            onNameTextFieldChange = { name -> viewModel.updateName(name) },
            progressText = viewModel.taskUiState.taskDetail.progress,
            onProgressTextFieldChange = { progress -> viewModel.updateProgress(progress) },
            deadlineValue = viewModel.taskUiState.taskDetail.deadline,
            isDisplayDeadlineDatePicker = viewModel.taskUiState.isDisplayDeadlineDatePicker,
            onClickDeadlineConfirmButton = {deadline ->
                viewModel.updateIsDisplayDeadlineDatePicker()
                viewModel.updateDeadline(deadline)
            },
            onDismissRequest = { viewModel.updateIsDisplayDeadlineDatePicker() },
            onCalenderIconClick = { viewModel.updateIsDisplayDeadlineDatePicker() },
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.updateTask()
                    navigateBack()
                }
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun EditFormItem(
    nameText: String,
    onNameTextFieldChange: (String) -> Unit,
    progressText: String,
    onProgressTextFieldChange: (String) -> Unit,
    deadlineValue: Long?,
    isDisplayDeadlineDatePicker: Boolean,
    onClickDeadlineConfirmButton: (Long?) -> Unit,
    onDismissRequest: () -> Unit,
    onCalenderIconClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier){
        if(nameText != ""){
            TextFieldArea(
                labelResId = R.string.task_name_label,
                inputFieldText = nameText,
                onValueChange = onNameTextFieldChange
            )
            TextFieldArea(
                labelResId = R.string.progress_label,
                inputFieldText = progressText,
                onValueChange = onProgressTextFieldChange,
                isDisplayUnit = true,
                unitText = "%"
            )
            DatePickerDialogArea(
                labelResId = R.string.form_item_deadline_text,
                inputFieldText = deadlineValue,
                isDisplay = isDisplayDeadlineDatePicker,
                onClickConfirmButton = { deadline -> onClickDeadlineConfirmButton(deadline) },
                onDismissRequest = onDismissRequest,
                onCalenderIconClick = onCalenderIconClick
            )
        }
        // セーブボタン
        Button(onClick = onSaveClick) {
            Text(text = stringResource(id = R.string.save_button_text))
        }
    }
}