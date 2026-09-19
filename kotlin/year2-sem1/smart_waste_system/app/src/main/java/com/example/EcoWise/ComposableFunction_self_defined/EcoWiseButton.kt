package com.example.EcoWise.ComposableFunction_self_defined

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.EcoWise.ui.theme.Green_Bright
import com.example.EcoWise.ui.theme.PrimaryActionButton
import com.example.EcoWise.ui.theme.PrimaryActionButtonTransparent
import com.example.EcoWise.ui.theme.PrimaryIconButton

/*
    @Stable
    public fun Modifier.size(
        size: Dp
    ): Modifier

    @Composable
    public fun painterResource(
        @DrawableRes id: Int
    ): Painter

    // Button Shape
    public fun RoundedCornerShape(
        topStart: Dp = 0.dp,
        topEnd: Dp = 0.dp,
        bottomEnd: Dp = 0.dp,
        bottomStart: Dp = 0.dp
    ): RoundedCornerShape

    public fun RoundedCornerShape(
        size: Dp
    ): RoundedCornerShape

    @Stable
    public fun BorderStroke(
        width: Dp,
        color: Color
    ): BorderStroke
*/

const val defaultButtonSize : Int = 25

const val defaultButtonRoundedCornerShapeTopStart : Int = 25
const val defaultButtonRoundedCornerShapeTopEnd : Int = 25
const val defaultButtonRoundedCornerShapeBottomStart : Int = 25
const val defaultButtonRoundedCornerShapeBottomEnd : Int = 25

