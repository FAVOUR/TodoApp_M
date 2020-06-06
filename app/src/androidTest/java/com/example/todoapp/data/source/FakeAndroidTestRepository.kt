package com.example.todoapp.data.source

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.example.todoapp.data.Task
import com.example.todoapp.util.Result
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.util.LinkedHashMap

class FakeAndroidTestRepository : TaskRepository {

    var tasksServiceData: LinkedHashMap<String, Task> = LinkedHashMap()

    private val observableTasks = MutableLiveData<Result<List<Task>>>()


    override suspend fun refreshTasks() {
        observableTasks.value = getTask()
    }

    override suspend fun refreshTasks(taskId: String) {
        refreshTasks()
    }

    override fun observeTasks(): LiveData<Result<List<Task>>> {
        runBlocking { refreshTasks() }
        return observableTasks
    }

    override fun observeTask(taskId: String): LiveData<Result<Task>> {
        runBlocking { refreshTasks() }
        return observableTasks.map { tasks ->
            Log.i("dummy  observeTask(taskId)", Gson().toJson(observableTasks.value))
            Log.i("dummy  tasks", Gson().toJson(tasks))

            when (tasks) {
                is Result.Loading -> Result.Loading
                is Result.Error -> Result.Error(tasks.exception)
                is Result.Success -> {
                    val task = tasks.data.firstOrNull() { it.id == taskId }
                        ?: return@map Result.Error(Exception("Not found"))
                    Result.Success(task)
                }
            }
        }
    }

    override suspend fun getTask(taskId: String, forceUpdate: Boolean): Result<Task> {
        Log.i("dummy  getTask(taskId: String, forceUpdate: Boolean)", Gson().toJson(tasksServiceData.values))

        tasksServiceData[taskId]?.let {
            return Result.Success(it)
        }
        return Result.Error(Exception("Could not find task"))
    }

    override suspend fun getTask(forceUpdate: Boolean): Result<List<Task>> {
        Log.i("dummy  getTask(forceUpdate: Boolean)", Gson().toJson(tasksServiceData.values))

        return Result.Success(tasksServiceData.values.toList())
    }

    override suspend fun saveTask(task: Task) {
        Log.i("Dummy tasks FakeAndroidRepo", "tasks ${Gson().toJson(task)}")

        tasksServiceData[task.id] = task
    }

    override suspend fun completeTask(task: Task) {
        val completedTask = task.copy(isCompleted = true)
        tasksServiceData[task.id] = completedTask
        refreshTasks()
    }

    override suspend fun completeTask(taskId: String) {
        // Not required for the remote data source.
        throw NotImplementedError()
    }

    override suspend fun activateTask(task: Task) {
        val activeTask = task.copy(isCompleted = false)
        tasksServiceData[task.id] = activeTask
        refreshTasks()
    }

    override suspend fun activateTask(taskId: String) {
        throw NotImplementedError()
    }

    override suspend fun clearCompletedTasks() {
        tasksServiceData = tasksServiceData.filterValues {
            !it.isCompleted
        } as LinkedHashMap<String, Task>
    }

    override suspend fun deleteTask(taskId: String) {
        tasksServiceData.remove(taskId)
        refreshTasks()
    }

    override suspend fun deleteAllTasks() {
        tasksServiceData.clear()
        refreshTasks()
    }

    fun addTasks(vararg tasks: Task) {
        for (task in tasks) {
            tasksServiceData[task.id] = task
        }
        runBlocking { refreshTasks() }
    }
}


//    :TaskRepository {
//
//    var tasksServiceData: LinkedHashMap<String, Task> = LinkedHashMap()
//
//    private var shouldReturnError = false
//
//    private val observableTasks = MutableLiveData<Result<List<Task>>>()
//
//
//    fun setReturnError(value: Boolean) {
//        shouldReturnError = value
//    }
//
//    override fun observeTasks(): LiveData<Result<List<Task>>> {
//        runBlocking { refreshTasks() }
//        return observableTasks
//    }
//
//    override suspend fun getTask(isForceUpdate: Boolean): Result<List<Task>> {
//        Log.e("Dummy tasks getTask(forceUpdate: Boolean)", "shouldReturnError   $shouldReturnError}")
//
//        if (shouldReturnError) {
//            return Result.Error(Exception("Test exception"))
//        }
//        return Result.Success(tasksServiceData.values.toList())
//    }
//
//    override suspend fun getTask(taskId: String, forceUpdate: Boolean): Result<Task> {
//        Log.e("Dummy tasks getTask(taskId: String, forceUpdate: Boolean)", "shouldReturnError   $shouldReturnError taskid $taskId}")
//
//        if (shouldReturnError) {
//
//
//            return Result.Error(Exception("Test exception"))
//        }
//        tasksServiceData[taskId]?.let {
//            return Result.Success(it)
//        }
//        return Result.Error(Exception("Could not find task"))    }
//
//    override suspend fun refreshTasks() {
//        observableTasks.value = getTasks()
//    }
//
//    override suspend fun refreshTasks(taskId: String) {
//        refreshTasks()
//    }
//
//    override fun observeTask(taskId: String): LiveData<Result<Task>> {
//        runBlocking { refreshTasks() }
//        return observableTasks.map { tasks ->
//            when (tasks) {
//                is Result.Loading -> Result.Loading
//                is Result.Error -> Result.Error(tasks.exception)
//                is Result.Success -> {
//                    val task = tasks.data.firstOrNull() { it.id == taskId }
//                        ?: return@map Result.Error(Exception("Not found"))
//                    Result.Success(task)
//                }
//            }
//        }    }
//
//    override suspend fun saveTask(task: Task) {
//
//        Log.e("Dummy tasks saveTask(task:Task)", "First ${Gson().toJson(task)}")
//
//        tasksServiceData[task.id] = task
//
//    }
//
//    override suspend fun completeTask(task: Task) {
//        val completedTask = Task(task.title, task.description, true, task.id)
//        tasksServiceData[task.id] = completedTask    }
//
//    override suspend fun completeTask(taskId: String) {
//        throw NotImplementedError()
//    }
//
//    override suspend fun activateTask(task: Task) {
//        val activeTask = Task(task.title, task.description, false, task.id)
//        tasksServiceData[task.id] = activeTask    }
//
//    override suspend fun activateTask(taskId: String) {
//        throw NotImplementedError()
//    }
//
//    override suspend fun clearCompletedTasks() {
//        tasksServiceData = tasksServiceData.filterValues {
//            !it.isCompleted
//        } as LinkedHashMap<String, Task>    }
//
//    override suspend fun deleteAllTasks() {
//        tasksServiceData.clear()
//        refreshTasks()
//    }
//
//    override suspend fun deleteTask(taskId: String) {
//        tasksServiceData.remove(taskId)
//        refreshTasks()
//    }
//}