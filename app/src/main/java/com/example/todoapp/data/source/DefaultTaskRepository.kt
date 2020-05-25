package com.example.todoapp.data.source

import android.app.Application
import androidx.room.Room
import com.example.todoapp.data.Task
import com.example.todoapp.data.source.local.TaskLocalDataSource
import com.example.todoapp.data.source.local.db.ToDoDataBase
import com.example.todoapp.data.source.remote.TaskRemoteDataSource
import com.example.todoapp.util.Result
import kotlinx.coroutines.*

class DefaultTaskRepository private constructor(application: Application) {

//    private val tasksRemoteDataSource: TasksDataSource
    private val tasksRemoteDataSource: TaskRemoteDataSource
    private val tasksLocalDataSource: TaskLocalDataSource
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO


        companion object{
            @Volatile
            private var INSTANCE :DefaultTaskRepository? =null

            fun getRepository(app:Application) :DefaultTaskRepository{
                return  INSTANCE ?: synchronized(this){
                    DefaultTaskRepository(app).also {
                        INSTANCE=it
                    }
                }
            }

        }

    init {
        val database = Room.databaseBuilder(application.applicationContext,
            ToDoDataBase::class.java, "Tasks.db")
            .build()

        tasksRemoteDataSource = TaskRemoteDataSource
        tasksLocalDataSource = TaskLocalDataSource(database.taskDoa())
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

    suspend fun refresh(){
        updateTasksFromRemoteDataSource()
    }

    suspend fun  refresh(taskId: String){
        updateTaskFromRemoteDataSource(taskId)

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