package com.example.todoapp.data.source.local

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.todoapp.data.Task
import com.example.todoapp.data.source.local.db.ToDoDataBase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.notNullValue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class TaskDaoTest {

    lateinit var database:ToDoDataBase

    @get:Rule
    var rest=InstantTaskExecutorRule()


    @Before
    fun intiDataBase(){
        var context:Context= ApplicationProvider.getApplicationContext()
           database = Room.inMemoryDatabaseBuilder(context,ToDoDataBase::class.java)
                           .allowMainThreadQueries()
                           .build()
    }

    @After
    fun cleanup(){
        database.close()
    }


    @Test
    fun createTask_saveTask_taskItemEqualsSavedItem()= runBlockingTest{
        var task = Task("Androidx","Test")

        System.out.println("Task id task ${task.id}")

        database.taskDoa().insertTask(task)

        val taskSaved=database.taskDoa().getTaskById(task.id)

        System.out.println("Task id  taskSaved ${taskSaved?.id}")


        assertThat(taskSaved as Task , notNullValue())
        assertThat(taskSaved?.id, `is`(task?.id))
        assertThat(taskSaved?.title,`is`(task?.title))
        assertThat(taskSaved?.description,`is`(task?.description))
    }


    @Test
    fun createTaskandInsertTask_updateTask_getTaskById () = runBlockingTest{
        var task = Task("Telli","Person")
        System.out.println("Task id  task ${task?.id}")

         database.taskDoa().insertTask(task)

        var task2 = Task("Outstanding ","Only ", true, task.id)
//        System.out.println("Task id  task2 ${task2?.id}")

        database.taskDoa().updateTask(task2)

        var savedValue = database.taskDoa().getTaskById(task.id)
        var savedValue2 = database.taskDoa().getTaskById(task2.id)

//        System.out.println("Task id  savedValue2 ${savedValue2}")
//
//
//        System.out.println("Task id  savedValue ${savedValue}")

        assertThat(savedValue as Task, notNullValue())
        assertThat(savedValue.id,`is` (task.id))
        assertThat(savedValue.description,`is` (task2.description))
        assertThat(savedValue.title,`is` (task2.title))


    }
}