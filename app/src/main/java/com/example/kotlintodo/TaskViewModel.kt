package com.example.kotlintodo

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.lifecycle.ViewModel

// a ViewModel is just a regular Kotlin class
// when we have a screen rotation (screen rotation is a configuration change) and this means that Android recreates the Activity and resets the states to default value, which causes an unwanted behavior
// https://www.youtube.com/watch?v=V-s4z7B_Gnc
// Google introduced its own viewModel, which will create a ViewModel that outlives the lifecycle of its screen/activity; the ViewModel will persist even if the Activity is destroyed
class TaskViewModel: ViewModel() {

    var task by mutableStateOf("")

    // TODO (need):
    // look up a way to persist the data on Android
    // check if the data persists even with screen rotation

    // TODO (wish list):
    // maybe drag the tasks to change position for priority
    // open a new activity to see/add more details about the task
    // maybe have categories to group the tasks/change colors?

    // this will be empty after developing, it's just nice to have some defaults for testing

    val sampleList = listOf<TaskModel>(
        TaskModel("Sample 1", true),
        TaskModel("Sample 2"),
        TaskModel("Sample 3")
    )
    var tasks by mutableStateOf(listOf<TaskModel>())

    var taskToEdit by mutableStateOf("")
    var editMode by mutableStateOf(false)

    var showIncompleteTasks by mutableStateOf(false)

    var backgroundColor by mutableStateOf(White)

    // I don't understand this completely, it was in StackOverflow - need to research
    // isSystemInDarkTheme() is only usable within a Composable, we cannot use it in taskViewModel
    private var _isDarkTheme by mutableStateOf(false)

    private fun setDarkTheme() {
        backgroundColor = if (_isDarkTheme) Color.Black else White
    }

    var isDarkTheme: Boolean
        get() = _isDarkTheme
        set(value) {
            _isDarkTheme = value
            setDarkTheme()
        }

    init {
        setDarkTheme()
        tasks = sampleList
    }

    fun toggleCompletion(currentTask: TaskModel) {
        tasks = tasks.map {
            if (it == currentTask) it.copy(isComplete = !it.isComplete) else it
        }

    }

    val filteredTasks: List<TaskModel>
        get() = if (showIncompleteTasks) {
            tasks.filter { !it.isComplete }
        } else {
            tasks
        }

    //     we have to specify the type of what we are returning for it to work
    fun filterIncompleteTasks() {
        if (showIncompleteTasks) {
            filteredTasks
            showIncompleteTasks = false
        } else {
            tasks
            showIncompleteTasks = true
        }
    }

    fun addTask(newTask: TaskModel) {
        tasks += newTask
    }

    fun editTask(clickedTask: TaskModel, newTask: String) {
        tasks = tasks.map {
            // "it" is a keyword in Kotlin - it stands for the current task the map is on https://kotlinlang.org/docs/lambdas.html#it-implicit-name-of-a-single-parameter
            // if the current task being mapped matches the clickedTask, then we will edit the task in the Model with the newTask
            // copy is used in Kotlin data classes to copy a data and alter part of it https://kotlinlang.org/docs/data-classes.html#copying
            // since we are only updating the task, which is of type String, we do not need to make newTask conform to the TaskModel type
            if (it == clickedTask) it.copy(task = newTask) else it
        }
    }

    fun deleteTask(taskToDelete: TaskModel) {
        tasks -= taskToDelete
    }
}