package com.example.todoapp

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.example.todoapp.data.source.DefaultTaskRepository
import com.example.todoapp.data.source.TaskRepository
import com.example.todoapp.data.source.TasksDataSource
import com.example.todoapp.data.source.local.TaskLocalDataSource
import com.example.todoapp.data.source.local.db.TaskDao
import com.example.todoapp.data.source.local.db.ToDoDataBase
import com.example.todoapp.data.source.remote.TaskRemoteDataSource
import kotlinx.coroutines.runBlocking

object ServiceLocator {

    var dataBase :ToDoDataBase? =null
    @Volatile
    var taskRepository :TaskRepository? =null

    //Create a TaskRepository and if created return the already instantiated instance

     fun provideRepository(context: Context):TaskRepository{
       synchronized(this) {
           return taskRepository ?: taskDataRepository(context)
       }
    }
//
    //Create the TaskDataRepository
     private fun taskDataRepository(context: Context):TaskRepository{
       val  newRepo = DefaultTaskRepository(TaskRemoteDataSource,createLocalDataSource(context))

        taskRepository=newRepo

        return  newRepo
    }

    //Create a TaskDataSource
    private fun createLocalDataSource(context: Context):TasksDataSource{
        val dataBase = dataBase ?: createDatabase(context)

        return  TaskLocalDataSource(dataBase.taskDoa())
    }

    //Create the repository
    private fun createDatabase(context: Context):ToDoDataBase{

        var dbCreated = Room.databaseBuilder(context,ToDoDataBase::class.java,"Tasks.db")
                                        .fallbackToDestructiveMigration()
                                        .build()
        dataBase=dbCreated

        return dbCreated
    }


    private val lock = Any()

    @VisibleForTesting
    fun resetRepository() {
        synchronized(lock) {
            runBlocking {
                TaskRemoteDataSource.deleteAllTasks()
            }
            // Clear all data to avoid test pollution.
            dataBase?.apply {
                clearAllTables()
                close()
            }
            dataBase = null
            taskRepository = null
        }
    }

}