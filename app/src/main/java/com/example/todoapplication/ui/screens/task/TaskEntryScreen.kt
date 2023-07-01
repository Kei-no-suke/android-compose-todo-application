package com.example.todoapplication.ui.screens.task

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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

object TaskEntryDestination : NavigationDestination {
    override val route: String = "task_entry"
    override val titleRes: Int = R.string.task_entry_title
    const val taskIdArg = "taskId"
    val routeWithArgs = "$route/{$taskIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEntryScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    viewModel: TaskEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TodoTopAppBar(
                title = stringResource(id = TaskEntryDestination.titleRes),
                canNavigateBack = true
            )
        }
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
                    navigateBack()
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



