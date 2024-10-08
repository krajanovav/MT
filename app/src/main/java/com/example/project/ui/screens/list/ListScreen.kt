package com.example.project.ui.screens.list

import android.R
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ListScreen(navigateToTaskScreen: (Int) -> Unit)

{
    Scaffold (
        content = {},
        floatingActionButton = {

        }
    )
}
@Composable
fun ListFab(){
    FloatingActionButton(onClick = {}) {
        Icon(painter = ,    contentDescription = )
    }
}

@Composable
@Preview
private fun ListScreenPreview(){
    ListScreen(navigateToTaskScreen = {})
}