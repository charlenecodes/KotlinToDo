package com.example.kotlintodo

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlintodo.ui.theme.KotlinToDoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            KotlinToDoTheme {
                // we have to initialize our viewModel like this in order to be able to use it
                val taskViewModel = viewModel<TaskViewModel>()

                val isDarkTheme = isSystemInDarkTheme()
                taskViewModel.isDarkTheme = isDarkTheme

                println(taskViewModel.tasks)


                Column(
                    modifier = Modifier
                        .background(
                            color = taskViewModel.backgroundColor
                        )
                        .padding(horizontal = 5.dp)
                ) {
//                    ThemeToggle(taskViewModel = taskViewModel)

                    Title(text = "To Do List 📝")

                    if (taskViewModel.tasks.isEmpty()) {
                        GettingStarted(taskViewModel = taskViewModel)
                    } else {
                        if (taskViewModel.tasks.isNotEmpty()) TaskFilter(taskViewModel)
                    }
                    
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom,
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxSize(1f)
                    ) {
                        TaskInput(taskViewModel)

                        SubmitButton(
                            taskViewModel,
                        )
                    }

                }
            }
        }
    }
}

//@Composable
//fun ThemeToggle(taskViewModel: TaskViewModel) {
//    if (taskViewModel.isDarkTheme) {
//        Icon(
//            imageVector = ImageVector.vectorResource(id = R.drawable.baseline_sunny_24),
//            contentDescription = "darkMode",
//            tint = colorResource(id = R.color.yellow),
//            modifier = Modifier
//                .size(25.dp)
//                .clickable {
//                    taskViewModel.isDarkTheme = false
//                },
//        )
//    } else {
//        Icon(
//            // added in res/drawable -> New > VectorAsset
//            imageVector = ImageVector.vectorResource(id = R.drawable.baseline_nightlight_round_24),
//            contentDescription = "lightMode",
//            tint = White,
//            modifier = Modifier
//                .size(25.dp)
//                .background(
//                    color = colorResource(id = R.color.indigo),
//                    shape = CircleShape
//                )
//                .padding(3.dp)
//                .clickable {
//                    taskViewModel.isDarkTheme = true
//                },
//        )
//    }
//}

@Composable
fun Title(text: String) {
    val myTextColor = if (isSystemInDarkTheme()) White else Black
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 8.dp,
                vertical = 25.dp
            )
    ) {
            Text(
                text = text,
                fontWeight = FontWeight.Bold,
                color = myTextColor,
                style = MaterialTheme.typography.displaySmall,
                textAlign = TextAlign.Center
            )
        }
}

@Composable
fun GettingStarted(taskViewModel: TaskViewModel) {
    val color = if(isSystemInDarkTheme()) White else Black

    @Composable
    fun Instructions(text: String) = Text(text = text, color = color)

        Column (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                "✨ Getting Started ✨",
                color = color,
                style = MaterialTheme.typography.headlineSmall
            )
            Instructions("Tap a task to mark complete")
            Instructions("Undo by tapping the task again")
            Row{
                Instructions("Tap ")
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = color
                )
                Instructions(" to edit task or ")
                Icon(
                    Icons.Outlined.Delete,
                    contentDescription = "delete",
                    tint = color
                )
                Instructions(" to delete")

            }

            Row{
                Instructions(text = "Filter ")
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.baseline_filter_list_24),
                    contentDescription = "filter on",
                    modifier = Modifier
                        .size(25.dp)
                        .padding(3.dp)
                        .clickable {
                            taskViewModel.filterIncompleteTasks()
                        },
                    tint = if (isSystemInDarkTheme()) White else Black
                )
                Instructions(text = " tasks to only show incomplete tasks")
            }
        }
}

@Composable
fun TaskFilter(taskViewModel: TaskViewModel) {
//    val filteredTasks = if (taskViewModel.showIncompleteTasks) {
//        taskViewModel.filteredTasks
//    } else {
//        taskViewModel.tasks
//    }

    Row(
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 8.dp
            )
            .padding(
                bottom = 20.dp
            )
    ) {
        if (taskViewModel.showIncompleteTasks) {
            Text(
                "Show All",
                modifier = Modifier
                    .padding(end = 5.dp),
                color = colorResource(id = R.color.indigo),
                fontSize = 20.sp,
                )

            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_filter_list_24),
                contentDescription = "filter on",
                modifier = Modifier
                    .size(25.dp)
                    .background(
                        color = colorResource(id = R.color.indigo),
                        shape = CircleShape
                    )
                    .padding(3.dp)
                    .clickable {
                        taskViewModel.filterIncompleteTasks()
                    },
                tint = if (isSystemInDarkTheme()) Black else White
            )
        } else {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_filter_list_24),
                contentDescription = "filter off",
                modifier = Modifier
                    .size(25.dp)
                    .border(
                        border = BorderStroke(
                            width = 1.8.dp,
                            color = colorResource(id = R.color.indigo)
                        ),
                        shape = CircleShape
                    )
                    .padding(3.dp)
                    .clickable {
                        taskViewModel.filterIncompleteTasks()
                    },
                tint = colorResource(id = R.color.indigo)
            )
        }

    }
    TaskList(
        taskViewModel,
        tasks = if (taskViewModel.showIncompleteTasks) {
            taskViewModel.filteredTasks
        } else {
            taskViewModel.tasks
        }
    )
}

