package com.example.todoapplication.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.todoapplication.ui.screens.home.HomeDestination
import com.example.todoapplication.ui.screens.home.TodoHomeScreen
import com.example.todoapplication.ui.screens.task.TaskEditDestination
import com.example.todoapplication.ui.screens.task.TaskEditScreen

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
            arguments = listOf(navArgument(TaskEditDestination.taskIdArg){
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