package com.example.project.components

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.example.project.R
import com.example.project.ui.theme.taskItemTextColor

@Composable
fun DisplayAlertDialog(
    title: String,
    message: String,
    openDialog: Boolean,
    closeDialog: () ->Unit,
    onYesClicked: () -> Unit,
)
{
    if(openDialog){
        AlertDialog(
            title = {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = message,
                    fontSize = MaterialTheme.typography.subtitle1.fontSize,
                    fontWeight = FontWeight.Normal)
            },
            confirmButton = {
                Button(
                    onClick = {
                    onYesClicked()
                    closeDialog()
                }
                ){
                    Text(text= stringResource(id = R.string.yes),color = Color.White
                    )
                }
            },
            dismissButton={
                OutlinedButton(
                    onClick = {
                        closeDialog()
                    }
                ){
                    Text(text= stringResource(id=R.string.no))
                }
            },
            onDismissRequest = {closeDialog()}
        )
    }
}