package com.example.todoapp

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.todoapp.util.DataBindingIdlingResource
import com.example.todoapp.util.ExpressoIdling
import com.example.todoapp.util.monitorActivity
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class AppNavigationTest {

    val dataBindingIdlingResource =DataBindingIdlingResource()

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

        // 2. Open drawer by clicking drawer icon.

        // 3. Check if drawer is open.

        // When using ActivityScenario.launch(), always call close()
        activityScenario.close()
    }



}