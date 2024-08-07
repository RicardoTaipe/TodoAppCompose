package com.example.todoappcompose.util

/**
 * A generic class that holds a loading signal or a [Result].
 */
sealed class Async<out T> {
    data object Loading : Async<Nothing>()
    data class Success<out T>(val data: T) : Async<T>()
}