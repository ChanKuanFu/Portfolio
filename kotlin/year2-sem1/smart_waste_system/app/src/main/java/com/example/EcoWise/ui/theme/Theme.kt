package com.example.EcoWise.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Green_Dark,
    onPrimary = Color.White,
    secondary = Green_Light,
    background = DeepNavy,
    surface = CardBackground,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    error = AlertRed
)

private val LightColorScheme = lightColorScheme(
    primary = Green_Primary,
    onPrimary = Color.White,
    secondary = Green_Medium,
    tertiary = Green_Light,
    background = Green_Light_BG,
    surface = CardBackground,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    error = AlertRed
)

@Composable
fun MAD_EcoWise_TeeZhongKai_ChanKuanFu_LeeGinShyang_LimChunChenTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // to ensure a consistent green design
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}