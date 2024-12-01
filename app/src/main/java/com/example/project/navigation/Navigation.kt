package com.example.project.navigation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.project.navigation.destinations.listComposable
import com.example.project.navigation.destinations.taskComposable
import com.example.project.ui.viewmodels.SharedViewModel
import com.example.project.util.Constants.LIST_SCREEN
import com.example.project.util.Constants.SPLASH_SCREEN

@ExperimentalMaterialApi
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
        startDestination = LIST_SCREEN // zde je potřeba zajistit, aby bylo vždy předáno {action}
    ) {
       // splashComposable(
        //    navigateToListScreen = screen.splash
       // )
        listComposable(
            navigateToTaskScreen = screen.list,
            sharedViewModel = sharedViewModel
        )
        taskComposable(
            navigateToListScreen = { action ->
                screen.task(action)},
            sharedViewModel = sharedViewModel// předáváme správný action argument

        )
    }
}
