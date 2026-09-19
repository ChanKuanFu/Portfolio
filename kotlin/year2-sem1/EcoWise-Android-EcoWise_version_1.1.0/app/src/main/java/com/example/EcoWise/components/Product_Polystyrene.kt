package com.example.EcoWise.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
@Composable
fun Product_Polystyrene(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val boxWidth = w * 0.65f
        val boxHeight = h * 0.45f
        val left = (w - boxWidth) / 2
        val top = (h - boxHeight) / 2

        // White styrofoam clamshell
        drawRoundRect(
            color = Color(0xFFF5F5F5),
            topLeft = Offset(left, top),
            size = Size(boxWidth, boxHeight),
            cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx()),
            style = Fill
        )
        drawRoundRect(
            color = Color(0xFFE0E0E0),
            topLeft = Offset(left, top),
            size = Size(boxWidth, boxHeight),
            cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx()),
            style = Stroke(width = 2.dp.toPx())
        )
        // Mid seam
        drawLine(
            color = Color(0xFFD6D6D6),
            start = Offset(left, top + boxHeight * 0.5f),
            end = Offset(left + boxWidth, top + boxHeight * 0.5f),
            strokeWidth = 2.dp.toPx()
        )
    }
}