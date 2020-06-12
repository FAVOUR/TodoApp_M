package com.example.todoapp.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.todoapp.TestCouroutinUtilAT

import com.example.todoapp.TodoApplication
import com.example.todoapp.data.Task
import com.example.todoapp.data.source.local.db.ToDoDataBase
import com.example.todoapp.util.Result
import com.example.todoapp.util.succeeded
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

//@MediumTest
//@ExperimentalCoroutinesApi
//@RunWith(AndroidJUnit4::class)

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class TaskLocalDataSourceTest{

    lateinit var dataBase: ToDoDataBase
    lateinit var localDataSource: TaskLocalDataSource

    @get:Rule
    val archiRule =InstantTaskExecutorRule()

    @get:Rule
    @ExperimentalCoroutinesApi
    var mainCouroutine: TestCouroutinUtilAT = TestCouroutinUtilAT()


    @Before
    fun initDbandDatasource(){
        var applicationDatabase= ApplicationProvider.getApplicationContext<TodoApplication>()
        dataBase= Room.inMemoryDatabaseBuilder(applicationDatabase,ToDoDataBase::class.java)
                        .allowMainThreadQueries()
                        .build()

        localDataSource= TaskLocalDataSource(dataBase.taskDoa(),Dispatchers.Main)
    }

    @After
    fun removeDatabase(){
        dataBase.close()
    }


    @Test
    fun createTask_saveTask_getAllTask()=mainCouroutine.runBlockingTest{
        var task = Task("New Task","Just inserted a Task")

        localDataSource.saveTask(task)

         val savedTask= localDataSource.getTask(task.id)

        assertThat(savedTask.succeeded,`is`(true))

         savedTask as Result.Success
          assertThat(savedTask.data.title , `is`(task.title))
          assertThat(savedTask.data.description , `is`(task.description))
          assertThat(savedTask.data.id , `is`(task.id))
    }

    @Test
    fun saveTask_completeTask_retrievedTaskIsComplete()=mainCouroutine.runBlockingTest{
          //Given
        val task = Task("Task at first ","This Task will be completed")

        localDataSource.saveTask(task)

        //When
        localDataSource.completeTask(task.copy(isCompleted = true))

        val _savedTask = localDataSource.getTask(task.id)

        //Assert
        assertThat(_savedTask.succeeded,`is` (true))

           _savedTask as Result.Success
        assertThat(_savedTask.data.id , `is` (task.id))
        assertThat(_savedTask.data.isCompleted , `is` (true))
//        assertThat(savedTask.description , `is` (task.description))



    }


}