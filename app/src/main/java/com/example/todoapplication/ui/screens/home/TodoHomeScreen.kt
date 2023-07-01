package com.example.todoapplication.ui.screens.home

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ScopeUpdateScope
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todoapplication.R
import com.example.todoapplication.ui.AppViewModelProvider
import com.example.todoapplication.ui.TodoTopAppBar
import com.example.todoapplication.ui.navigation.NavigationDestination

object HomeDestination : NavigationDestination {
    override val route: String = "home"
    override val titleRes: Int = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoHomeScreen(
    navigateToTaskEntry: () -> Unit,
    navigateToDetailScreen: (Int) -> Unit,
    viewModel: TodoHomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val uiState = viewModel.homeUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    
    Scaffold(
        topBar = {
            TodoTopAppBar(
                title = stringResource(id = HomeDestination.titleRes),
                canNavigateBack = false
            )
        },
        bottomBar = {
            TodoBottomAppBar(
                updateArchivedState = { viewModel.updateHomeArchivedState() },
                isUnarchivedScreen = viewModel.homeArchivedState.isUnarchivedScreen
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToTaskEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(16.dp)
            ){
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.task_entry_title)
                )
            }
        }
    ) {innerPadding ->
        if (uiState.value.size == 0){
            Text(text = stringResource(id = R.string.task_none_text))
        }else{
            TaskCardList(
                modifier = Modifier.padding(innerPadding),
                homeUiStateList = uiState.value,
                onCheckedChange = {flag, id ->
                    viewModel.updateIsCompleted(flag, id)
                },
                navigateToDetailScreen = navigateToDetailScreen
            )
        }
        
    }
}

@Composable
fun TaskCardList(
    modifier: Modifier = Modifier,
    homeUiStateList: List<HomeUiState>,
    onCheckedChange: (Boolean, Int) -> Unit,
    navigateToDetailScreen: (Int) -> Unit
) {
    LazyColumn(modifier = modifier){
        items(homeUiStateList.size){
            TaskCard(
                homeUiState = homeUiStateList[it],
                onCheckedChange = onCheckedChange,
                modifier = Modifier.clickable(
                    enabled = true
                ){
                    navigateToDetailScreen(homeUiStateList[it].taskDetail.id)
                }
            )
        }
    }
}

@Composable
fun TaskCard(
    homeUiState: HomeUiState,
    onCheckedChange: (Boolean, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val id: Int = homeUiState.taskDetail.id
    Card(
        modifier = modifier.padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ){
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = homeUiState.taskDetail.name)
            TaskProgressDisplayBox(
                labelResId = R.string.progress_label,
                progress = homeUiState.taskDetail.progress.toFloat() / 100f
            )
            if(homeUiState.taskDetail.deadline == null){
                TaskCompletedCheckbox(
                    labelResId = R.string.is_completed_label,
                    taskDetailElement = homeUiState.taskDetail.isCompleted,
                    onClickCheckbox = {onCheckedChange(it, id)})
            }else{
                Row() {
                    TaskDeadlineBox(
                        labelResId = R.string.form_item_deadline_text,
                        deadline = homeUiState.taskDetail.deadline!!,
                        modifier = Modifier.padding(end = 10.dp)
                    )
                    TaskCompletedCheckbox(
                        labelResId = R.string.is_completed_label,
                        taskDetailElement = homeUiState.taskDetail.isCompleted,
                        onClickCheckbox = {onCheckedChange(it, id)}
                    )
                }
            }

        }

    }
}

@Composable
fun TodoBottomAppBar(
    updateArchivedState: () -> Unit,
    isUnarchivedScreen: Boolean
){
    BottomAppBar() {
        Column(){
            IconButton(onClick = updateArchivedState,enabled = !isUnarchivedScreen) {
                Icon(painter = painterResource(id = R.drawable.outline_task_24), contentDescription = null)
            }
            Text(
                text = stringResource(id = R.string.task_list),
                fontSize = 10.sp
            )
        }
        Column(){
            IconButton(onClick = updateArchivedState,enabled = isUnarchivedScreen) {
                Icon(painter = painterResource(id = R.drawable.outline_archive_24), contentDescription = null)
            }
            Text(
                text = stringResource(id = R.string.archived_list),
                fontSize = 10.sp
            )
        }
    }
}
