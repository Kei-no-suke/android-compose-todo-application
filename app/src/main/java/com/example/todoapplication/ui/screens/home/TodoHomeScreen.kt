package com.example.todoapplication.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todoapplication.R
import com.example.todoapplication.ui.AppViewModelProvider
import com.example.todoapplication.ui.TodoApp
import com.example.todoapplication.ui.TodoTopAppBar
import com.example.todoapplication.ui.navigation.NavigationDestination
import com.example.todoapplication.ui.theme.TodoApplicationTheme

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
                updateHomeScreenState = { viewModel.updateHomeArchivedState(it) },
                homeScreenState = viewModel.homeArchivedState.homeScreenState
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
    updateHomeScreenState: (String) -> Unit,
    homeScreenState: String
){
    var isArchived: Boolean
    var isUnarchived: Boolean
    var isAdd: Boolean
    var isEdit: Boolean
    when(homeScreenState){
        HomeScreenState.Archive.name -> {
            isArchived = true
            isUnarchived = false
            isAdd = false
            isEdit = false
        }
        HomeScreenState.Add.name -> {
            isArchived = false
            isUnarchived = false
            isAdd = true
            isEdit = false
        }
        HomeScreenState.Edit.name -> {
            isArchived = false
            isUnarchived = false
            isAdd = false
            isEdit = true
        }
        else -> {
            isArchived = false
            isUnarchived = true
            isAdd = false
            isEdit = false
        }
    }
    val activeColor = MaterialTheme.colorScheme.primary
    val nonActiveColor = MaterialTheme.colorScheme.onSurface
    val unableColor = MaterialTheme.colorScheme.outlineVariant

    val unarchivedColor = if(isUnarchived){ activeColor }else{ nonActiveColor }
    val archiveColor = if(isArchived){ activeColor }else{ nonActiveColor }
    val editColor = if(isEdit){ activeColor }else{ unableColor }
    val addColor = if(isAdd){ activeColor }else{ nonActiveColor }
    BottomAppBar(

    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable(enabled = !isUnarchived){
                updateHomeScreenState(HomeScreenState.Unarchived.name)
            }.weight(1f)
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
            modifier = Modifier.clickable(enabled = !isArchived){
                updateHomeScreenState(HomeScreenState.Archive.name)
            }.weight(1f)
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
            modifier = Modifier.clickable(enabled = !isAdd){
                updateHomeScreenState(HomeScreenState.Add.name)
            }.weight(1f)
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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable(enabled = false){}.weight(1f)
        ){
            Icon(
                painter = painterResource(id = R.drawable.baseline_edit_24),
                contentDescription = null,
                tint = editColor
            )
            Text(
                text = stringResource(id = R.string.edit_button_text),
                fontSize = 10.sp,
                color = editColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomAppBarPreview() {
    TodoApplicationTheme(useDarkTheme = false) {
        TodoBottomAppBar({}, HomeScreenState.Unarchived.name)
    }
}
