package com.example.todoapp.data.source

import android.util.Log
import com.example.todoapp.FakeDataSource
import com.example.todoapp.TestCouroutinUtil
import com.example.todoapp.data.Task
import com.example.todoapp.data.source.remote.TaskRemoteDataSource
import com.example.todoapp.util.Result
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.core.IsEqual
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DefaultTaskRepositoryTest(){

    var task1 = Task("Title_1"  ,"Descreption 1 ")
    var task2= Task("Title_2"  ,"Descreption 2 ")
    var task3 = Task("Title_3"  ,"Descreption 3 ")

    private val remoteTasks = listOf(task1, task2).sortedBy { it.id }
    private val localTasks = listOf(task3).sortedBy { it.id }
    private val newTasks = listOf(task3).sortedBy { it.id }

    lateinit var tasksRemoteDataSource: TasksDataSource
    lateinit var tasksLocalDataSource: TasksDataSource
    lateinit var defaultTaskRepository: DefaultTaskRepository


    @ExperimentalCoroutinesApi
    @get:Rule
    val mainCouroutin= TestCouroutinUtil()

     @Before
     fun  createRepository(){
         tasksRemoteDataSource = FakeDataSource(remoteTasks.toMutableList())
         tasksLocalDataSource = FakeDataSource(localTasks.toMutableList())
         defaultTaskRepository= DefaultTaskRepository(tasksRemoteDataSource,tasksLocalDataSource,Dispatchers.Main)
     }

    @Test
    fun getTask_requestTaskFromRemoteDataSource()=mainCouroutin.runBlockingTest {

       val  task = defaultTaskRepository.getTask(isForceUpdate = true) as Result.Success
            System.out.println("Data ${Gson().toJson(task)}")

              assertThat(task.data, IsEqual(remoteTasks))

        }


    }
}