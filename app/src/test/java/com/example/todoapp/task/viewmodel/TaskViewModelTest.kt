package com.example.todoapp.task.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.databinding.Observable
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.todoapp.Event
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.nullValue
import org.jetbrains.annotations.NotNull
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(AndroidJUnit4::class)
class TaskViewModelTest{

    @get:Rule
    var instantTaskExecutorRule =InstantTaskExecutorRule()
    @Test
    fun addTask_setsNewEventTask(){

        //Given
        var app = ApplicationProvider.getApplicationContext<Application>()

        var taskVM= TaskViewModel(app)


        //

        var observer = Observer<Event<Unit>>{}

        try {
            taskVM.newTaskEvent.observeForever(observer)


            //When
            taskVM.addNewTask()


            var value = taskVM.newTaskEvent.value

            assertThat(value?.getContentIfNotHandled(), (not(nullValue())))

        }finally {
            taskVM.newTaskEvent.removeObserver(observer)
        }
    }
}