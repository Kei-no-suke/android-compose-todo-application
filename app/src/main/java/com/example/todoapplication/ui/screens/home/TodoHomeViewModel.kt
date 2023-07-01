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

class TodoHomeViewModel(
    private val tasksRepository: TasksRepository
): ViewModel() {

    // ホーム画面に表示しているタスクが未アーカイブかアーカイブ済みかの状態
    var homeArchivedState by mutableStateOf(HomeArchivedState())
        private set

    private val taskStream = if(homeArchivedState.isUnarchivedScreen){ tasksRepository.getUnarchivedTasksStream() }
    else { tasksRepository.getArchivedTasksStream() }

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

    // ホーム画面の表示状態を更新する(アーカイブ済み or 未アーカイブ)
    fun updateHomeArchivedState(){
        val currentState = homeArchivedState.isUnarchivedScreen
        homeArchivedState = HomeArchivedState(!currentState)
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class HomeUiState(
    val taskDetail: TaskDetail = TaskDetail()
)

data class HomeArchivedState(
    val isUnarchivedScreen: Boolean = true
)
