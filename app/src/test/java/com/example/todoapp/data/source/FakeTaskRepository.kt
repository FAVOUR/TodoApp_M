package com.example.todoapp.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.todoapp.data.Task
import com.example.todoapp.util.Result
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class FakeTaskRepository :TaskRepository{

    var taskDataSource :LinkedHashMap<String,Task> = LinkedHashMap()
    var  observableDataSource : MutableLiveData<Result<List<Task>>> = MutableLiveData()



    override fun observeTasks(): LiveData<Result<List<Task>>> {
        runBlocking { refreshTasks() }
        return  observableDataSource
    }

    override suspend fun getTask(isForceUpdate: Boolean): Result<List<Task>> {
        Timber.d("Dummy tasks", "Gson().toJson(tasks)")

        val tasks=  taskDataSource.values.toList()
            Timber.d("Dummy tasks", Gson().toJson(tasks))
           return  Result.Success(tasks)
    }

    override suspend fun getTask(taskId: String, forceUpdate: Boolean): Result<Task> {
        TODO("Not yet implemented")
    }

    override suspend fun refreshTasks() {
      observableDataSource.value = getTask()
    }

    override suspend fun refreshTasks(taskId: String) {
        TODO("Not yet implemented")
    }

    override fun observeTask(taskId: String): LiveData<Result<Task>> {
        TODO("Not yet implemented")
    }

    override suspend fun saveTask(task: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun completeTask(task: Task) {
        val completedTask = task.copy(isCompleted = true)
        taskDataSource[task.id] = completedTask
        refreshTasks()
    }

    override suspend fun completeTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun activateTask(task: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun activateTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun clearCompletedTasks() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllTasks() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTask(taskId: String) {
        TODO("Not yet implemented")
    }

     fun addTask(vararg tasks:Task){
         Timber.d("Dummy tasks", "Added")

         for(task in tasks ) {

                taskDataSource[task.id] = task
            }
        }


}