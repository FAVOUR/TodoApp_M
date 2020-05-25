package com.example.todoapp.data.source.local

import androidx.lifecycle.LiveData
import com.example.todoapp.data.Task
import com.example.todoapp.data.source.TasksDataSource
import com.example.todoapp.util.Result

class TaskLocalDataSource : TasksDataSource {
    override fun observeTasks(): LiveData<Result<List<Task>>> {
    }

    override suspend fun getTasks(): Result<List<Task>> {
    }

    override suspend fun refreshTasks() {
    }

    override fun observeTask(taskId: String): LiveData<Result<Task>> {
    }

    override suspend fun getTask(taskId: String): Result<Task> {
    }

    override suspend fun refreshTask(taskId: String) {
    }

    override suspend fun saveTask(task: Task) {
    }

    override suspend fun completeTask(task: Task) {
    }

    override suspend fun completeTask(taskId: String) {
    }

    override suspend fun activateTask(task: Task) {
    }

    override suspend fun activateTask(taskId: String) {
    }

    override suspend fun clearCompletedTasks() {
    }

    override suspend fun deleteAllTasks() {

    }

    override suspend fun deleteTask(taskId: String) {

    }
}