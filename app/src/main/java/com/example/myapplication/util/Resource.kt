package com.example.myapplication.util

sealed class Resource<out T>(val data: T?) {
     class Success<T>( data: T, val message: String = "") : Resource<T>(data)
     class Error<T>(
        val error: Throwable?,
         data: T?,
        var errorMessage: String = ""
    ) : Resource<T>(data)
     class Loading<T>(val message: String = "", data:T?) : Resource<T>(data)
}

