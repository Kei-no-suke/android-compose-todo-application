package com.example.todoapplication.ui.screens.task

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.todoapplication.R
import com.example.todoapplication.ui.AppViewModelProvider
import com.example.todoapplication.ui.navigation.NavigationDestination
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todoapplication.data.Task
import com.example.todoapplication.ui.TodoTopAppBar
import com.example.todoapplication.ui.screens.home.TaskCompletedCheckbox
import com.example.todoapplication.ui.screens.home.TaskDetailItem

object TaskDetailDestination : NavigationDestination {
    override val route: String = "task_detail"
    override val titleRes: Int = R.string.task_detail_title
    const val taskIdArg = "taskId"
    val routeWithArgs = "$route/{$taskIdArg}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    navigateToEditItem: (Int) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TaskDetailViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(topBar = {
        TodoTopAppBar(
            title = stringResource(TaskDetailDestination.titleRes),
            canNavigateBack = true,
            navigateUp = navigateBack
        )
    },
    floatingActionButton = {
        FloatingActionButton(
            onClick = {navigateToEditItem(uiState.value.taskDetail.id)},
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = stringResource(id = R.string.task_edit_title)
            )
        }
    }, modifier = modifier) {innerPadding ->
        TaskDetailCard(
            taskDetailUiState = uiState.value,
            onClickCheckbox = {viewModel.updateIsCompleted(it)},
            modifier = Modifier.padding(innerPadding)
                .clickable(){
                    navigateToEditItem(uiState.value.taskDetail.id)
                }
        )
    }
}

@Composable
fun TaskDetailCard(
    taskDetailUiState: TaskDetailUiState, modifier: Modifier = Modifier,
    onClickCheckbox: (Boolean) -> Unit
){
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TaskDetailItem(
                labelResId = R.string.task_name_label,
                taskDetailElement = taskDetailUiState.taskDetail.name
            )
            TaskCompletedCheckbox(
                labelResId = R.string.is_completed_label,
                taskDetailElement = taskDetailUiState.taskDetail.isCompleted,
                onClickCheckbox = { onClickCheckbox(it) }
            )
        }
    }

}



