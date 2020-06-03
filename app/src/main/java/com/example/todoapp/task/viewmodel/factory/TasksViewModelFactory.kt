package com.example.todoapp.task.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.data.source.TaskRepository
import com.example.todoapp.task.viewmodel.TaskViewModel

@Suppress("UNCHECKED_CAST")
class TasksViewModelFactory (val  tasksRepository : TaskRepository):ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
          if (modelClass.isAssignableFrom(TaskViewModel::class.java)){
               return  TaskViewModel(tasksRepository) as T
          }else{
              throw Throwable("the class $modelClass is not an instance of viewmodel ")
          }
    }


}