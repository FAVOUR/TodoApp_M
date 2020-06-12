package com.example.todoapp.util

import androidx.test.espresso.idling.CountingIdlingResource

object ExpressoIdling {

    private const val COUNTDOWN = "CountDown"

    val countdownIdlingResource= CountingIdlingResource(COUNTDOWN)

    fun increment(){
        countdownIdlingResource.increment()
    }

    fun decrement(){
        if(!countdownIdlingResource.isIdleNow) {

            countdownIdlingResource.decrement()
        }
    }

}


 inline fun <T> expressoCountDownIdlingWrapper(func: () -> T):T{

    ExpressoIdling.increment()

   return  try{
        func()
    }finally {
        ExpressoIdling.decrement()
    }
}




