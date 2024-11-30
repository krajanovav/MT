package com.example.project.navigation.destinations

import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.project.ui.screens.list.ListScreen
import com.example.project.ui.viewmodels.SharedViewModel
import androidx.navigation.NavType
import com.example.project.util.Action
import com.example.project.util.Constants.LIST_ARGUMENT_KEY
import com.example.project.util.toAction

@ExperimentalMaterialApi
fun NavGraphBuilder.listComposable(
    navigateToTaskScreen: (Int) -> Unit,
    sharedViewModel: SharedViewModel
) {
    composable(
        route = "list/{action}",
        arguments = listOf(navArgument("action") {
            type = NavType.StringType
            defaultValue = Action.NO_ACTION.name // Výchozí hodnota pro action
        })
    ) { navBackStackEntry ->
        val action = navBackStackEntry.arguments?.getString(LIST_ARGUMENT_KEY).toAction()

        LaunchedEffect(key1 = action) {
            sharedViewModel.action.value=action
        }
        // Log.d("ListComposable", action.name)

       // val actionString = navBackStackEntry.arguments?.getString("action") ?: Action.NO_ACTION.name
        //val action = Action.valueOf(actionString) // Převedeme string na Action objekt

        // Předáme akci do ListScreen
        ListScreen(
            action = action, // Předáváme akci do ListScreen
            navigateToTaskScreen = navigateToTaskScreen,
            sharedViewModel = sharedViewModel
        )
    }
}
