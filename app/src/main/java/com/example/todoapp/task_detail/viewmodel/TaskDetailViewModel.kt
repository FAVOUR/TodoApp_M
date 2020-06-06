package com.example.todoapp.task_detail.viewmodel

import android.app.Application
import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.example.todoapp.Event
import com.example.todoapp.data.Task
import com.example.todoapp.data.source.DefaultTaskRepository
import com.example.todoapp.util.Result
import kotlinx.coroutines.launch
import com.example.todoapp.R
import com.example.todoapp.data.source.TaskRepository
import com.google.gson.Gson


class TaskDetailViewModel (var tasksRepository: TaskRepository) : ViewModel() {

    // Note, for testing and architecture purposes, it's bad practice to construct the repository
    // here. We'll show you how to fix this during the codelab
//    private val tasksRepository = DefaultTaskRepository.getRepository(application)

    private val _taskId = MutableLiveData<String>()

    private val _task = _taskId.switchMap { taskId ->
        Log.i("dummy _task", taskId)

        tasksRepository.observeTask(taskId).map {
            Log.i("dummy  tasksRepository.observeTask(taskId)", Gson().toJson(it))

            computeResult(it)
        }
    }
    val task: LiveData<Task?> = _task

    val isDataAvailable: LiveData<Boolean> = _task.map { it != null }

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _editTaskEvent = MutableLiveData<Event<Unit>>()
    val editTaskEvent: LiveData<Event<Unit>> = _editTaskEvent

    private val _deleteTaskEvent = MutableLiveData<Event<Unit>>()
    val deleteTaskEvent: LiveData<Event<Unit>> = _deleteTaskEvent

    private val _snackbarText = MutableLiveData<Event<Int>>()
    val snackbarText: LiveData<Event<Int>> = _snackbarText

    // This LiveData depends on another so we can use a transformation.
    val completed: LiveData<Boolean> = _task.map { input: Task? ->
        input?.isCompleted ?: false
    }

    fun deleteTask() = viewModelScope.launch {
        _taskId.value?.let {
            tasksRepository.deleteTask(it)
            _deleteTaskEvent.value = Event(Unit)
        }
    }

    fun editTask() {
        _editTaskEvent.value = Event(Unit)
    }

    fun setCompleted(completed: Boolean) = viewModelScope.launch {

        val task = _task.value ?: return@launch
        if (completed) {

            tasksRepository.completeTask(task)
            showSnackbarMessage(R.string.task_marked_complete)
        } else {
            tasksRepository.activateTask(task)
            showSnackbarMessage(R.string.task_marked_active)
        }
    }

    fun start(taskId: String?) {
        Log.i("dummy start()",taskId)

        // If we're already loading or already loaded, return (might be a config change)
        if (_dataLoading.value == true || taskId == _taskId.value) {
            Log.i("dummy start()","Here data loading true and the id us also equal to the value")

            return
        }
        // Trigger the load
        Log.i("dummy start() passing the data to taskId variable",taskId)
        _taskId.value = taskId
    }

    private fun computeResult(taskResult: Result<Task>): Task? {
        Log.i("dummy  computeResult(taskResult: Result<Task>)", Gson().toJson( taskResult))

        return if (taskResult is Result.Success) {
            Log.i("dummy  computeResult (taskResult is Result.Success)", Gson().toJson( taskResult.data))

            taskResult.data
        } else {
            Log.i("dummy  computeResult showSnackbarMessage()", "No data")

            showSnackbarMessage(R.string.loading_tasks_error)
            null
        }
    }


    fun refresh() {
        // Refresh the repository and the task will be updated automatically.
        _task.value?.let {
            _dataLoading.value = true
            viewModelScope.launch {
                tasksRepository.refreshTasks(it.id)
                _dataLoading.value = false
            }
        }
    }

    private fun showSnackbarMessage(@StringRes message: Int) {

        Log.i("dummy  showSnackbarMessage(@StringRes message: Int) TaskDetailViewmodel",message.toString())

        _snackbarText.value = Event(message)
    }

//
//    @Suppress("UNCHECKED_CAST")
//    class TaskDetailViewModelFactory (
//        private val tasksRepository: TaskRepository
//    ) : ViewModelProvider.NewInstanceFactory() {
//        override fun <T : ViewModel> create(modelClass: Class<T>) =
//            (TaskDetailViewModel(tasksRepository) as T)
//    }

}
