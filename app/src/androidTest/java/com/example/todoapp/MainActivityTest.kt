package com.example.todoapp




import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.todoapp.data.Task
import com.example.todoapp.data.source.TaskRepository
import com.example.todoapp.util.DataBindingIdlingResource
import com.example.todoapp.util.ExpressoIdling
import com.example.todoapp.util.monitorActivity

import kotlinx.coroutines.runBlocking
import org.hamcrest.core.IsNot.not
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest{

    lateinit var repository: TaskRepository

    val dataBindingResources = DataBindingIdlingResource()


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


    @Before
    fun setupIdlingResources(){
        IdlingRegistry.getInstance().register(ExpressoIdling.countdownIdlingResource)
      IdlingRegistry.getInstance().register(dataBindingResources)
    }


    @After
    fun unRegisterIdlingResource(){
        IdlingRegistry.getInstance().unregister(ExpressoIdling.countdownIdlingResource)
        IdlingRegistry.getInstance().unregister(dataBindingResources)
    }

    @Test
    fun editText()= runBlocking {
        var task = Task("First Expresso test","Initial Setup")
        repository.saveTask(task)

        var activitySenerio = ActivityScenario.launch(MainActivity::class.java)
        dataBindingResources.monitorActivity(activitySenerio)

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
        onView(withText("TITLE1")).check(doesNotExist())


//        delay(5000)


        activitySenerio.close()
    }



    @Test
    fun createOneTask_deleteTask()= runBlocking {
        var task = Task("Create Task", "Start Task ")
        repository.saveTask(task)
        // 1. Start TasksActivity.
       val activityScenerio = ActivityScenario.launch(MainActivity::class.java)
        dataBindingResources.monitorActivity(activityScenerio)
//
//        onView(withText(task.title)).perform(click())
//        onView(withId(R.id.task_detail_title_text)).check(matches(withText(task.title)))
//        onView(withId(R.id.task_detail_description_text)).check(matches(withText(task.description)))
//        onView(withId(R.id.edit_task_fab)).perform(click())
//

        // 2. Add an active task by clicking on the FAB and saving a new task.
        onView(withId(R.id.add_task_fab)).perform(click())

        task=Task("New Task to Test","Save and click the complete FAB")
        onView(withId(R.id.add_task_title_edit_text)).perform(typeText("New Task to Test"),
            closeSoftKeyboard()) //Not a must to close softKeyboard
        onView(withId(R.id.add_task_description_edit_text)).perform(typeText("Save and click the complete FAB"))
        onView(withId(R.id.save_task_fab)).perform(click())


        // 3. Open the new task in a details view.

        onView(withText("New Task to Test")).perform(click())
        // 4. Click delete task in menu.
        onView(withId(R.id.menu_delete)).perform(click())


        // 5. Verify it was deleted.

        onView(withId(R.id.menu_filter)).perform(click())
        onView(withText(R.string.nav_all)).perform(click())

        onView(withText("New Task to Test")).check(doesNotExist())
        // 6. Make sure the activity is closed.
        activityScenerio.close()
    }




}
