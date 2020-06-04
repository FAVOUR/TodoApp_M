package com.example.todoapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.add_task.viewmodel.AddTaskViewModel
import com.example.todoapp.data.source.TaskRepository
import com.example.todoapp.statistics.viewmodel.StatisticsViewModel
import com.example.todoapp.task.viewmodel.TaskViewModel
import com.example.todoapp.task_detail.viewmodel.TaskDetailViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory (val  tasksRepository : TaskRepository):ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            with(modelClass){
                when{
                    isAssignableFrom(AddTaskViewModel::class.java) -> AddTaskViewModel(tasksRepository)
                    isAssignableFrom(StatisticsViewModel::class.java) -> StatisticsViewModel(tasksRepository)
                    isAssignableFrom(TaskDetailViewModel::class.java) -> TaskDetailViewModel(tasksRepository)
                   isAssignableFrom(TaskViewModel::class.java) -> TaskViewModel(tasksRepository)

                    else ->              throw Throwable("the class $modelClass is not an instance of viewmodel ")

                }

            }
          if (modelClass.isAssignableFrom(TaskViewModel::class.java)){
               return  TaskViewModel(tasksRepository) as T
          }else{
              throw Throwable("the class $modelClass is not an instance of viewmodel ")
          }
    }



//
//    override fun <T : ViewModel> create(modelClass: Class<T>) =
//        with(modelClass) {
//            when {
//                isAssignableFrom(StatisticsViewModel::class.java) ->
//                    StatisticsViewModel(tasksRepository)
//                isAssignableFrom(TaskDetailViewModel::class.java) ->
//                    TaskDetailViewModel(tasksRepository)
//                isAssignableFrom(AddEditTaskViewModel::class.java) ->
//                    AddEditTaskViewModel(tasksRepository)
//                isAssignableFrom(TasksViewModel::class.java) ->
//                    TasksViewModel(tasksRepository)
//                else ->
//                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
//            }
//        } as T


}