package com.example.todoapp

import android.content.Context
import androidx.room.Room
import com.example.todoapp.data.source.TaskRepository
import com.example.todoapp.data.source.TasksDataSource
import com.example.todoapp.data.source.local.db.TaskDao
import com.example.todoapp.data.source.local.db.ToDoDataBase

object ServiceLocator {

    //Create a TaskRepository and if created return the already instantiated instance
//     fun provideRepository():TasksDataSource{
//
//    }
//
//    //Create the TaskDataRepository
//     fun taskDataRepository():TaskRepository{
//
//    }
//
//    //Create a TaskDataSource
//    fun createLocalDataSource():TaskDataSource{
//
//    }

    //Create the repository
    fun createDatabase(context: Context):ToDoDataBase{

        var dbCreated = Room.databaseBuilder(context,ToDoDataBase::class.java,"todo_db")
                                        .fallbackToDestructiveMigration()
                                        .build()
        return dbCreated
    }

}