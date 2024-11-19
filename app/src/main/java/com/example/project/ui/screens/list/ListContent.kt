package com.example.project.ui.screens.list

import android.view.Surface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.graphics.RectangleShape
import com.example.project.data.models.ToDoTask
import com.example.project.navigation.Screens
import com.example.project.ui.theme.TASK_ITEM_ELEVATION
import com.example.project.ui.theme.taskItemBackroundColor
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.project.data.models.Priority
import com.example.project.ui.theme.LARGE_PADDING
import com.example.project.ui.theme.PRIORITY_INDICATOR_SIZE
import com.example.project.ui.theme.taskItemTextColor

@Composable
fun ListContent(){

}
@ExperimentalMaterialApi
@Composable
fun TaskItem(
    toDoTask: ToDoTask,
    navigateToTaskScreen: (taskId: Int)->Unit
){
    Surface(
        modifier=Modifier
        .fillMaxWidth(),
        color=MaterialTheme.colors.taskItemBackroundColor,
        shape = RectangleShape,
        elevation= TASK_ITEM_ELEVATION,
        onClick = {
            navigateToTaskScreen(toDoTask.id)
        }

        ){
        Column(
            modifier = Modifier
                .padding(all = LARGE_PADDING)
                .fillMaxWidth()
        ){
            Row {
               Text(
                   modifier = Modifier.weight(8f),
                   text = toDoTask.title,
                   color = MaterialTheme.colors.taskItemTextColor,
                   style = MaterialTheme.typography.h5,
                   fontWeight = FontWeight.Bold,
                   maxLines = 1
               )
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                    contentAlignment = Alignment.TopEnd
                ){
                    Canvas(modifier = Modifier
                        .width(PRIORITY_INDICATOR_SIZE)
                        .height(PRIORITY_INDICATOR_SIZE)
                    ){
                        drawCircle(
                            color = toDoTask.priority.color
                        )
                    }
                }
            }
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = toDoTask.description,
                color = MaterialTheme.colors.taskItemTextColor,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

    }

}
@ExperimentalMaterialApi
@Composable
@Preview
fun TaskItemPreview(){
    TaskItem(
        toDoTask = ToDoTask(
            id=0,
            title = "Title",
            description = "Some random text",
            priority = Priority.MEDIUM
        ),
        navigateToTaskScreen = {}
    )
}