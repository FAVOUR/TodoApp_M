package com.example.todoapp.statistics.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.todoapp.data.source.DefaultTaskRepository
import com.example.todoapp.statistics.util.getActiveAndCompletedStats
import com.example.todoapp.util.Result
import kotlinx.coroutines.launch

class StatisticsViewModel(application: Application) :AndroidViewModel(application) {

    private val defaultTaskRepository = DefaultTaskRepository.getRepository(application)

    private val task = defaultTaskRepository.observeTasks()

    private var mDataLoading = MutableLiveData<Boolean>(false)

    private val stats = task.map {
        if (it is Result.Success){
            getActiveAndCompletedStats(it.data)
        }else{
            null
        }
    }

     val activeTaskPercent = stats.map { it?.activeTasksPercent ?: 0f }

     val completeTaskPercent = stats.map { it?.completedTasksPercent ?: 0f }

     val lDataLoading = mDataLoading

     val error = task.map {it is Result.Error}

     val empty = task.map {(it as? Result.Success)?.data.isNullOrEmpty()}

    fun refresh(){
        mDataLoading.value= true

        viewModelScope.launch {
            defaultTaskRepository.refreshTasks()
            mDataLoading.value = false
        }


    }





}