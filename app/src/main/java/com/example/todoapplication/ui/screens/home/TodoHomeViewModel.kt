package com.example.todoapplication.ui.screens.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapplication.data.TasksRepository
import com.example.todoapplication.ui.screens.task.TaskDetail
import com.example.todoapplication.ui.screens.task.toTask
import com.example.todoapplication.ui.screens.task.toTaskDetail
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

enum class HomeScreenState{
    Archive, Unarchived, Add, Edit
}

class TodoHomeViewModel(
    private val tasksRepository: TasksRepository
): ViewModel() {

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


