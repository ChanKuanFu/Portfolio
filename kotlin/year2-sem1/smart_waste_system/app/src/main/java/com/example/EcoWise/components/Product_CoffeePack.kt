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
fun Product_CoffeePack(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val packWidth = w * 0.58f
        val packHeight = h * 0.75f
        val left = (w - packWidth) / 2
        val top = h * 0.15f

        // Kraft paper / dark coffee foil pouch
        drawRoundRect(
            color = Color(0xFF5D4037),
            topLeft = Offset(left, top),
            size = Size(packWidth, packHeight),
            cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
        )

        // Pouch seal top
        drawLine(
            color = Color(0xFF3E2723),
            start = Offset(left, top + packHeight * 0.12f),
            end = Offset(left + packWidth, top + packHeight * 0.12f),
            strokeWidth = 3.dp.toPx()
        )

        // Golden/Green Organic seal label
        drawRoundRect(
            color = Color(0xFFE8F5E9),
            topLeft = Offset(left + packWidth * 0.15f, top + packHeight * 0.3f),
            size = Size(packWidth * 0.7f, packHeight * 0.45f),
            cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
        )

        // Coffee Bean / Leaf Icon
        drawCircle(
            color = Color(0xFF2E7D32),
            radius = packWidth * 0.16f,
            center = Offset(left + packWidth * 0.5f, top + packHeight * 0.52f)
        )
    }
}