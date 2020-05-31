package com.example.todoapp

import androidx.lifecycle.LiveData
import com.example.todoapp.data.Task
import com.example.todoapp.data.source.TasksDataSource
import com.example.todoapp.util.Result

class FakeDataSource(val tasks:MutableList<Task>?) : TasksDataSource{
    override fun observeTasks(): LiveData<Result<List<Task>>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTasks(): Result<List<Task>> {
        tasks?.let {
            return Result.Success<ArrayList<Task>>(ArrayList(tasks))
        }

        return  Result.Error(Exception("No Task Found"))

    }

    override suspend fun refreshTasks() {
        TODO("Not yet implemented")
    }

    override fun observeTask(taskId: String): LiveData<Result<Task>> {
        TODO("Not yet implemented")
    }

    override suspend fun getTask(taskId: String): Result<Task> {

        TODO("Not yet implemented")

    }

    override suspend fun refreshTask(taskId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun saveTask(task: Task) {
              tasks?.add(task)
    }

    override suspend fun completeTask(task: Task) {
        TODO("Not yet implemented")
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
                tasks?.clear()
            }

    override suspend fun deleteTask(taskId: String) {
        TODO("Not yet implemented")
    }
}