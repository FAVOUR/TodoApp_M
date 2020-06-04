package com.example.todoapp

import android.app.Application
import com.example.todoapp.data.source.TaskRepository
import timber.log.Timber

class TodoApplication :Application() {

     val taskRepository:TaskRepository
             get() = ServiceLocator.provideRepository(this)


    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}