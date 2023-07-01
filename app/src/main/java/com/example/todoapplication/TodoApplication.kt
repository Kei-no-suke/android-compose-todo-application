package com.example.todoapplication

import android.app.Application
import com.example.todoapplication.data.AppContainer
import com.example.todoapplication.data.AppDataContainer

class TodoApplication: Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}