@Composable
fun EcoWiseButton(
    // condition to detect able to use button
    buttonEnabled : Boolean? = true,

    // Button()
    buttonColor : Color? = null,
    buttonSize : Dp? = null,           // -> modifier
    buttonIsFillMaxWidth : Boolean? = false,
    // for RoundedCornerShape(topStart = a.dp, topEnd = b.dp, bottomEnd = c.dp, bottomStart = d.dp)
    buttonRoundedCornerShapeTopStart : Int? = null,
    buttonRoundedCornerShapeTopEnd : Int? = null,
    buttonRoundedCornerShapeBottomStart : Int? = null,
    buttonRoundedCornerShapeBottomEnd : Int? = null,
    // for z-axis elevation
    buttonElevation : Dp? = null,
    // BorderStroke(width = 0.dp, color = Color.Transparent)
    buttonBorderWidth : Dp? = null,
    buttonBorderColor : Color? = null,
    // Content padding
    // ButtonDefaults.ContentPadding
    buttonContentPadding : Dp? = null,

    // Text()
    text : String? = null,
    textSize:  TextUnit? = null,
    textStyle: FontStyle? = null,
    textWeight: FontWeight? = null,
    textFamily: FontFamily? = null,
    textMsgDecoration: TextDecoration? = null,
    textColor: Color? = null,
    textPadding: Dp? = null,

    // Icon()
    iconPainterResourceID : Int? = null,
    iconContentDescription : String? = null,
    iconSize : Dp? = null,         // -> modifier
    iconColor : Color? = null,
    iconPosition : String? = "End",  // Start or End

    // Button() action
    onClickChange : () -> Unit,

    // Modifier
    modifierPadding : Dp? = null,
    modifier : Modifier? = null
){
    val getButtonColor = buttonColor ?: PrimaryActionButton

    val getRoundedCornerShapeTopStart = buttonRoundedCornerShapeTopStart ?: defaultButtonRoundedCornerShapeTopStart
    val getRoundedCornerShapeTopEnd = buttonRoundedCornerShapeTopEnd ?: defaultButtonRoundedCornerShapeTopEnd
    val getRoundedCornerShapeBottomStart = buttonRoundedCornerShapeBottomStart ?: defaultButtonRoundedCornerShapeBottomStart
    val getRoundedCornerShapeBottomEnd = buttonRoundedCornerShapeBottomEnd ?: defaultButtonRoundedCornerShapeBottomEnd

    val getButtonBorderWidth = buttonBorderWidth ?: 0.dp
    val getButtonBorderColor : Color = (
            if (buttonBorderColor == null) PrimaryActionButtonTransparent
            else buttonBorderColor
            )

    val getButtonContentPadding = buttonContentPadding ?: 0.dp

    // decide to use Button or IconButton
    // no Text() but has Icon() -> iconButton
    if (text == null && iconPainterResourceID != null){
        IconButton(
            onClick = onClickChange,
            modifier = Modifier
                .padding(modifierPadding ?: 0.dp)
                .size(buttonSize ?: 48.dp),
            enabled = buttonEnabled ?: true,
            // shape default to circle
            colors = IconButtonDefaults.iconButtonColors(getButtonColor)
        ){
            EcoWiseIcon(
                iconPainterResourceID = iconPainterResourceID,
                iconContentDescription = iconContentDescription ?: "",
                iconSize = iconSize,
                iconColor = iconColor ?: PrimaryIconButton
            )
        }
    }
    else if (text != null && iconPainterResourceID == null){
        TextButton(
            onClick = onClickChange,
            modifier = Modifier
                .padding(modifierPadding ?: 0.dp)
                .then(
                    if (buttonSize != null) Modifier.size(buttonSize)
                    else Modifier
                ),
            enabled = buttonEnabled ?: true,
            contentPadding = PaddingValues(getButtonContentPadding)
        ){
            EcoWiseText(
                textMsg = text,
                textSize = textSize,
                textStyle = textStyle,
                textWeight = textWeight,
                textFamily = textFamily,
                textMsgDecoration = textMsgDecoration,
                textColor = textColor,
                textPadding = textPadding
            )
        }
    }
    else {
        // has Text() -> Button
        // then decide whether is need to add on icon()
        // has Text() but no Icon()
        val isFillMaxWidth = buttonIsFillMaxWidth ?: false
        Button(
            onClick = onClickChange,
            modifier = Modifier
                .padding(modifierPadding ?: 0.dp)
                .then(
                    if (isFillMaxWidth) {
                        Modifier.fillMaxWidth()
                    } else {
                        Modifier.size(buttonSize ?: defaultButtonSize.dp)
                    }
                ),
            enabled = buttonEnabled ?: true,
            shape = RoundedCornerShape(
                topStart = getRoundedCornerShapeTopStart.dp,
                topEnd = getRoundedCornerShapeTopEnd.dp,
                bottomStart = getRoundedCornerShapeBottomStart.dp,
                bottomEnd = getRoundedCornerShapeBottomEnd.dp
            ),
            colors = ButtonDefaults.buttonColors(
                getButtonColor
            ),
            elevation = ButtonDefaults.buttonElevation(
                buttonElevation ?: 0.dp
            ),
            border = BorderStroke(
                width = getButtonBorderWidth,
                color = getButtonBorderColor
            ),
            //contentPadding: PaddingValues = ButtonDefaults.ContentPadding
            contentPadding = PaddingValues(getButtonContentPadding)
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                // has Icon()
                // icon position
                val isIconFirst : Boolean = if(iconPosition?.equals("Right",ignoreCase = true) == true) true else false
                if(iconPainterResourceID != null && isIconFirst){   // icon position -> start
                    EcoWiseIcon(
                        iconPainterResourceID = iconPainterResourceID,
                        iconContentDescription = iconContentDescription ?: "",
                        iconSize = iconSize,
                        iconColor = iconColor ?: Green_Bright
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                }
                EcoWiseText(
                    textMsg = text,
                    textSize = textSize,
                    textStyle = textStyle,
                    textWeight = textWeight,
                    textFamily = textFamily,
                    textMsgDecoration = textMsgDecoration,
                    textColor = textColor,
                    textPadding = textPadding
                )
                if(iconPainterResourceID != null && !isIconFirst){  // icon position -> end
                    Spacer(modifier = Modifier.width(10.dp))
                    EcoWiseIcon(
                        iconPainterResourceID = iconPainterResourceID,
                        iconContentDescription = iconContentDescription ?: "",
                        iconSize = iconSize,
                        iconColor = iconColor ?: Green_Bright
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun PreviewOnEcoWiseButton(){
    EcoWiseButton(
        buttonIsFillMaxWidth = true,
        onClickChange = {}
    )
}
