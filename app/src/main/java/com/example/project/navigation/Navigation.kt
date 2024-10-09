package com.example.project.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.project.navigation.destinations.listComposable
import com.example.project.navigation.destinations.taskComposable
import com.example.project.ui.viewmodels.SharedViewModel
import com.example.project.util.Constants.LIST_SCREEN

@Composable
fun SetupNavigation(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    val screen = remember(navController) {
        Screens(navController = navController)
    }

    NavHost(
        navController = navController,
        startDestination = "list/{action}" // zde je potřeba zajistit, aby bylo vždy předáno {action}
    ) {
        listComposable(
            navigateToTaskScreen = screen.task,
            sharedViewModel = sharedViewModel
        )
        taskComposable(
            navigateToListScreen = { action ->
                screen.list(action) // předáváme správný action argument
            }
        )
    }
}
