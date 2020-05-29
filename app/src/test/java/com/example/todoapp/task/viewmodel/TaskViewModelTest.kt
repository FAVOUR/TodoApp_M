package com.example.todoapp.task.viewmodel

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(AndroidJUnit4::class)
class TaskViewModelTest{

    @Test
    fun addTask_setsNewEventTask(){

        //Given
        var app = ApplicationProvider.getApplicationContext<Application>()

        var taskVM= TaskViewModel(app)

        //When
         taskVM.addNewTask()

        //
    }
}