package com.example.todoapp

import android.app.Application
import androidx.annotation.VisibleForTesting
import com.example.todoapp.data.source.TaskRepository
import timber.log.Timber

class TodoApplication :Application() {

     var taskRepository:TaskRepository?=null
         get() = ServiceLocator.provideRepository(this)
         @VisibleForTesting set



    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}