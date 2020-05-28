package com.example.todoapp.statistics.util

import com.example.todoapp.data.Task
import org.hamcrest.CoreMatchers.`is`
import org.junit.Test

import org.junit.Assert.*

class StatisticsUtilsTest {

    //        subjectUnderTest_actionOrInput_resultState

    @Test
    fun getActiveAndCompletedStats_notcompleted_returnHundredZero() {


//           given (Arrange)
         val tasks = listOf<Task>(Task(title = "Test Test ",description = "Dummy Data"))


//           when (Act)
              var statsResult= getActiveAndCompletedStats(tasks)


//           Then(Assert)
              assertThat(statsResult.activeTasksPercent,`is`(100f))
              assertThat(statsResult.completedTasksPercent, `is`(0f))

    }

    @Test
    fun getActiveCompletedStats_empty_returnZero(){
        val task= listOf<Task>()

        var statsResult = getActiveAndCompletedStats(task)

          assertThat(statsResult.completedTasksPercent, `is` (0f))
          assertThat(statsResult.activeTasksPercent, `is` (0f))
    }


    @Test
    fun getActiveAndCompletedStats_noActive_returnsZeroHundred() {
        val tasks = listOf(
            Task("title", "desc", isCompleted = true)
        )
        // When the list of tasks is computed with a completed task
        val result = getActiveAndCompletedStats(tasks)

        // Then the percentages are 0 and 100
        assertThat(result.activeTasksPercent, `is`(0f))
        assertThat(result.completedTasksPercent, `is`(100f))
    }

    @Test
    fun getActiveAndCompletedStats_both_returnsFortySixty() {
        // Given 3 completed tasks and 2 active tasks
        val tasks = listOf(
            Task("title", "desc", isCompleted = true),
            Task("title", "desc", isCompleted = true),
            Task("title", "desc", isCompleted = true),
            Task("title", "desc", isCompleted = false),
            Task("title", "desc", isCompleted = false)
        )
        // When the list of tasks is computed
        val result = getActiveAndCompletedStats(tasks)

        // Then the result is 40-60
        assertThat(result.activeTasksPercent, `is`(40f))
        assertThat(result.completedTasksPercent, `is`(60f))
    }

    @Test
    fun getActiveAndCompletedStats_error_returnsZeros() {
        // When there's an error loading stats
        val result = getActiveAndCompletedStats(null)

        // Both active and completed tasks are 0
        assertThat(result.activeTasksPercent, `is`(0f))
        assertThat(result.completedTasksPercent, `is`(0f))
    }

}