package com.example.todoapplication.data

import android.util.Log
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(task: Task)

    @Transaction
    suspend fun insertWithTransaction(task: Task){
        try {
            insert(task)
        }catch (e:Exception){
            Log.d("TaskDaoInsertError", e.toString())
        }
    }

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("SELECT * from tasks WHERE id = :id")
    fun getTask(id: Int): Flow<Task>

    @Query("SELECT * from tasks ORDER BY deadline ASC")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * from tasks WHERE isCompleted = 0 ORDER BY (deadline IS NULL),deadline ASC")
    fun getUncompletedTasks(): Flow<List<Task>>

    @Query("SELECT * from tasks WHERE isCompleted = 1 ORDER BY (deadline IS NULL),deadline ASC")
    fun getCompletedTasks(): Flow<List<Task>>
}