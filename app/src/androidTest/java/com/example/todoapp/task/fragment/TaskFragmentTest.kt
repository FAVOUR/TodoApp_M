package com.example.todoapp.task.fragment

import android.content.Context
import android.content.res.Resources
import android.graphics.ColorSpace.match
import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.todoapp.ADD_EDIT_RESULT_OK
import com.example.todoapp.R
import com.example.todoapp.ServiceLocator
import com.example.todoapp.TodoApplication
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
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*

@MediumTest
@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
class TaskFragmentTest{
     lateinit var taskRepository :TaskRepository
     lateinit var context : Context
    @Before
    fun setup(){
        taskRepository = FakeAndroidTestRepository()

        ServiceLocator.taskRepository=taskRepository

        context= ApplicationProvider.getApplicationContext()
    }

    @After
    fun done(){
       ServiceLocator.resetRepository()
    }

     @Test
      fun checkNavigation_navigatetonTaskDetails()= runBlockingTest{



          var task = Task("Start Here","Testing the Task Fragment", false,"id1")

          val bundle = TaskFragmentArgs(ADD_EDIT_RESULT_OK).toBundle()

         taskRepository.saveTask(task)

             val senerio = launchFragmentInContainer<TaskFragment>(Bundle(), R.style.AppTheme)

              var navcontroller = mock(NavController::class.java)

           senerio.onFragment {

              Navigation.setViewNavController(it.view!!,navcontroller)
           }

           onView(withId(R.id.tasks_list)).perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
               hasDescendant(withText("Start Here")),click()
           ))

         verify(navcontroller).navigate(
             TaskFragmentDirections.actionTasksFragmentToTaskDetailFragment("id1")
         )

      }
@Test
    fun fragment_clickAddTaskButton_navigateToAddTaskFragment()= runBlockingTest{

//        val task =Task("Welcome","Lets Number",false)
//        taskRepository.saveTask(task)


        val scenerio =  launchFragmentInContainer<TaskFragment>(Bundle(), R.style.AppTheme)

         val controller = mock(NavController::class.java)

        scenerio.onFragment {
            Navigation.setViewNavController(it.view!!,controller)
        }

        onView(withId(R.id.add_task_fab)).perform(click())

        verify(controller).navigate(TaskFragmentDirections.actionTasksFragmentToAddEditTaskFragment(null ,context.resources.getString(R.string.add_task)))





    }

}