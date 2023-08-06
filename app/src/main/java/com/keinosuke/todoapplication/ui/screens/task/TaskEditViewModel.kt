package com.keinosuke.todoapplication.ui.screens.task

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.keinosuke.todoapplication.data.TasksRepository
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.keinosuke.todoapplication.ui.screens.home.FormUiState
import com.keinosuke.todoapplication.ui.screens.home.TaskDetail
import com.keinosuke.todoapplication.ui.screens.home.TaskUiState
import com.keinosuke.todoapplication.ui.screens.home.toTask
import com.keinosuke.todoapplication.ui.screens.home.toTaskUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

class TaskEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val tasksRepository: TasksRepository
) : ViewModel() {

    var taskUiState by mutableStateOf(TaskUiState())
        private set

    private val _formUiState = MutableStateFlow(FormUiState())
    val formUiState: StateFlow<FormUiState> = _formUiState

    private val taskId: Int = checkNotNull(savedStateHandle[TaskEditDestination.taskIdArg])

    init {
        viewModelScope.launch {
            taskUiState = tasksRepository.getTaskStream(taskId)
                .filterNotNull()
                .first()
                .toTaskUiState(isEntryValid = true)
        }
    }

    suspend fun updateTask(): Boolean{
        val validatedFlag = validateInput(taskUiState.taskDetail)
        if(validatedFlag){
            tasksRepository.updateTask(taskUiState.taskDetail.toTask())
        }else{
            _formUiState.update { it.copy(
                alertDisplay = true
            ) }
        }
        return validatedFlag
    }

    suspend fun deleteTask() {
        tasksRepository.deleteTask(taskUiState.taskDetail.toTask())
    }

    private fun updateUiState(taskDetail: TaskDetail) {
        val currentProgress = taskDetail.progress.toIntOrNull() ?: 0
        val newTaskDetail =
            if(currentProgress == 100)
            {
                taskDetail.copy(
                    isCompleted = true,
                    completedDate = Date().time
                )
            }else{
                taskDetail.copy(
                    isCompleted = false,
                    completedDate = null
                )
            }
        taskUiState = TaskUiState(taskDetail = newTaskDetail, isEntryValid = validateInput(taskDetail))
        if(taskUiState.isEntryValid){
            _formUiState.update { it.copy(
                alertDisplay = false
            ) }
        }
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