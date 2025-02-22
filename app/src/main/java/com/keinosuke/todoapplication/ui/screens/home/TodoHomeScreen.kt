package com.keinosuke.todoapplication.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.keinosuke.todoapplication.R
import com.keinosuke.todoapplication.data.DisplayTaskType
import com.keinosuke.todoapplication.ui.AppViewModelProvider
import com.keinosuke.todoapplication.ui.navigation.NavigationDestination
import com.keinosuke.todoapplication.ui.theme.TodoApplicationTheme
import kotlinx.coroutines.launch

object HomeDestination : NavigationDestination {
    override val route: String = "home"
    override val titleRes: Int = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoHomeScreen(
    navigateToEditScreen: (Int) -> Unit,
    viewModel: TodoHomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val unarchiveUiState = viewModel.unarchiveUiState.collectAsState()
    val archiveUiState = viewModel.archiveUiState.collectAsState()
    val displayTaskState = viewModel.displayTaskState.collectAsState()
    val formUiState = viewModel.formUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    when(displayTaskState.value.currentDisplayTaskType){
        DisplayTaskType.Unarchived -> {
            UnarchivedTaskScreen(
                uiState = unarchiveUiState,
                displayTaskState = displayTaskState,
                updateDisplayTaskState = { viewModel.updateDisplayTaskState(it) },
                onClickCheckbox = { flag, id ->
                    viewModel.updateIsCompleted(flag, id) },
                onArchiveButtonClick = { id ->
                    viewModel.updateUnarchiveIsArchived(id)
                },
                navigateToEditScreen = { id, progress ->
                    navigateToEditScreen(id)
                    viewModel.updateProgress(progress = progress, id = id)
                },
                resetAddScreen = { viewModel.resetAddScreen() }
            )
        }
        DisplayTaskType.Add -> {
            AddNewTaskScreen(
                formUiState = formUiState.value,
                displayTaskState = displayTaskState,
                taskUiState = viewModel.taskUiState,
                updateDisplayTaskState = { viewModel.updateDisplayTaskState(it) },
                onDeadlineClickConfirmButton = { deadline ->
                    viewModel.updateDeadline(deadline)
                    viewModel.updateIsDisplayDeadlineDatePicker()
                },
                onDismissRequest = { viewModel.updateIsDisplayDeadlineDatePicker() },
                onCalenderIconClick = { viewModel.updateIsDisplayDeadlineDatePicker() },
                onSaveClick = {
                    coroutineScope.launch {
                        val validateFlag = viewModel.saveTask()
                        if(validateFlag){
                            viewModel.updateDisplayTaskState(DisplayTaskType.Unarchived)
                        }
                    }
                },
                onNameValueChange = { name -> viewModel.updateName(name) },
                resetAddScreen = { viewModel.resetAddScreen() }
            )
        }
        DisplayTaskType.Archive -> {
            ArchivedTaskScreen(
                uiState = archiveUiState,
                displayTaskState = displayTaskState,
                updateDisplayTaskState = { viewModel.updateDisplayTaskState(it) },
                onArchiveButtonClick = { viewModel.updateArchiveIsArchived(it) },
                onDeleteButtonClick = { viewModel.deleteArchiveTask(it) },
                resetAddScreen = { viewModel.resetAddScreen() }
            )
        }
    }
}

@Composable
fun TodoBottomAppBar(
    updateDisplayTaskState: (DisplayTaskType) -> Unit,
    displayTaskState: DisplayTaskType,
    resetAddScreen: () -> Unit
){
    var isArchived: Boolean
    var isUnarchived: Boolean
    var isAdd: Boolean
    when(displayTaskState){
        DisplayTaskType.Archive -> {
            isArchived = true
            isUnarchived = false
            isAdd = false
        }
        DisplayTaskType.Add -> {
            isArchived = false
            isUnarchived = false
            isAdd = true
        }
        else -> {
            isArchived = false
            isUnarchived = true
            isAdd = false
        }
    }
    val activeColor = MaterialTheme.colorScheme.primary
    val nonActiveColor = MaterialTheme.colorScheme.onSurface

    val unarchivedColor = if(isUnarchived){ activeColor }else{ nonActiveColor }
    val archiveColor = if(isArchived){ activeColor }else{ nonActiveColor }
    val addColor = if(isAdd){ activeColor }else{ nonActiveColor }
    BottomAppBar(

    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clickable(enabled = !isUnarchived) {
                    updateDisplayTaskState(DisplayTaskType.Unarchived)
                }
                .weight(1f)
        ){
            Icon(
                painter = painterResource(id = R.drawable.outline_task_24),
                contentDescription = null,
                tint = unarchivedColor
            )
            Text(
                text = stringResource(id = R.string.task_list),
                fontSize = 10.sp,
                color = unarchivedColor
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clickable(enabled = !isArchived) {
                    updateDisplayTaskState(DisplayTaskType.Archive)
                }
                .weight(1f)
        ){
            Icon(
                painter = painterResource(id = R.drawable.outline_archive_24),
                contentDescription = null,
                tint = archiveColor
            )
            Text(
                text = stringResource(id = R.string.archived_list),
                fontSize = 10.sp,
                color = archiveColor
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clickable(enabled = !isAdd) {
                    updateDisplayTaskState(DisplayTaskType.Add)
                    resetAddScreen()
                }
                .weight(1f)
        ){
            Icon(
                painter = painterResource(id = R.drawable.baseline_add_task_24),
                contentDescription = null,
                tint = addColor
            )
            Text(
                text = stringResource(id = R.string.add_button_text),
                fontSize = 10.sp,
                color = addColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomAppBarPreview() {
    TodoApplicationTheme(useDarkTheme = false) {
        TodoBottomAppBar({}, DisplayTaskType.Unarchived, {})
    }
}
