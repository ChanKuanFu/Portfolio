package com.example.EcoWise.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun Product_ReusableBottle(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val bWidth = w * 0.42f
        val bHeight = h * 0.72f
        val left = (w - bWidth) / 2
        val top = h * 0.2f

        // Sleek matte forest green reusable thermo flask
        drawRoundRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF2E7D32), Color(0xFF1B5E20))
            ),
            topLeft = Offset(left, top),
            size = Size(bWidth, bHeight),
            cornerRadius = CornerRadius(10.dp.toPx(), 10.dp.toPx())
        )

        // Bamboo / Steel lid
        drawRoundRect(
            color = Color(0xFFD7CCC8),
            topLeft = Offset(left + bWidth * 0.15f, top - h * 0.1f),
            size = Size(bWidth * 0.7f, h * 0.1f),
            cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
        )

        // Metal handle loop
        drawCircle(
            color = Color(0xFFB0BEC5),
            radius = bWidth * 0.2f,
            center = Offset(left + bWidth * 0.5f, top - h * 0.08f),
            style = Stroke(width = 3.dp.toPx())
        )
    }
}