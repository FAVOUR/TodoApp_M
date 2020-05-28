package com.example.todoapp.data.source.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.example.todoapp.data.Task
import com.example.todoapp.data.source.TasksDataSource
import com.example.todoapp.util.Result
import kotlinx.coroutines.delay
import java.lang.Error

object  TaskRemoteDataSource : TasksDataSource {

    private const val SERVICE_LATENCY_IN_MILLIS = 2000L

    private var TASKS_SERVICE_DATA = LinkedHashMap<String, Task>(2)

    init {
        addTask("Build tower in Pisa", "Ground looks good, no foundation work required.")
        addTask("Finish bridge in Tacoma", "Found awesome girders at half the cost!")
    }

    private val observableTasks = MutableLiveData<Result<List<Task>>>()



    private fun addTask(title:String, description:String){
        val newTask = Task(title, description)
        TASKS_SERVICE_DATA[newTask.id] =newTask

    }

    override fun observeTasks(): LiveData<Result<List<Task>>> {

        return  observableTasks
    }

    override suspend fun getTasks(): Result<List<Task>> {
        // Simulate network by delaying the execution.
        val tasks = TASKS_SERVICE_DATA.values.toList()
        delay(SERVICE_LATENCY_IN_MILLIS)
        return Result.Success(tasks)
    }

    override suspend fun refreshTasks() {
        observableTasks.value = getTasks()

    }

    override fun observeTask(taskId: String): LiveData<Result<Task>> {

        return  observableTasks?.map{ tasks ->
            when(tasks){
                is Result.Success -> {
                    var task = tasks.data.firstOrNull() {it.id == taskId} ?: return@map Result.Error( Exception("Item Not Found "))
                       Result.Success(task)
                }
                is Result.Loading ->{Result.Loading}
                is Result.Error ->{Result.Error(tasks.exception)}
            }

        }

    }

    //TODO I Still do not understand how this code works
    override suspend fun getTask(taskId: String): Result<Task> {
        // Simulate network by delaying the execution.
        delay(SERVICE_LATENCY_IN_MILLIS)
        TASKS_SERVICE_DATA[taskId]?.let {
            return Result.Success(it)
        }
        return Result.Error(Exception("Task not found"))
    }



    override suspend fun refreshTask(taskId: String) {
        refreshTasks()
    }

    override suspend fun saveTask(task: Task) {
        TASKS_SERVICE_DATA[task.id] = task
    }

    override suspend fun completeTask(task: Task) {
        val completedTask = Task(task.title,task.description,true,task.id)
        TASKS_SERVICE_DATA[task.id]=completedTask
    }

    override suspend fun completeTask(taskId: String) {
    }

    override suspend fun activateTask(task: Task) {
        val activeTask = Task(task.title, task.description, false, task.id)
        TASKS_SERVICE_DATA[task.id] = activeTask
    }

    override suspend fun activateTask(taskId: String) {
    }

    override suspend fun clearCompletedTasks() {
        TASKS_SERVICE_DATA=TASKS_SERVICE_DATA.filterValues {
            !it.isCompleted
        }as LinkedHashMap
    }

    override suspend fun deleteAllTasks() {
        TASKS_SERVICE_DATA.clear()
    }

    override suspend fun deleteTask(taskId: String) {
        TASKS_SERVICE_DATA.remove(taskId)
    }
}