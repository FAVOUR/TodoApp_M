package com.example.todoapp.task.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.todoapp.Event
import com.example.todoapp.R
import com.example.todoapp.TestCouroutinUtil
import com.example.todoapp.`addObserver getOrAwaitValue`
import com.example.todoapp.data.Task
import com.example.todoapp.data.source.FakeTaskRepository
import com.example.todoapp.task.util.TasksFilterType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Matchers.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TaskViewModelTest {

    lateinit var taskViewModel: TaskViewModel
    lateinit var task1: Task
    lateinit var tasksRepository: FakeTaskRepository

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCouroutine:TestCouroutinUtil =TestCouroutinUtil()

    @Before
    fun setupViewModel() {

        //Given
//        var app = ApplicationProvider.getApplicationContext<Application>()


        // We initialise the tasks to 3, with one active and two completed
        tasksRepository = FakeTaskRepository()
        task1 = Task("Title1", "Description1")
//        val task2 = Task("Title2", "Description2", true)
//        val task3 = Task("Title3", "Description3", true)
//        tasksRepository.addTask(task1, task2, task3)
        tasksRepository.addTask(task1)


        taskViewModel = TaskViewModel(tasksRepository)

    }

    @Test
    fun addTask_setsNewEventTask() {


        var observer = Observer<Event<Unit>> {}

        try {
            //When
            taskViewModel.addNewTask()


            var value = taskViewModel.newTaskEvent.`addObserver getOrAwaitValue`()

            assertThat(value?.getContentIfNotHandled(), (not(nullValue())))

        } finally {
            taskViewModel.newTaskEvent.removeObserver(observer)
        }
    }


    @Test
    fun setFilterAllTasks_tasksAddViewVisible() {

        // When the filter type is ALL_TASKS
        taskViewModel.setFiltering(TasksFilterType.ALL_TASKS)


        var value = taskViewModel.tasksAddViewVisible.`addObserver getOrAwaitValue`()


        // Then the "Add task" action is visible
        assertThat(value, `is`(true))

    }


    @Test
    fun completeTask_dataAndSnackBarUpdated() {
        taskViewModel.completeTask(task1, true)

        assertThat(tasksRepository.taskDataSource[task1.id]?.isCompleted!!, `is`(true))

        val snackbartest: Event<Int> = taskViewModel.snackbarText.`addObserver getOrAwaitValue`()

        assertThat(snackbartest.getContentIfNotHandled(), `is`(R.string.task_marked_complete))
    }
}