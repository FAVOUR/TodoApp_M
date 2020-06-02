package com.example.todoapp.data.source

import androidx.lifecycle.LiveData
import com.example.todoapp.data.Task
import com.example.todoapp.util.Result

interface TaskRepository {
    fun observeTasks(): LiveData<Result<List<Task>>>

    suspend fun getTask(isForceUpdate : Boolean = false): Result<List<Task>>
    suspend fun getTask(taskId: String, forceUpdate: Boolean = false): Result<Task>

    suspend fun refreshTasks()
    suspend fun refreshTasks(taskId: String)
    fun observeTask(taskId: String): LiveData<Result<Task>>

    suspend fun saveTask(task: Task)
    suspend fun completeTask(task: Task)

    suspend fun completeTask(taskId: String)

    suspend fun activateTask(task: Task)

    suspend fun activateTask(taskId: String)

    suspend fun clearCompletedTasks()

    suspend fun deleteAllTasks()

    suspend fun deleteTask(taskId: String)
}