package com.keinosuke.todoapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val progress: Int,
    val deadline: Date?,
    val isCompleted: Boolean,
    val completedDate: Date?,
    val isArchived: Boolean
)
