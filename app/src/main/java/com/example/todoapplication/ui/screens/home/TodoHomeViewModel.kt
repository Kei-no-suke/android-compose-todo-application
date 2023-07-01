package com.example.todoapplication.ui.screens.home

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

class TodoHomeViewModel(
    private val tasksRepository: TasksRepository
): ViewModel() {

    // 未アーカイブタスクのuiStateの取得
    val homeUiState: StateFlow<List<HomeUiState>> = tasksRepository.getUnarchivedTasksStream()
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

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class HomeUiState(
    val taskDetail: TaskDetail = TaskDetail()
)