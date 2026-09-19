package com.example.EcoWise.ComposableFunction_self_defined

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.EcoWise.ui.theme.TextPrimary

/*
    @Composable
    @ComposableTarget
    public fun Text(
        text: String,
        modifier: Modifier = Modifier,
        color: Color = Color.Unspecified,

        fontSize: TextUnit = TextUnit.Unspecified,
        fontStyle: FontStyle? = null,
        fontWeight: FontWeight? = null,
        fontFamily: FontFamily? = null,

        textDecoration: TextDecoration? = null,
        textAlign: TextAlign? = null,
        lineHeight: TextUnit = TextUnit.Unspecified
    ): Unit

*/

@Composable
fun EcoWiseText(
    textMsg: String?,
    textSize: TextUnit? = null,
    textStyle: FontStyle? = null,
    textWeight: FontWeight? = null,
    textFamily: FontFamily? = null,
    textMsgDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    textColor: Color? = null,
    textPadding: Dp? = null,
    modifier: Modifier? = null
) {
    val defaultTypography = MaterialTheme.typography.bodyMedium

    Text(
        text = textMsg ?: "",
        fontSize =
            if(textSize == null|| textSize == 0.sp) defaultTypography.fontSize
            else textSize
        ,
        fontStyle = textStyle ?: defaultTypography.fontStyle,
        fontWeight = textWeight ?: defaultTypography.fontWeight,
        fontFamily = textFamily ?: defaultTypography.fontFamily,
        textDecoration = textMsgDecoration ?: defaultTypography.textDecoration,
        textAlign = textAlign ?: TextAlign.Start,   // Start -> Left | End -> Right | Center -> Center
        color = textColor ?: TextPrimary,   // default as primary text color (almost Color.Black)
        modifier = modifier ?: Modifier.padding(textPadding ?: 0.dp)
    )
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun PreviewOnEcoWiseText(){
    EcoWiseText(
        textMsg = "EcoWise",
        textFamily = FontFamily.Cursive
    )
}
