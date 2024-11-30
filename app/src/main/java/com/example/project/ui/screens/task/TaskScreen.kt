package com.example.project.ui.screens.task

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.example.project.data.models.Priority
import com.example.project.data.models.ToDoTask
import com.example.project.ui.viewmodels.SharedViewModel
import com.example.project.util.Action
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.platform.LocalContext

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
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

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TaskAppBar(
                selectedTask = selectedTask,
                navigateToListScreen = {
                    action -> if (action==Action.NO_ACTION){
                        navigateToListScreen(action)
                    }else{
                        if (sharedViewModel.validateFields()){
                            navigateToListScreen(action)
                        }else{
                            displayToast(context = context)
                        }
                }
                }
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
                    sharedViewModel.updateTitle(it)
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
fun displayToast(context: Context){
    Toast.makeText(
        context,
        "Fields Empty.",
        Toast.LENGTH_SHORT
    ).show()

}
