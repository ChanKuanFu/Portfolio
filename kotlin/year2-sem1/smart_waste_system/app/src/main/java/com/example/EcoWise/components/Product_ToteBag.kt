package com.example.EcoWise.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun Product_ToteBag(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val bagWidth = w * 0.62f
        val bagHeight = h * 0.55f
        val left = (w - bagWidth) / 2
        val top = h * 0.35f

        // Cotton Canvas Tote
        drawRoundRect(
            color = Color(0xFFEFEBE9),
            topLeft = Offset(left, top),
            size = Size(bagWidth, bagHeight),
            cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
        )
        drawRoundRect(
            color = Color(0xFFD7CCC8),
            topLeft = Offset(left, top),
            size = Size(bagWidth, bagHeight),
            cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx()),
            style = Stroke(width = 1.5.dp.toPx())
        )

        // Bag Handles
        val handlePath = Path().apply {
            moveTo(left + bagWidth * 0.25f, top)
            cubicTo(
                left + bagWidth * 0.25f, top - h * 0.25f,
                left + bagWidth * 0.75f, top - h * 0.25f,
                left + bagWidth * 0.75f, top
            )
        }
        drawPath(handlePath, color = Color(0xFFBCAAA4), style = Stroke(width = 3.dp.toPx()))

        // Green Leaf print on bag
        drawCircle(
            color = Color(0xFF4CAF50),
            radius = bagWidth * 0.15f,
            center = Offset(left + bagWidth * 0.5f, top + bagHeight * 0.5f)
        )
    }
}
