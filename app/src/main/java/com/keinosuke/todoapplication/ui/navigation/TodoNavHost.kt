package com.keinosuke.todoapplication.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.keinosuke.todoapplication.ui.screens.home.HomeDestination
import com.keinosuke.todoapplication.ui.screens.home.TodoHomeScreen
import com.keinosuke.todoapplication.ui.screens.task.TaskEditDestination
import com.keinosuke.todoapplication.ui.screens.task.TaskEditScreen

@Composable
fun TodoNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    NavHost(navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ){
        composable(route = HomeDestination.route){
            TodoHomeScreen(
                navigateToEditScreen = { navController.navigate("${TaskEditDestination.route}/${it}") }
            )
        }

        composable(
            route = TaskEditDestination.routeWithArgs,
            arguments = listOf(navArgument(TaskEditDestination.TASK_ID_ARG){
                type = NavType.IntType
            })
        ){
            TaskEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}