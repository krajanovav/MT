package com.example.project.data.models

import androidx.compose.ui.graphics.Color
import com.example.project.ui.theme.HighPriorityColor
import com.example.project.ui.theme.LowPriorityColor
import com.example.project.ui.theme.MediumPriorityColor
import com.example.project.ui.theme.NonePriorityColor

enum class Priority(val color: Color) {
    HIGH(HighPriorityColor),
    MEDIUM(MediumPriorityColor),
    LOW(LowPriorityColor),
    NONE(NonePriorityColor)
}
