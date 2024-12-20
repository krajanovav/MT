package com.example.project.ui.screens.list

import android.annotation.SuppressLint
import android.view.Surface
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.graphics.RectangleShape
import com.example.project.data.models.ToDoTask
import com.example.project.navigation.Screens
import com.example.project.ui.theme.TASK_ITEM_ELEVATION
import androidx.compose.material.Surface
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.project.R
import com.example.project.data.models.Priority
import com.example.project.ui.theme.HighPriorityColor
import com.example.project.ui.theme.LARGEST_PADDING
import com.example.project.ui.theme.LARGE_PADDING
import com.example.project.ui.theme.PRIORITY_INDICATOR_SIZE
import com.example.project.ui.theme.taskItemBackgroundColor
import com.example.project.ui.theme.taskItemTextColor
import com.example.project.util.Action
import com.example.project.util.RequestState
import com.example.project.util.SearchAppBarState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun ListContent(
    allTasks: RequestState<List<ToDoTask>>,
    searchedTasks: RequestState<List<ToDoTask>>,
    lowPriorityTasks: List<ToDoTask>,
    highPriorityTasks: List<ToDoTask>,
    sortState: RequestState<Priority>,
    searchAppBarState: SearchAppBarState,
    onSwipeToDelete: (Action, ToDoTask) -> Unit,
    navigateToTaskScreen: (taskId: Int) -> Unit
) {
    if(sortState is RequestState.Success){
        when{
            searchAppBarState == SearchAppBarState.TRIGGERED->

        if (searchAppBarState == SearchAppBarState.TRIGGERED) {
            if (searchedTasks is RequestState.Success) {
                HandleListContent(
                    tasks = searchedTasks.data,
                    onSwipeToDelete= onSwipeToDelete,
                    navigateToTaskScreen = navigateToTaskScreen
                )
            }
        }
        sortState.data == Priority.NONE->{
            if (allTasks is RequestState.Success) {
                HandleListContent(
                    tasks = allTasks.data,
                    onSwipeToDelete= onSwipeToDelete,
                    navigateToTaskScreen = navigateToTaskScreen
                )
            }
        }
            sortState.data==Priority.LOW ->{
                HandleListContent(
                    tasks = lowPriorityTasks,
                    onSwipeToDelete= onSwipeToDelete,
                    navigateToTaskScreen = navigateToTaskScreen
                )
            }
            sortState.data==Priority.HIGH ->{
                HandleListContent(
                    tasks = highPriorityTasks,
                    onSwipeToDelete= onSwipeToDelete,
                    navigateToTaskScreen = navigateToTaskScreen
                )
            }
        }


    }


}

@ExperimentalMaterialApi
@Composable
fun HandleListContent(
    tasks: List<ToDoTask>,
    onSwipeToDelete: (Action, ToDoTask)->Unit,
    navigateToTaskScreen: (taskId: Int) -> Unit
)
{
    if (tasks.isEmpty()){
        EmptyContent()
    }else  {
        DisplayTasks(
            tasks = tasks,
            onSwipeToDelete = onSwipeToDelete,
            navigateToTaskScreen = navigateToTaskScreen
        )

    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@ExperimentalMaterialApi
@Composable
fun DisplayTasks(
    tasks: List<ToDoTask>,
    onSwipeToDelete: (Action, ToDoTask)->Unit,
    navigateToTaskScreen: (taskId: Int) -> Unit
){

    LazyColumn {
        items(
            items = tasks,
            key = {task ->
                task.id
            }
        ){   task ->

            val dismissState = rememberDismissState()
            val dismissDirection = dismissState.dismissDirection
            val isDismissed = dismissState.isDismissed(DismissDirection.EndToStart)
            if(isDismissed && dismissDirection == DismissDirection.EndToStart){
                val scope = rememberCoroutineScope()
                scope.launch {
                    delay(300)
                    onSwipeToDelete(Action.DELETE, task)
                }
            }
            val degrees by animateFloatAsState(
                if(dismissState.targetValue == DismissValue.Default)
                    0f
                else -45f
            )

            var itemAppeared by remember{
                mutableStateOf(false)
            }
            LaunchedEffect(key1 = true){
                itemAppeared = true
            }
            AnimatedVisibility(
                visible = itemAppeared && !isDismissed,
                enter = expandVertically  (
                    animationSpec = tween(
                        durationMillis = 300
                    )
                ),
                exit = shrinkVertically (
                    animationSpec = tween(
                        durationMillis = 300
                    )
                )

                ) {
                SwipeToDismiss(
                    state = dismissState,
                    directions = setOf(DismissDirection.EndToStart),
                    dismissThresholds = {FractionalThreshold(fraction = 0.2f)},
                    background = { RedBackground(degrees = degrees)},
                    dismissContent = {
                        TaskItem(
                            toDoTask = task,
                            navigateToTaskScreen = navigateToTaskScreen)
                    }
                )
            }
            }
    }
}

@Composable
fun RedBackground(degrees: Float){
    Box(modifier = Modifier
        .fillMaxSize()
        .background(HighPriorityColor)
        .padding(horizontal= LARGEST_PADDING),
        contentAlignment = Alignment.CenterEnd
        ){
        Icon(
            modifier = Modifier.rotate(degrees = degrees),
            imageVector = Icons.Filled.Delete,
            contentDescription = stringResource(id= R.string.delete_icon),
            tint = Color.White)
            }
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
        color=MaterialTheme.colors.taskItemBackgroundColor,
        shape = RectangleShape,
        elevation= TASK_ITEM_ELEVATION,
        onClick = {
            println("Navigating to task ID: ${toDoTask.id}")
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
                    Canvas(
                        modifier = Modifier
                            .size(PRIORITY_INDICATOR_SIZE)
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
