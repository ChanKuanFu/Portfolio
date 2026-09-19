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
fun Product_OatMilk(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        // Milk carton body
        val cartonWidth = w * 0.52f
        val cartonHeight = h * 0.72f
        val left = (w - cartonWidth) / 2
        val top = h * 0.22f

        // Carton main body
        drawRoundRect(
            color = Color(0xFFE2DFD2), // soft oat cream
            topLeft = Offset(left, top),
            size = Size(cartonWidth, cartonHeight),
            cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
        )

        // Carton top fold
        val foldPath = Path().apply {
            moveTo(left, top)
            lineTo(left + cartonWidth * 0.5f, top - h * 0.12f)
            lineTo(left + cartonWidth, top)
            close()
        }
        drawPath(foldPath, color = Color(0xFFD4CFC0))

        // Cap
        drawRoundRect(
            color = Color(0xFF43A047),
            topLeft = Offset(left + cartonWidth * 0.3f, top - h * 0.16f),
            size = Size(cartonWidth * 0.4f, h * 0.05f),
            cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
        )

        // Blue label strip
        drawRoundRect(
            color = Color(0xFF81D4FA),
            topLeft = Offset(left + cartonWidth * 0.1f, top + cartonHeight * 0.25f),
            size = Size(cartonWidth * 0.8f, cartonHeight * 0.45f),
            cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
        )

        // Leaf / oat stem
        val stemPath = Path().apply {
            moveTo(left + cartonWidth * 0.5f, top + cartonHeight * 0.6f)
            cubicTo(
                left + cartonWidth * 0.4f, top + cartonHeight * 0.45f,
                left + cartonWidth * 0.6f, top + cartonHeight * 0.35f,
                left + cartonWidth * 0.5f, top + cartonHeight * 0.3f
            )
        }
        drawPath(stemPath, color = Color(0xFF2E7D32), style = Stroke(width = 2.dp.toPx()))
    }
}