package com.example.todoapp.task_detail.fragment

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.runner.AndroidJUnitRunner
import com.example.todoapp.R
import com.example.todoapp.ServiceLocator
import com.example.todoapp.data.Task
import com.example.todoapp.data.source.FakeAndroidTestRepository
import com.example.todoapp.data.source.TaskRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class TaskDetailFragmentTest{

    private lateinit var repository: TaskRepository

    @Before
    fun initRepository() {
        repository = FakeAndroidTestRepository()
        ServiceLocator.taskRepository = repository
    }

    @After
    fun cleanupDb() = runBlockingTest {
        ServiceLocator.resetRepository()
    }

    @Test
    fun activeTask_displayedOnUi()= runBlockingTest{
        // GIVEN - Add active (incomplete) task to the DB
        val task =  Task("Active Task","Androidx Rocks",false)

        repository.saveTask(task)

        // WHEN - Details fragment launched to display task
          val bundle =TaskDetailFragmentArgs(task.id).toBundle()




        launchFragmentInContainer<TaskDetailFragment>(bundle, R.style.AppTheme)

        //Makes the display delay for 2 seconds
        Thread.sleep(2000)
    }
}