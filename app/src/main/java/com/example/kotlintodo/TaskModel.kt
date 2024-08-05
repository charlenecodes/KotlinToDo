package com.example.kotlintodo

// a Model has to be a Kotlin data class
data class TaskModel(
    val task: String,
    var isComplete: Boolean = false,
)