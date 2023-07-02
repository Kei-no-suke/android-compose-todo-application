package com.example.todoapplication.ui.screens.task

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapplication.data.TasksRepository
import com.example.todoapplication.ui.screens.home.TaskDetail
import com.example.todoapplication.ui.screens.home.toTask
import com.example.todoapplication.ui.screens.home.toTaskDetail
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date

class TaskDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val tasksRepository: TasksRepository
): ViewModel() {
    private val taskId: Int = checkNotNull(savedStateHandle[TaskDetailDestination.taskIdArg])

    val uiState: StateFlow<TaskDetailUiState> =
        tasksRepository.getTaskStream(taskId)
            .filterNotNull()
            .map {
                TaskDetailUiState(taskDetail = it.toTaskDetail())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = TaskDetailUiState()
            )

    fun updateIsCompleted(isCompletedFlag: Boolean = false) {
        viewModelScope.launch {
            val currentTask = uiState.value.taskDetail.toTask()
            tasksRepository.updateTask(currentTask.copy(
                isCompleted = isCompletedFlag,
                completedDate = if(isCompletedFlag){ Date() }else{ null }
            ))
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class TaskDetailUiState(
    val taskDetail: TaskDetail = TaskDetail()
)