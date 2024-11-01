package com.example.project.ui.screens.list

import android.view.Surface
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.material.MaterialTheme
import com.example.project.data.models.ToDoTask
import com.example.project.navigation.Screens
import com.example.project.ui.theme.taskItemBackroundColor

@Composable
fun ListContent(){

}

@Composable
fun TaskItem(
    toDoTask: ToDoTask,
    navigateToScreens: (taskId: Int)->Unit
){
    Surface(
        modifier=Modifier
        .fillMaxWidth(),
        color=MaterialTheme.colors.taskItemBackroundColor

        ){

    }

}