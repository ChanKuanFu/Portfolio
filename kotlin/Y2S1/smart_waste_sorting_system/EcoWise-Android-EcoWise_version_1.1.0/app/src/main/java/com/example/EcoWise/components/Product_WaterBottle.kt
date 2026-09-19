package com.example.EcoWise.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Product_WaterBottle(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val bottleWidth = w * 0.36f
        val bottleHeight = h * 0.72f
        val left = (w - bottleWidth) / 2
        val top = h * 0.22f

        // Transparent blue plastic body
        drawRoundRect(
            color = Color(0xFFB3E5FC),
            topLeft = Offset(left, top),
            size = Size(bottleWidth, bottleHeight),
            cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
        )

        // Neck
        drawRoundRect(
            color = Color(0xFF81D4FA),
            topLeft = Offset(left + bottleWidth * 0.25f, top - h * 0.08f),
            size = Size(bottleWidth * 0.5f, h * 0.08f),
            cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
        )

        // Cap (Red/Blue single-use cap)
        drawRoundRect(
            color = Color(0xFF0288D1),
            topLeft = Offset(left + bottleWidth * 0.2f, top - h * 0.12f),
            size = Size(bottleWidth * 0.6f, h * 0.045f),
            cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
        )

        // Plastic ridges
        for (i in 1..3) {
            drawLine(
                color = Color(0xFFE1F5FE),
                start = Offset(left + 4.dp.toPx(), top + (bottleHeight * (0.25f + i * 0.15f))),
                end = Offset(left + bottleWidth - 4.dp.toPx(), top + (bottleHeight * (0.25f + i * 0.15f))),
                strokeWidth = 2.dp.toPx()
            )
        }
    }
}