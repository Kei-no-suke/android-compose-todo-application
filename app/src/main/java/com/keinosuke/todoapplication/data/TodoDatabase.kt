package com.keinosuke.todoapplication.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Task::class], version = 4, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var Instance: TodoDatabase? = null

        fun getDatabase(context: Context): TodoDatabase {
            return Instance ?: synchronized(this){
                Room.databaseBuilder(context, TodoDatabase::class.java, "task_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}