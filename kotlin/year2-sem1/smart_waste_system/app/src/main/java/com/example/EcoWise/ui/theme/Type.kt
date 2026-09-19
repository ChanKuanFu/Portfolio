package com.example.EcoWise.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp

const val defaultTextSize : Int = 25
val Typography = Typography(
    // use for button text
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontStyle = FontStyle.Normal,
        // Underline or Delete_line (LineThrough)
        textDecoration = TextDecoration.None,
        fontSize = defaultTextSize.sp,
        color = PrimaryActionButtonText
    ),
    // use for TopAppBar title
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontStyle = FontStyle.Normal,
        // Underline or Delete_line (LineThrough)
        textDecoration = TextDecoration.None,
        fontSize = defaultTextSize.sp,
        color = LightHeader
    ),
    // use for BottomAppBar screen name
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontStyle = FontStyle.Normal,
        // Underline or Delete_line (LineThrough)
        textDecoration = TextDecoration.None,
        fontSize = defaultTextSize.sp,
        color = LightHeader
    )
)