package com.example.todoapp.task.viewmodel

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.databinding.Observable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.todoapp.Event
import com.example.todoapp.addObserver
import com.example.todoapp.task.util.TasksFilterType
import org.hamcrest.Matchers.*
import org.jetbrains.annotations.NotNull
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(AndroidJUnit4::class)
class TaskViewModelTest{

    lateinit var  taskViewModel: TaskViewModel

    @get:Rule
    var instantTaskExecutorRule =InstantTaskExecutorRule()

    @Before
    fun setupViewModel(){

        //Given
        var app = ApplicationProvider.getApplicationContext<Application>()

        taskViewModel= TaskViewModel(app)

    }
    @Test
    fun addTask_setsNewEventTask(){




        var observer = Observer<Event<Unit>>{}

        try {
//            taskVM.newTaskEvent.observeForever(observer)


            //When
            taskViewModel.addNewTask()


            var value = taskViewModel.newTaskEvent.addObserver()

            assertThat(value?.getContentIfNotHandled(), (not(nullValue())))

        }finally {
            taskViewModel.newTaskEvent.removeObserver(observer)
        }
    }


    @Test
    fun setFilterAllTasks_tasksAddViewVisible() {

//        var app= ApplicationProvider.getApplicationContext<Application>()
//        // Given a fresh ViewModel
//
//        var taskViewModel = TaskViewModel(app)
        // When the filter type is ALL_TASKS

        taskViewModel.setFiltering(TasksFilterType.ALL_TASKS)

        var value =taskViewModel.tasksAddViewVisible.addObserver()

        // Then the "Add task" action is visible

        assertThat(value, `is` (true) )


    }
}