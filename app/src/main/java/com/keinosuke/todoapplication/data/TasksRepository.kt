package com.keinosuke.todoapplication.data

import kotlinx.coroutines.flow.Flow

interface TasksRepository {

    fun getAllTasksStream(): Flow<List<Task>>

    fun getUncompletedTasksStream(): Flow<List<Task>?>

    fun getCompletedTasksStream(): Flow<List<Task>?>

    fun getUnarchivedTasksStream(): Flow<List<Task>?>

    fun getArchivedTasksStream(): Flow<List<Task>?>

    fun getTaskStream(id: Int): Flow<Task?>

    suspend fun insertTask(task: Task)

    suspend fun deleteTask(task: Task)

    suspend fun updateTask(task: Task)
}