@Composable
fun TaskList (
    taskViewModel: TaskViewModel,
    tasks: List<TaskModel>,
){
        LazyColumn {
            items(tasks) { task ->
                    TaskRow(taskViewModel = taskViewModel, task = task)
                    HorizontalDivider(
                        modifier = Modifier
                            .padding(
                                vertical = 5.dp,
                                horizontal = 10.dp
                                )
                    )
            }
        }
}

@Composable
fun TaskRow(
    taskViewModel: TaskViewModel,
    task: TaskModel
){
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = 10.dp,
                vertical = 5.dp
            )
    ) {
        // this is from a tutorial
        val context = LocalContext.current

        if (task.isComplete) {
            Icon(
                Icons.Rounded.CheckCircle,
                contentDescription = "status",
                tint = Green
            )
        } else {
            Icon(
                // added in res/drawable -> New > VectorAsset
                imageVector = ImageVector.vectorResource(id = R.drawable.outline_circle_24),
                contentDescription = "status",
                tint = colorResource(id = R.color.indigo)
            )
        }

        // putting the text in a box with these modifications makes it so that it doesn't push the icons out of the way
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp, end = 8.dp) // Add padding for better spacing
                .clickable {
                    taskViewModel.toggleCompletion(task)
                }
        ) {
            if (task.isComplete) {
                Text(
                    text = task.task,
                    modifier = Modifier
                        .fillMaxWidth(),
                    textDecoration = TextDecoration.LineThrough,
                    color = Gray,
                    fontSize = 20.sp,
                    )
            } else {
                Text(
                    text = task.task,
                    modifier = Modifier
                        .fillMaxWidth(),
                    color = if (isSystemInDarkTheme()) White else colorResource(id = R.color.indigo),
                    fontSize = 20.sp,
                )
            }

        }

        if (!task.isComplete){
            Icon(
                Icons.Default.Edit,
                contentDescription = "Edit",
                tint = colorResource(id = R.color.indigo),
                modifier = Modifier
                    .clickable {
                        taskViewModel.task = task.task
                        taskViewModel.taskToEdit = task.task
                        taskViewModel.editMode = true
                    }
            )
        }

        Icon(
            Icons.Outlined.Delete,
            contentDescription = "Delete",
            tint = colorResource(id = R.color.indigo),
            modifier = Modifier
                .clickable {
                    Toast.makeText(context, "Task successfully deleted!", Toast.LENGTH_SHORT).show()
                    taskViewModel.deleteTask(task)
                }
        )

    }
}

@Composable
fun TaskInput(
    taskViewModel: TaskViewModel
) {

    val color = if(isSystemInDarkTheme()) White else Black

    if (taskViewModel.tasks.isEmpty()) {
            Text("Add a task to start your list!", color = color)
        }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            value = taskViewModel.task,
            onValueChange = { text: String ->
                taskViewModel.task = text
            },
            placeholder = {
                Text("Enter a task...",
                    color = Gray,
                    fontSize = 20.sp
                )
            },
            shape = RoundedCornerShape(25),
            trailingIcon = {
                if (taskViewModel.task.isNotBlank()) {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = "cancelButton",
                        modifier = Modifier
                            // .clickable allows the simple way to reset the state
                            .clickable {
                                taskViewModel.task = ""
                            }
                            .size(20.dp)
                            .background(
                                color = Gray,
                                shape = RoundedCornerShape(100)
                            )
                    )
                }
            },
            singleLine = true,
        )
    }

@Composable
fun SubmitButton(
    taskViewModel: TaskViewModel,
) {
    val context = LocalContext.current
    Button(
        onClick = {
            if (taskViewModel.editMode) {
                // may need a better way for this
                taskViewModel.editTask(TaskModel(taskViewModel.taskToEdit), taskViewModel.task)
                Toast.makeText(context, "Task edited successfully!", Toast.LENGTH_SHORT).show()
                taskViewModel.editMode = false
                taskViewModel.task = ""
                taskViewModel.taskToEdit = ""
            } else {
                Toast.makeText(context, "Task Submitted!", Toast.LENGTH_SHORT).show()
                taskViewModel.addTask(TaskModel(taskViewModel.task))
                taskViewModel.task = ""
            }

                  },
        modifier = Modifier
            .background(
                shape = RoundedCornerShape(size = 10.dp),
                color = colorResource(id = R.color.indigo)
            )
            .padding(vertical = 1.dp)
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorResource(id = R.color.indigo)
        ),
    ) {
        Text(
            "SUBMIT",
            fontSize = 20.sp,
            color = White
        )
    }
}