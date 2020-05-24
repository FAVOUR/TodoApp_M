package com.example.todoapp.util


sealed class Result<R> {

    data class Success <T>(val data: T ): Result<T>()
    data class Error (val failure:Exception ): Result<Nothing>()
    object Loading :Result<Nothing>()

    override fun toString(): String {
         return when(this){
                  is Success<*> ->"Success[$data]"
                  is Error ->  "Error [exception =$failure]"
                   Loading -> "Loading"
         }
    }

}

