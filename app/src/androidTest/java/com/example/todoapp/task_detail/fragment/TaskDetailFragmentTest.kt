package com.example.todoapp.task_detail.fragment

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.runner.AndroidJUnitRunner
import com.example.todoapp.R
import com.example.todoapp.data.Task
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@MediumTest
@RunWith(AndroidJUnit4::class)
class TaskDetailFragmentTest{

    @Test
    fun activeTask_displayedOnUi(){
        // GIVEN - Add active (incomplete) task to the DB
        val task =  Task("Active Task","Androidx Rocks",false)

        // WHEN - Details fragment launched to display task
          val bundle =TaskDetailFragmentArgs(task.id).toBundle()

          launchFragmentInContainer<TaskDetailFragment>(bundle, R.style.AppTheme)

        //Makes the display delay for 2 seconds
        Thread.sleep(2000)
    }
}