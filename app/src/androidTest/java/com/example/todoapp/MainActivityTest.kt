package com.example.todoapp



import android.graphics.ColorSpace.match
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.todoapp.data.Task
import com.example.todoapp.data.source.TaskRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class MainActivityTest{

    lateinit var repository: TaskRepository

//    @ExperimentalCoroutinesApi
//    @get:Rule
//    var testCouroutine= TestCouroutinUtilAT()

    @Before
    fun  setUp(){
//        val context=ApplicationProvider.getApplicationContext<TodoApplication>()
         repository=  ServiceLocator.provideRepository(getApplicationContext())

        runBlocking{
            repository.deleteAllTasks()
        }
    }

    @After
    fun reset(){
        ServiceLocator.resetRepository()
    }


    @Test
    fun editText()= runBlocking {
        var task = Task("First Expresso test","Initial Setup")
        repository.saveTask(task)

        var activitySenerio = ActivityScenario.launch(MainActivity::class.java)


         onView(withText("First Expresso test")).perform(click())
         onView(withId(R.id.task_detail_title_text)).check( matches(withText("First Expresso test")))
         onView(withId(R.id.task_detail_description_text)).check( matches(withText("Initial Setup")))
         onView(withId(R.id.task_detail_complete_checkbox)).check( matches(isNotChecked()))


        //Click edit save
        onView(withId(R.id.edit_task_fab)).perform(click())
        onView(withId(R.id.add_task_title_edit_text)).perform(replaceText("NEW TITLE"))
        onView(withId(R.id.add_task_description_edit_text)).perform(replaceText("NEW DESCRIPTION"))
        onView(withId(R.id.save_task_fab)).perform(click())


        // Verify task is displayed on screen in the task list.
        onView(withText("NEW TITLE")).check(matches(isDisplayed()))
        // Verify previous task is not displayed
        onView(withText("TITLE1")).check(ViewAssertions.doesNotExist())


        delay(5000)


        activitySenerio.close()
    }



}
