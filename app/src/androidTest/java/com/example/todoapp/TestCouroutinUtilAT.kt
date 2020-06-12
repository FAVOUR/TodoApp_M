package com.example.todoapp

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class TestCouroutinUtilAT(val testCouroutineDispatcher: TestCoroutineDispatcher= TestCoroutineDispatcher()) :TestWatcher(),
                               TestCoroutineScope by TestCoroutineScope(testCouroutineDispatcher){
    @ExperimentalCoroutinesApi
    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(testCouroutineDispatcher)
    }

    @ExperimentalCoroutinesApi
    override fun finished(description: Description?) {
        super.finished(description)
        cleanupTestCoroutines()
        Dispatchers.resetMain()

    }
}