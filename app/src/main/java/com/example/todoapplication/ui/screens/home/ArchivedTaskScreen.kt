package com.example.todoapplication.ui.screens.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todoapplication.R
import com.example.todoapplication.ui.AppViewModelProvider
import com.example.todoapplication.ui.TodoTopAppBar

@Composable
fun ArchivedTaskScreen(
    viewModel: TodoHomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
//    val uiState = viewModel.homeUiState.collectAsState()
//    val coroutineScope = rememberCoroutineScope()
//
//    Scaffold(
//        topBar = {
//            TodoTopAppBar(
//                title = stringResource(id = HomeDestination.titleRes),
//                canNavigateBack = false
//            )
//        },
//        bottomBar = {
//            TodoBottomAppBar(
//                updateHomeScreenState = { viewModel.updateHomeArchivedState(it) },
//                homeScreenState = viewModel.homeArchivedState.homeScreenState
//            )
//        }
//    ) {innerPadding ->
//        if (uiState.value.size == 0){
//            Text(text = stringResource(id = R.string.task_none_text))
//        }else{
//            TaskCardList(
//                modifier = Modifier.padding(innerPadding),
//                homeUiStateList = uiState.value,
//                onCheckedChange = {flag, id ->
//                    viewModel.updateIsCompleted(flag, id)
//                },
//                navigateToDetailScreen = { }
//            )
//        }
//
//    }
}