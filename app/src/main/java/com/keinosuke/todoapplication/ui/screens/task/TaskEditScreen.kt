package com.keinosuke.todoapplication.ui.screens.task

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.keinosuke.todoapplication.R
import com.keinosuke.todoapplication.ui.AppViewModelProvider
import com.keinosuke.todoapplication.ui.TodoTopAppBar
import com.keinosuke.todoapplication.ui.navigation.NavigationDestination
import com.keinosuke.todoapplication.ui.screens.home.DatePickerDialogArea
import com.keinosuke.todoapplication.ui.screens.home.FormUiState
import com.keinosuke.todoapplication.ui.screens.home.TextFieldArea
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
    val formUiState = viewModel.formUiState.collectAsState()

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
            formUiState = formUiState.value,
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
                    val validateFlag = viewModel.updateTask()
                    if(validateFlag){
                        navigateBack()
                    }
                }
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EditFormItem(
    formUiState: FormUiState,
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
        TextFieldArea(
            labelResId = R.string.task_name_label,
            inputFieldText = nameText,
            onValueChange = onNameTextFieldChange,
            isDisplayNeeded = true
        )
        TextFieldArea(
            labelResId = R.string.progress_label,
            inputFieldText = progressText,
            onValueChange = onProgressTextFieldChange,
            isDisplayUnit = true,
            isDisplayNeeded = true,
            unitText = "%"
        )
        DatePickerDialogArea(
            labelResId = R.string.form_item_deadline_text,
            inputFieldLong = deadlineValue,
            isDisplay = isDisplayDeadlineDatePicker,
            onClickConfirmButton = { deadline -> onClickDeadlineConfirmButton(deadline) },
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
        Button(onClick = onSaveClick, modifier = Modifier.padding(8.dp)) {
            Text(text = stringResource(id = R.string.save_button_text))
        }
    }
}