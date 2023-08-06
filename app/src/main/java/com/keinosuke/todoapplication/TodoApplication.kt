package com.keinosuke.todoapplication

import android.app.Application
import com.keinosuke.todoapplication.data.AppContainer
import com.keinosuke.todoapplication.data.AppDataContainer

class TodoApplication: Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}