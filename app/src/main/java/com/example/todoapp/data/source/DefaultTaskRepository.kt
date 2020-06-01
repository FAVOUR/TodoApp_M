package com.example.todoapp.data.source

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.todoapp.data.Task
import com.example.todoapp.data.source.local.TaskLocalDataSource
import com.example.todoapp.data.source.local.db.ToDoDataBase
import com.example.todoapp.data.source.remote.TaskRemoteDataSource
import com.example.todoapp.util.Result
import kotlinx.coroutines.*
import javax.sql.DataSource

//class DefaultTaskRepository private constructor(application: Application) {
class DefaultTaskRepository (  private val tasksRemoteDataSource:TasksDataSource,
                                                 private val tasksLocalDataSource: TasksDataSource,
                                                 private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) {

//    private val tasksRemoteDataSource: TasksDataSource
//    private val tasksRemoteDataSource:tasksRemoteDataSource
//    private val tasksLocalDataSource: TasksDataSource
//    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO


        companion object{
            @Volatile
            private var INSTANCE :DefaultTaskRepository? =null

            fun getRepository(app:Application) :DefaultTaskRepository{
                return  INSTANCE ?: synchronized(this){
                    val database = Room.databaseBuilder(app.applicationContext,
                        ToDoDataBase::class.java, "Tasks.db")
                        .build()
                    DefaultTaskRepository(TaskRemoteDataSource,TaskLocalDataSource(database.taskDoa()),Dispatchers.IO).also {
                        INSTANCE=it
                    }
                }
            }

        }

    init {

//
//        tasksRemoteDataSource = TaskRemoteDataSource
//        tasksLocalDataSource = TaskLocalDataSource(database.taskDoa())
}


    fun observeTasks(): LiveData<Result<List<Task>>> {
        return tasksLocalDataSource.observeTasks()
    }


    suspend fun getTask(isForceUpdate : Boolean = false):Result<List<Task>>{
        if(isForceUpdate) {
            try {
                updateTasksFromRemoteDataSource()
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
        return tasksLocalDataSource.getTasks()

    }

    suspend fun getTask(taskId: String,  forceUpdate: Boolean = false): Result<Task> {
        if (forceUpdate) {
            updateTaskFromRemoteDataSource(taskId)
        }
        return tasksLocalDataSource.getTask(taskId)
    }


    suspend fun updateTasksFromRemoteDataSource(){
          val remoteDataSource = tasksRemoteDataSource.getTasks()

          if (remoteDataSource is Result.Success){
              // Real apps might want to do a proper sync.
              tasksLocalDataSource.deleteAllTasks()
              remoteDataSource.data.forEach {
                  tasksLocalDataSource.saveTask(it)
              }
          }else if (remoteDataSource is Result.Error){
                   throw remoteDataSource.exception
          }

    }

    private suspend fun updateTaskFromRemoteDataSource(taskId: String) {
        val remoteTask = tasksRemoteDataSource.getTask(taskId)

        if (remoteTask is Result.Success) {
            tasksLocalDataSource.saveTask(remoteTask.data)
        }
    }

    suspend fun refreshTasks(){
        updateTasksFromRemoteDataSource()
    }

    suspend fun refreshTasks(taskId: String){
        updateTaskFromRemoteDataSource(taskId)

    }

    fun observeTask(taskId: String): LiveData<Result<Task>> {
        return tasksLocalDataSource.observeTask(taskId)
    }

    suspend fun saveTask(task:Task){
        coroutineScope {
            launch { tasksRemoteDataSource.saveTask(task) }
            launch { tasksLocalDataSource.saveTask(task) }
        }
    }

    suspend fun completeTask(task: Task) {
        coroutineScope {
            launch { tasksRemoteDataSource.completeTask(task) }
            launch { tasksLocalDataSource.completeTask(task) }
        }
    }

    suspend fun completeTask(taskId: String) {
        withContext(ioDispatcher) {
            (getTaskWithId(taskId) as? Result.Success)?.let { it ->
                completeTask(it.data)
            }
        }
    }

    suspend fun activateTask(task: Task) = withContext<Unit>(ioDispatcher) {
        coroutineScope {
            launch { tasksRemoteDataSource.activateTask(task) }
            launch { tasksLocalDataSource.activateTask(task) }
        }
    }

    suspend fun activateTask(taskId: String) {
        withContext(ioDispatcher) {
            (getTaskWithId(taskId) as? Result.Success)?.let { it ->
                activateTask(it.data)
            }
        }
    }

    suspend fun clearCompletedTasks() {
        coroutineScope {
            launch { tasksRemoteDataSource.clearCompletedTasks() }
            launch { tasksLocalDataSource.clearCompletedTasks() }
        }
    }

    suspend fun deleteAllTasks() {
        withContext(ioDispatcher) {
            coroutineScope {
                launch { tasksRemoteDataSource.deleteAllTasks() }
                launch { tasksLocalDataSource.deleteAllTasks() }
            }
        }
    }

    suspend fun deleteTask(taskId: String) {
        coroutineScope {
            launch { tasksRemoteDataSource.deleteTask(taskId) }
            launch { tasksLocalDataSource.deleteTask(taskId) }
        }
    }

    private suspend fun getTaskWithId(id: String): Result<Task> {
        return tasksLocalDataSource.getTask(id)
    }




}