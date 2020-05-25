package com.example.todoapp.data.source.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.todoapp.data.Task
import com.example.todoapp.data.source.TasksDataSource
import com.example.todoapp.data.source.local.db.TaskDao
import com.example.todoapp.util.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Error
import java.lang.Exception

class TaskLocalDataSource internal constructor(
    private val tasksDao: TaskDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : TasksDataSource {
    override fun observeTasks(): LiveData<Result<List<Task>>> {
     return tasksDao.observeTasks().map {
         Result.Success(it)
     }
    }

    override suspend fun getTasks(): Result<List<Task>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(tasksDao.getTask())
        }catch(e:Exception){
            Result.Error(e)
        }
    }

    override suspend fun refreshTasks() {
    }

    override suspend fun refreshTask(taskId: String) {
    }


    override fun observeTask(taskId: String): LiveData<Result<Task>> {
        return  tasksDao.observeTaskById(taskId).map {
            Result.Success(it)
        }
    }

    override suspend fun getTask(taskId: String): Result<Task> = withContext(ioDispatcher){

        val task =tasksDao.getTaskById(taskId)
        try {

        if (task != null ){
            return@withContext Result.Success(task)
        }else{
            return@withContext  Result.Error(Exception("No Task Found"))

        }
        }catch (e:Exception){
            return@withContext  Result.Error(e)
        }
    }


    override suspend fun saveTask(task: Task) =withContext(ioDispatcher) {
        tasksDao.insertTask(task)

    }

    override suspend fun completeTask(task: Task)  = withContext(ioDispatcher) {
        tasksDao.updateCompleted(task.id    ,true)
    }

    override suspend fun completeTask(taskId: String)  = withContext(ioDispatcher) {
            tasksDao.updateCompleted(taskId,true)
    }

    override suspend fun activateTask(task: Task)  = withContext(ioDispatcher) {
        tasksDao.updateCompleted(task.id,false)

    }

    override suspend fun activateTask(taskId: String) = withContext(ioDispatcher) {
        tasksDao.updateCompleted(taskId,false)

    }

    override suspend fun clearCompletedTasks()  = withContext(ioDispatcher) {
        tasksDao.deleteCompletedTask()
    }

    override suspend fun deleteAllTasks() = withContext(ioDispatcher) {
       tasksDao.deleteTask()
    }

    override suspend fun deleteTask(taskId: String)  = withContext(ioDispatcher) {
       tasksDao.deleteTaskById(taskId)
    }
}