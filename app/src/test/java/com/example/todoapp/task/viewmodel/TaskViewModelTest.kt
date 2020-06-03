package com.example.todoapp.task.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.todoapp.Event
import com.example.todoapp.addObserver
import com.example.todoapp.data.Task
import com.example.todoapp.data.source.FakeTaskRepository
import com.example.todoapp.data.source.TaskRepository
import com.example.todoapp.task.util.TasksFilterType
import org.hamcrest.Matchers.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TaskViewModelTest{

    lateinit var  taskViewModel: TaskViewModel

    @get:Rule
    var instantTaskExecutorRule =InstantTaskExecutorRule()

    @Before
    fun setupViewModel(){

        //Given
//        var app = ApplicationProvider.getApplicationContext<Application>()


        // We initialise the tasks to 3, with one active and two completed
       var tasksRepository : FakeTaskRepository   = FakeTaskRepository()
        val task1 = Task("Title1", "Description1")
        val task2 = Task("Title2", "Description2", true)
        val task3 = Task("Title3", "Description3", true)
        tasksRepository.addTask(task1, task2, task3)


        taskViewModel= TaskViewModel(tasksRepository)

    }
    @Test
    fun addTask_setsNewEventTask(){


        var observer = Observer<Event<Unit>>{}

        try {
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

        // When the filter type is ALL_TASKS
        taskViewModel.setFiltering(TasksFilterType.ALL_TASKS)


        var value =taskViewModel.tasksAddViewVisible.addObserver()


        // Then the "Add task" action is visible
        assertThat(value, `is` (true) )

    }
}