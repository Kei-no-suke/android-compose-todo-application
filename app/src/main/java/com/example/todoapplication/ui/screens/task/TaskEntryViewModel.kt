package com.example.todoapplication.ui.screens.task

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.todoapplication.data.TasksRepository
import java.util.Date
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.todoapplication.data.Task

class TaskEntryViewModel(private val tasksRepository: TasksRepository) : ViewModel(){
    var taskUiState by mutableStateOf(TaskUiState())
        private set

    fun updateUiState(taskDetail: TaskDetail){
        val currentTaskUiState = taskUiState
        taskUiState = currentTaskUiState.copy(taskDetail = taskDetail, isEntryValid = validateInput(taskDetail))
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

    fun updateIsDisplayDeadlineDatePicker(){
        val currentTaskUiState = taskUiState
        taskUiState = currentTaskUiState.copy(isDisplayDeadlineDatePicker = !currentTaskUiState.isDisplayDeadlineDatePicker)
    }

    suspend fun saveTask() {
        if(validateInput()){
            tasksRepository.insertTask(taskUiState.taskDetail.toTask())
        }
    }

    private fun validateInput(uiState: TaskDetail = taskUiState.taskDetail): Boolean {
        return uiState.name.isNotBlank()
    }
}

data class TaskUiState(
    val taskDetail: TaskDetail = TaskDetail(),
    val isEntryValid: Boolean = false,
    val isDisplayDeadlineDatePicker: Boolean = false
)

data class TaskDetail(
    val id: Int = 0,
    val name: String = "",
    val progress: String = "",
    val deadline: Long? = null,
    val isCompleted: Boolean = false,
    val completedDate: Long? = null
)

fun TaskDetail.toTask(): Task = Task(
    id = id,
    name = name,
    progress = progress.toIntOrNull() ?: 0,
    deadline = if(deadline == null){ null }else{ Date(deadline) },
    isCompleted = isCompleted,
    completedDate = if(completedDate == null){ null }else{ Date(completedDate) }
)

fun Task.toTaskUiState(isEntryValid: Boolean = false, isDisplayDeadlineDatePicker: Boolean = false): TaskUiState = TaskUiState(
    taskDetail = this.toTaskDetail(),
    isEntryValid = isEntryValid,
    isDisplayDeadlineDatePicker = isDisplayDeadlineDatePicker
)

fun Task.toTaskDetail(): TaskDetail = TaskDetail(
    id = id,
    name = name,
    progress = progress.toString(),
    deadline = if(deadline == null){ null }else{ deadline.time },
    isCompleted = isCompleted,
    completedDate = if(completedDate == null){ null }else{ completedDate.time }
)