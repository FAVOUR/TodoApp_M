package com.example.todoapp

import android.app.Activity
import android.graphics.ColorSpace.match
import android.view.Gravity
import androidx.appcompat.widget.Toolbar
import androidx.room.Room
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.contrib.DrawerMatchers.isOpen
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.todoapp.data.Task
import com.example.todoapp.data.source.TaskRepository
import com.example.todoapp.data.source.local.db.TaskDao
import com.example.todoapp.data.source.local.db.ToDoDataBase
import com.example.todoapp.util.DataBindingIdlingResource
import com.example.todoapp.util.ExpressoIdling
import com.example.todoapp.util.monitorActivity
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class AppNavigationTest {

    val dataBindingIdlingResource =DataBindingIdlingResource()
     lateinit var  taskRepository : TaskRepository
     lateinit var  taskDataBase : ToDoDataBase

    @Before
    fun init(){
        taskDataBase= Room.inMemoryDatabaseBuilder(getApplicationContext(),ToDoDataBase::class.java)
                            .allowMainThreadQueries()
                            .build()

        taskRepository =  ServiceLocator.provideRepository(getApplicationContext())

        runBlocking {
            taskRepository.deleteAllTasks()
        }

    }

    @After
    fun resetDetails(){
        taskDataBase.close()
    }



    @Before
    fun initialSetup(){

        IdlingRegistry.getInstance().register(ExpressoIdling.countdownIdlingResource)
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)

    }

    @After
    fun reset(){
        IdlingRegistry.getInstance().unregister(ExpressoIdling.countdownIdlingResource)
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)

    }

    @Test
    fun tasksScreen_clickOnDrawerIcon_OpensNavigation() {
        // Start the Tasks screen.
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // 1. Check that left drawer is closed at startup.

        onView(withId(R.id.nav_drawer)).check(matches(isClosed(Gravity.START)))

        // 2. Open drawer by clicking drawer icon.
         onView(withContentDescription(activityScenario.getToolbarNavigationContentDescription())).perform(click())
        // 3. Check if drawer is open.
         onView(withId(R.id.nav_drawer)).check(matches(isOpen(Gravity.START)))
        // When using ActivityScenario.launch(), always call close()
        activityScenario.close()
    }


    @Test
    fun taskDetailScreen_doubleUpButton() = runBlocking {
        val task = Task("Up button", "Description")
        taskRepository.saveTask(task)

        // Start the Tasks screen.
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // 1. Click on the task on the list.
        onView(withText("Up button")).perform(click())

        // 2. Click on the edit task button.
        onView(withId(R.id.edit_task_fab)).perform(click())
        // 3. Confirm that if we click Up button once, we end up back at the task details page.
        onView(withContentDescription(activityScenario.getToolbarNavigationContentDescription())).perform(click())
        // 4. Confirm that if we click Up button a second time, we end up back at the home screen.

        onView(withContentDescription(activityScenario.getToolbarNavigationContentDescription())).perform(click())

        onView(withId(R.id.tasks_container_layout)).check(matches(isDisplayed()))
        // When using ActivityScenario.launch(), always call close().
        activityScenario.close()
    }








    fun <T : Activity> ActivityScenario<T>.getToolbarNavigationContentDescription(): String {
        var description = ""
        onActivity {
            description =
                it.findViewById<Toolbar>(R.id.toolbar).navigationContentDescription as String
        }
        return description
    }
}
