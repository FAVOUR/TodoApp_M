package com.example.todoapp.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
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


@SmallTest
@RunWith(AndroidJUnit4::class)
class TaskLocalDataSourceTest{

    lateinit var dataBase: ToDoDataBase
    lateinit var localDataSource: TaskLocalDataSource

    @get:Rule
    val archiRule =InstantTaskExecutorRule()

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
    @ExperimentalCoroutinesApi
    fun createTask_saveTask_getAllTask()= runBlockingTest{
        var task = Task("New Task","Just inserted a Task")

        localDataSource.saveTask(task)

         val savedTask= localDataSource.getTask(task.id)

        assertThat(savedTask.succeeded,`is`(true))

         savedTask as Result.Success
          assertThat(savedTask.data.title , `is`(task.title))
          assertThat(savedTask.data.description , `is`(task.description))
          assertThat(savedTask.data.id , `is`(task.id))
    }


}