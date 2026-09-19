package com.example.EcoWise.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.EcoWise.ComposableFunction_self_defined.EcoWiseText
import com.example.EcoWise.ui.theme.*

@Composable
fun EcoCircularGauge(
    percentage: Float, // 0.0 to 1.0    ->  0% to 100%
    displayText: String,
    subText: String? = null,
    gaugeColor: Color = Green_Primary,
    trackColor: Color = CardBorder,
    strokeWidth: Dp = 8.dp,
    sizeDp: Dp = 100.dp,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = percentage,
        animationSpec = tween(durationMillis = 1000),
        label = "gauge"
    )

    Box(
        modifier = modifier.size(sizeDp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val showStrokeWidth = strokeWidth.toPx()
            val arcSize = size.width - showStrokeWidth

            // Track ()
            drawArc(
                color = trackColor,
                startAngle = -90f, // rotate 90 degree with anticlockwise
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(showStrokeWidth / 2, showStrokeWidth / 2),
                size = Size(arcSize, arcSize),
                style = Stroke(width = showStrokeWidth, cap = StrokeCap.Round)
            )
            // Progress
            drawArc(
                color = gaugeColor,
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                topLeft = Offset(showStrokeWidth / 2, showStrokeWidth / 2),
                size = Size(arcSize, arcSize),
                style = Stroke(width = showStrokeWidth, cap = StrokeCap.Round)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            EcoWiseText(
                textMsg = displayText, // current score in percentage
                textSize = if (subText != null) 25.sp else 20.sp,
                textFamily = FontFamily.Cursive,
                textWeight = FontWeight.ExtraBold,
                textColor = Green_Primary
            )
            if (subText != null) {
                EcoWiseText(
                    textMsg = subText,
                    textSize = 10.sp,
                    textFamily = FontFamily.SansSerif,
                    textWeight = FontWeight.Light,
                    textColor = Green_Forest
                )
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun PreviewOn(){
    EcoCircularGauge(
        percentage = 0.8f,
        displayText = "80%",
        subText = "Eco Score"
    )
}
