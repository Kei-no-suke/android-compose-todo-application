package com.example.todoapplication.ui.screens.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapplication.data.TasksRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.todoapplication.data.Task

enum class HomeScreenState{
    Archive, Unarchived, Add, Edit
}

class TodoHomeViewModel(
    private val tasksRepository: TasksRepository
): ViewModel() {


    // == Add画面用 =================================================
    // タスクの状態
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


    // == home画面用 ================================================
    // ホーム画面の状態 | Archive, Unarchived, Add |
    var homeArchivedState by mutableStateOf(HomeArchivedState(homeScreenState = HomeScreenState.Unarchived.name))
        private set

    private val taskStream =
        if(homeArchivedState.homeScreenState == HomeScreenState.Unarchived.name)
        { tasksRepository.getArchivedTasksStream() }
        else
        { tasksRepository.getUnarchivedTasksStream() }

    // 未アーカイブタスクのuiStateの取得
    val homeUiState: StateFlow<List<HomeUiState>> = taskStream
        .map{List ->
            List!!.map{
                HomeUiState(it.toTaskDetail())
            }
    }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = listOf()
    )

    // タスクを完了済みに変更する機能
    // タスク完了と同時に完了日の設定を行う
    fun updateIsCompleted(isCompletedFlag: Boolean = false, id: Int){
        viewModelScope.launch {
            val currentTask = homeUiState.value.find{ it.taskDetail.id == id }!!.taskDetail.toTask()
            tasksRepository.updateTask(currentTask.copy(
                isCompleted = isCompletedFlag,
                completedDate = if(isCompletedFlag){ Date() }else{ null }
            ))
        }
    }

    // ホーム画面の表示状態を更新する | Archive, Unarchived, Add |
    fun updateHomeArchivedState(homeScreenState: String){
        homeArchivedState = HomeArchivedState(homeScreenState)
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class HomeUiState(
    val taskDetail: TaskDetail = TaskDetail()
)

data class HomeArchivedState(
    val homeScreenState: String
)

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
    val completedDate: Long? = null,
    val isArchived: Boolean = false
)

fun TaskDetail.toTask(): Task = Task(
    id = id,
    name = name,
    progress = progress.toIntOrNull() ?: 0,
    deadline = if(deadline == null){ null }else{ Date(deadline) },
    isCompleted = isCompleted,
    completedDate = if(completedDate == null){ null }else{ Date(completedDate) },
    isArchived = isArchived
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
    completedDate = if(completedDate == null){ null }else{ completedDate.time },
    isArchived = isArchived
)


