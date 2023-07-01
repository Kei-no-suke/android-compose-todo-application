package com.example.todoapplication.data

import kotlinx.coroutines.flow.Flow

class OfflineTasksRepository(private val taskDao: TaskDao) : TasksRepository{
    override fun getAllTasksStream(): Flow<List<Task>> = taskDao.getAllTasks()

    override fun getUncompletedTasksStream(): Flow<List<Task>?> = taskDao.getUncompletedTasks()

    override fun getCompletedTasksStream(): Flow<List<Task>?> = taskDao.getCompletedTasks()

    override fun getTaskStream(id: Int): Flow<Task?> = taskDao.getTask(id)

    override suspend fun insertTask(task: Task) = taskDao.insertWithTransaction(task)

    override suspend fun deleteTask(task: Task) = taskDao.delete(task)

    override suspend fun updateTask(task: Task) = taskDao.update(task)
}