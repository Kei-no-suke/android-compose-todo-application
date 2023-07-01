package com.example.todoapplication.ui.screens.task

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.todoapplication.data.TasksRepository
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class TaskEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val tasksRepository: TasksRepository
) : ViewModel() {

    var taskUiState by mutableStateOf(TaskUiState())
        private set

    private val taskId: Int = checkNotNull(savedStateHandle[TaskEditDestination.taskIdArg])

    init {
        viewModelScope.launch {
            taskUiState = tasksRepository.getTaskStream(taskId)
                .filterNotNull()
                .first()
                .toTaskUiState(isEntryValid = true)
        }
    }

    suspend fun updateTask() {
        if(validateInput(taskUiState.taskDetail)){
            tasksRepository.updateTask(taskUiState.taskDetail.toTask())
        }
    }

    suspend fun deleteTask() {
        tasksRepository.deleteTask(taskUiState.taskDetail.toTask())
    }

    fun updateUiState(taskDetail: TaskDetail) {
        taskUiState = TaskUiState(taskDetail = taskDetail, isEntryValid = validateInput(taskDetail))
    }

    fun updateIsDisplayDeadlineDatePicker(){
        val currentTaskUiState = taskUiState
        taskUiState = currentTaskUiState.copy(isDisplayDeadlineDatePicker = !currentTaskUiState.isDisplayDeadlineDatePicker)
    }

    fun updateDeadline(deadline: Long?){
        val currentTaskUiState = taskUiState
        val currentTaskDetail = currentTaskUiState.taskDetail.copy(deadline = deadline)
        updateUiState(taskDetail = currentTaskDetail)
    }

    fun updateName(name: String){
        val currentTaskUiState = taskUiState
        val currentTaskDetail = currentTaskUiState.taskDetail.copy(name = name)
        updateUiState(taskDetail = currentTaskDetail)
    }

    fun updateProgress(progress: String){
        val currentTaskUiState = taskUiState
        val currentTaskDetail = currentTaskUiState.taskDetail.copy(progress = progress)
        updateUiState(taskDetail = currentTaskDetail)
    }

    private fun validateInput(uiState: TaskDetail = taskUiState.taskDetail): Boolean {
        return uiState.name.isNotBlank()
    }
}