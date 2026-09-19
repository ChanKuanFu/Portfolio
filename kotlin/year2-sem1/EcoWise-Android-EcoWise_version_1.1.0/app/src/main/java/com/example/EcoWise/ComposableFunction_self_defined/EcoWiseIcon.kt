package com.example.EcoWise.ComposableFunction_self_defined

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.EcoWise.R
import com.example.EcoWise.ui.theme.*

/*
    @Composable
    @ComposableTarget
    public fun Icon(
        painter: Painter,
        contentDescription: String?,
        modifier: Modifier = Modifier,
        tint: Color = LocalContentColor.current
    ): Unit
*/
const val defaultIconSize : Int = 25

@Composable
fun EcoWiseIcon(
    iconPainterResourceID : Int,    // if use Icon() , that means R.drawable id is necessary , so not Int?
    iconContentDescription : String? = null,
    iconSize : Dp? = null,         // -> modifier
    iconColor : Color? = null
){
    Icon(
        painter = painterResource(id = iconPainterResourceID),
        contentDescription = iconContentDescription,
        tint = iconColor ?: TextPrimary,   // default as primary text color  (almost Color.Black)
        modifier = Modifier.size(iconSize ?: defaultIconSize.dp)
    )
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun PreviewOnEcoWiseIcon(){
    EcoWiseIcon(
        iconPainterResourceID = R.drawable.eco,
        iconContentDescription = "EcoWise",
        iconSize = 48.dp,
        iconColor = SecondaryActionButtonText
    )
}