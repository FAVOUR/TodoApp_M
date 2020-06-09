package com.example.todoapp.statistics.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.todoapp.TestCouroutinUtil
import com.example.todoapp.`addObserver getOrAwaitValue`
import com.example.todoapp.data.Task
import com.example.todoapp.data.source.FakeTaskRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.core.Is.`is`
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test


class StatisticsViewModelTest{

    @get:Rule
    val instantRule=InstantTaskExecutorRule()

    @get:Rule
    @ExperimentalCoroutinesApi
    val maincouroutineScope=TestCouroutinUtil()


    // Subject under test
    private lateinit var statisticsViewModel: StatisticsViewModel

    // Use a fake repository to be injected into the view model.
    private lateinit var tasksRepository: FakeTaskRepository


    @Test
    @ExperimentalCoroutinesApi
    fun loadTask_dataLoading(){

        tasksRepository = FakeTaskRepository()

        maincouroutineScope.pauseDispatcher()


        statisticsViewModel = StatisticsViewModel(tasksRepository)

        statisticsViewModel.refresh()

        statisticsViewModel.lDataLoading.`addObserver getOrAwaitValue`()



        assertThat(statisticsViewModel.lDataLoading.value, `is` (true))

        maincouroutineScope.resumeDispatcher()
        assertThat(statisticsViewModel.lDataLoading.value, `is` (false))




    }

    @Test
    fun loadStatisticsWhenTasksAreUnavailable_callErrorToDisplay(){

        tasksRepository = FakeTaskRepository()

        statisticsViewModel = StatisticsViewModel(tasksRepository)

        tasksRepository.setReturnError(true)

        statisticsViewModel.refresh()

        // Then empty and error are true (which triggers an error message to be shown).
        assertThat(statisticsViewModel.empty.`addObserver getOrAwaitValue`(), `is`(true))
        assertThat(statisticsViewModel.error.`addObserver getOrAwaitValue`(), `is`(true))
    }

}