package com.example.project.ui.screens.task

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.example.project.data.models.Priority
import com.example.project.data.models.ToDoTask
import com.example.project.ui.viewmodels.SharedViewModel
import com.example.project.util.Action
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding

@Composable
fun TaskScreen(
    selectedTask: ToDoTask?,
    sharedViewModel: SharedViewModel,
    navigateToListScreen: (Action) -> Unit
) {
    // Reaktivní hodnoty ze SharedViewModel
    val title: String by sharedViewModel.title
    val description: String by sharedViewModel.description
    val priority: Priority by sharedViewModel.priority

    // Synchronizace `selectedTask` s ViewModelem
    androidx.compose.runtime.LaunchedEffect(selectedTask) {
        println("LaunchedEffect - Updating ViewModel with selectedTask: $selectedTask")
        sharedViewModel.updateTaskFields(selectedTask)
    }

    Scaffold(
        topBar = {
            TaskAppBar(
                selectedTask = selectedTask,
                navigateToListScreen = navigateToListScreen
            )
        },
        content = {
            println("TaskScreen - Title from ViewModel: $title")
            println("TaskScreen - Description from ViewModel: $description")
            println("TaskScreen - Priority from ViewModel: $priority")

            TaskContent(
                title = title,
                onTitleChange = {
                    println("TaskScreen - Updated Title: $it")
                    sharedViewModel.title.value = it
                },
                description = description,
                onDescriptionChange = {
                    println("TaskScreen - Updated Description: $it")
                    sharedViewModel.description.value = it
                },
                priority = priority,
                onPrioritySelected = {
                    println("TaskScreen - Updated Priority: $it")
                    sharedViewModel.priority.value = it
                }
            )
        }
    )
}
