package com.keinosuke.todoapplication.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.keinosuke.todoapplication.TodoApplication
import com.keinosuke.todoapplication.ui.screens.home.TodoHomeViewModel
import com.keinosuke.todoapplication.ui.screens.task.TaskEditViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        // TodoHomeViewModelの初期化
        initializer {
            TodoHomeViewModel(todoApplication().container.tasksRepository)
        }

        // TaskEditViewModelの初期化
        initializer {
            TaskEditViewModel(
                this.createSavedStateHandle(),
                todoApplication().container.tasksRepository
            )
        }
    }
}

fun CreationExtras.todoApplication(): TodoApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as TodoApplication)