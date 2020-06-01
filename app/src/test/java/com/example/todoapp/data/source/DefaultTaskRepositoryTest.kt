package com.example.todoapp.data.source

import com.example.todoapp.FakeDataSource
import com.example.todoapp.data.Task
import org.junit.Assert.*
import org.junit.Before

class DefaultTaskRepositoryTest(){

    var task1 = Task("Title_1"  ,"Descreption 1 ")
    var task2= Task("Title_2"  ,"Descreption 2 ")
    var task3 = Task("Title_3"  ,"Descreption 3 ")

    private val remoteTasks = listOf(task1, task2).sortedBy { it.id }
    private val localTasks = listOf(task3).sortedBy { it.id }
    private val newTasks = listOf(task3).sortedBy { it.id }


//     @Before
//     fun start
}