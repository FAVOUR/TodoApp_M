package com.example.todoapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


fun <T> LiveData<T>.addObserver(
    timeUnit: TimeUnit=TimeUnit.SECONDS,
    time:Long=2L,
    afterObserver : () -> Unit={}
): T {

     var data : T? =null
    var latch = CountDownLatch(1)
     val observer = object :Observer<T>{
         override fun onChanged(t: T) {
              data =t
             //StartCountDown
             latch.countDown()
             this@addObserver.removeObserver(this)


         }
     }

    this.observeForever(observer)

    try {
        afterObserver.invoke()
        //Noting returned and thr time has elapsed
        if(!latch.await(time,timeUnit)){
            this.removeObserver(observer)
        }
    }
    catch (e:Exception){

        this.removeObserver(observer)

    }finally {

        this.removeObserver(observer)

    }
    @Suppress("UNCHECKED_CAST")
    return data as T
}