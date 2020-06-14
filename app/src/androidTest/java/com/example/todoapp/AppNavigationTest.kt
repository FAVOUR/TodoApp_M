package com.example.todoapp

import androidx.test.espresso.IdlingRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.todoapp.util.DataBindingIdlingResource
import com.example.todoapp.util.ExpressoIdling
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class AppNavigationTest {

    val espressoIdlingResource =DataBindingIdlingResource()

    @Before
    fun initialSetup(){

        IdlingRegistry.getInstance().register(ExpressoIdling.countdownIdlingResource)
        IdlingRegistry.getInstance().register(espressoIdlingResource)

    }

    @After
    fun reset(){
        IdlingRegistry.getInstance().unregister(ExpressoIdling.countdownIdlingResource)
        IdlingRegistry.getInstance().unregister(espressoIdlingResource)

    }



}