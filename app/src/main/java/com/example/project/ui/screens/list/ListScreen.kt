package com.example.project.ui.screens.list

import android.annotation.SuppressLint
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.project.R
import com.example.project.ui.theme.fabBackgroundColor
import com.example.project.ui.viewmodels.SharedViewModel
import com.example.project.util.Action
import com.example.project.util.SearchAppBarState
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@ExperimentalMaterialApi
@Composable
fun ListScreen(
    action: Action,
    navigateToTaskScreen: (Int) -> Unit,
    sharedViewModel: SharedViewModel
) {
    LaunchedEffect(key1 = true) {
        sharedViewModel.getAllTasks()
        sharedViewModel.readSortState()
    }

    val action by sharedViewModel.action

    val allTasks by sharedViewModel.allTasks.collectAsState()
    val searchedTasks by sharedViewModel.searchedTasks.collectAsState()
    val sortState by sharedViewModel.sortState.collectAsState()
    val lowPriorityTasks by sharedViewModel.lowPriorityTasks.collectAsState()
    val highPriorityTasks by sharedViewModel.highPriorityTasks.collectAsState()

    val searchAppBarState: SearchAppBarState
            by sharedViewModel.searchAppBarState
    val searchTextState: String by sharedViewModel.searchTextState

    val scaffoldState= rememberScaffoldState()

    //sharedViewModel.handleDatabaseActions(action=action)
    DisplaySnackBar(
        scaffoldState = scaffoldState,
        handleDatabaseActions = {sharedViewModel.handleDatabaseActions(action=action)},
        onUndoClicked = {
            sharedViewModel.action.value=it
        },
        taskTitle = sharedViewModel.title.value,
        action= action,

    )

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            ListAppBar(
                sharedViewModel = sharedViewModel,
                searchAppBarState = searchAppBarState,
                searchTextState = searchTextState
            )
        },
        content = {
            ListContent(
                allTasks = allTasks,
                searchedTasks =  searchedTasks,
                lowPriorityTasks= lowPriorityTasks,
                highPriorityTasks = highPriorityTasks,
                sortState= sortState,
                searchAppBarState= searchAppBarState,
                onSwipeToDelete = {
                    action, task->sharedViewModel.action.value = action
                    sharedViewModel.updateTaskFields(selectedTask = task)
                },
                navigateToTaskScreen = navigateToTaskScreen
            )
        },
        floatingActionButton = {
            ListFab(onFabClicked = navigateToTaskScreen)
        }
    )
}

@Composable
fun ListFab(
    onFabClicked: (taskId: Int) -> Unit
) {
    FloatingActionButton(
        onClick = {
            onFabClicked(-1)
        },
        backgroundColor = MaterialTheme.colors.fabBackgroundColor
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(
                id = R.string.add_button
            ),
            tint = Color.White
        )
    }
}

@Composable
fun DisplaySnackBar(
    scaffoldState: ScaffoldState,
    handleDatabaseActions: ()->Unit,
    onUndoClicked: (Action) -> Unit,
    taskTitle: String,
    action: Action
    ){
    handleDatabaseActions()
    val scope= rememberCoroutineScope()
    LaunchedEffect(key1 = action) {
        if (action !=Action.NO_ACTION){
            scope.launch {
                val snackBarResult= scaffoldState.snackbarHostState.showSnackbar(
                    message = setMessage(action=action, taskTitle=taskTitle),
                    actionLabel = setActionLabel(action=action)
                )
                undoDeletedTask(
                    action=action,
                    snackBarResult= snackBarResult,
                    onUndoClicked = onUndoClicked
                )
            }
        }
    }
}

private fun setMessage(action: Action, taskTitle: String): String{
    return when(action){
        Action.DELETE_ALL->"All Tasks Removed."
        else-> "${action.name}: $taskTitle"
    }
}

private  fun setActionLabel(action: Action): String{
    return if (action.name=="DELETE"){
        "UNDO"
    }else{
        "Ok"
    }
}

private fun undoDeletedTask(
    action: Action,
    snackBarResult: SnackbarResult,
    onUndoClicked: (Action)-> Unit
){
    if (snackBarResult==SnackbarResult.ActionPerformed
        && action==Action.DELETE)
    {
        onUndoClicked(Action.UNDO)
    }
}