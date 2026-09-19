package com.example.EcoWise.ComposableFunction_self_defined

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.EcoWise.ui.theme.*
import com.example.EcoWise.R

/*
    @Composable
    @ComposableInferredTarget
    public fun OutlinedTextField(
        value: String,
        onValueChange: (String) -> Unit,
        modifier: Modifier = Modifier,

        readOnly: Boolean = false,
        singleLine: Boolean = false,
        label: @Composable (() -> Unit)? = null,

        enabled: Boolean = true,
        isError: Boolean = false,

        placeholder: @Composable (() -> Unit)? = null,
        leadingIcon: @Composable (() -> Unit)? = null,
        trailingIcon: @Composable (() -> Unit)? = null,
        prefix: @Composable (() -> Unit)? = null,
        suffix: @Composable (() -> Unit)? = null,
        supportingText: @Composable (() -> Unit)? = null,

        keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
        keyboardActions: KeyboardActions = KeyboardActions.Default,


        maxLines: Int = if (singleLine) 1 else Int.MAX_…,
        minLines: Int = 1,

        colors: TextFieldColors = OutlinedTextFieldDefaults.color…
    ): Unit

    @Composable
    @ComposableInferredTarget
    public fun OutlinedTextField(
        value: String,
        onValueChange: (String) -> Unit,
        modifier: Modifier = Modifier,

        readOnly: Boolean = false,
        singleLine: Boolean = false,
        label: @Composable (() -> Unit)? = null,

        enabled: Boolean = true,
        isError: Boolean = false,

        placeholder: @Composable (() -> Unit)? = null,
        leadingIcon: @Composable (() -> Unit)? = null,
        trailingIcon: @Composable (() -> Unit)? = null,
        prefix: @Composable (() -> Unit)? = null,
        suffix: @Composable (() -> Unit)? = null,
        supportingText: @Composable (() -> Unit)? = null,

        keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
        keyboardActions: KeyboardActions = KeyboardActions.Default,


        maxLines: Int = if (singleLine) 1 else Int.MAX_…,
        minLines: Int = 1,

        colors: TextFieldColors = OutlinedTextFieldDefaults.color…
    ): Unit
* */

@Composable
fun EcoWiseTextField(
    // decide to change OutlinedTextField or TextField
    isOutlined : Boolean? = false,  // default as false -> TextField()
    // textfield | outlinedtextfield parameters
    textFieldVar : String,
    onVarChange : (String) -> Unit,
    // Modifier
    textFieldModifierPadding : Dp? = null,

    textFieldReadOnly : Boolean? = false,
    textFieldSingleLine : Boolean? = true,
    textFieldLabel : @Composable (() -> Unit)? = null,

    textFieldEnabled : Boolean? = true,
    textFieldIsError : Boolean? = false,

    textFieldPlaceholder : @Composable (() -> Unit)? = null,
    textFieldLeadingIcon : @Composable (() -> Unit)? = null,
    textFieldTrailingIcon : @Composable (() -> Unit)? = null,
    textFieldPrefix : @Composable (() -> Unit)? = null,
    textFieldSuffix : @Composable (() -> Unit)? = null,
    textFieldSupportingText : @Composable (() -> Unit)? = null,

    // keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    // keyboardOptions( keyboardType = x, imeAction = ImeAction.x )
    // keyboardType: KeyboardType = KeyboardType.Unspecified
    // imeAction: ImeAction = ImeAction.Unspecified
    textFieldKeyboardType : KeyboardType? = KeyboardType.Unspecified,

    /*
        companion object {
            @Stable val Unspecified: ImeAction = ImeAction(-1)
            @Stable val Default: ImeAction = ImeAction(1)
            @Stable val None: ImeAction = ImeAction(0)
            @Stable val Go: ImeAction = ImeAction(2)
            @Stable val Search: ImeAction = ImeAction(3)
            @Stable val Send: ImeAction = ImeAction(4)
            @Stable val Previous: ImeAction = ImeAction(5)
            @Stable val Next: ImeAction = ImeAction(6)
            @Stable val Done: ImeAction = ImeAction(7)
        }
    */
    textFieldImeAction : ImeAction? = ImeAction.Unspecified,

    // keyboardAction: KeyboardActions = KeyboardActions.Default
    /*  'Based on the imeAction button type'
        public constructor KeyboardActions(
            onDone: (KeyboardActionScope.() -> Unit)? = null,
            onGo: (KeyboardActionScope.() -> Unit)? = null,
            onNext: (KeyboardActionScope.() -> Unit)? = null,
            onPrevious: (KeyboardActionScope.() -> Unit)? = null,
            onSearch: (KeyboardActionScope.() -> Unit)? = null,
            onSend: (KeyboardActionScope.() -> Unit)? = null
        )

        companion object {
            @Stable val Default: KeyboardActions = KeyboardActions()
        }
    */
    textFieldKeyboardActions : KeyboardActions? = KeyboardActions.Default,

    textFieldMaxLines : Int = if (textFieldSingleLine == true) 1 else 2,
    textFieldMinLines : Int = 1,

    textFieldVisualTransformation : VisualTransformation = VisualTransformation.None
    // color using its own theme
){
    // check whether system is in dark theme
    val inDarkMode = isSystemInDarkTheme()

    val customEcoWiseTextFieldColors = OutlinedTextFieldDefaults.colors(
        // Text color
        focusedTextColor = if (inDarkMode) TextField_FocusedText_Dark else TextField_FocusedText_Light,   // dark gray
        unfocusedTextColor = if (inDarkMode) TextField_UnfocusedText_Dark else TextField_UnfocusedText_Light, // gray
        disabledTextColor = if (inDarkMode) TextField_DisabledText_Dark else TextField_DisabledText_Light,  // light gray
        errorTextColor = if (inDarkMode) TextField_ErrorText_Dark else TextField_ErrorText_Light,     // dark red

        // TextField background color
        focusedContainerColor = if (inDarkMode) TextField_FocusedContainer_Dark else TextField_FocusedContainer_Light,      // light green
        unfocusedContainerColor = if (inDarkMode) TextField_UnfocusedContainer_Dark else TextField_UnfocusedContainer_Light,    // lighter gray
        disabledContainerColor = if (inDarkMode) TextField_DisabledContainer_Dark else TextField_DisabledContainer_Light,     // lighter gray
        errorContainerColor = if (inDarkMode) TextField_ErrorContainer_Dark else TextField_ErrorContainer_Light,        // lighter red

        // only for outlinedTextField since has border
        focusedBorderColor = if (inDarkMode) TextField_FocusedBorder_Dark else TextField_FocusedBorder_Light,     // green
        unfocusedBorderColor = if (inDarkMode) TextField_UnfocusedBorder_Dark else TextField_UnfocusedBorder_Light,   // light gray
        disabledBorderColor = if (inDarkMode) TextField_DisabledBorder_Dark else TextField_DisabledBorder_Light,    // lighter gray
        errorBorderColor = if (inDarkMode) TextField_ErrorBorder_Dark else TextField_ErrorBorder_Light,       // red

        // show cursor color
        cursorColor = if (inDarkMode) TextField_Cursor_Dark else TextField_Cursor_Light,        // green cursor
        errorCursorColor = if (inDarkMode) TextField_ErrorCursor_Dark else TextField_ErrorCursor_Light,   // red cursor

        //  show color when select the text
        selectionColors = TextSelectionColors(
            handleColor = if (inDarkMode) TextField_SelectionHandle_Dark else TextField_SelectionHandle_Light,    // green
            backgroundColor = if (inDarkMode) TextField_SelectionBg_Dark else TextField_SelectionBg_Light // lighter green for background
        ),

        // show leading Icon color before the 'value'
        focusedLeadingIconColor = if (!inDarkMode) TextField_FocusedIcon_Light else TextField_FocusedIcon_Dark,    // green
        unfocusedLeadingIconColor = if (!inDarkMode) TextField_UnfocusedIcon_Light else TextField_UnfocusedIcon_Dark,  // gray
        disabledLeadingIconColor = if (!inDarkMode) TextField_DisabledIcon_Light else TextField_DisabledIcon_Dark,   // light gray
        errorLeadingIconColor = if (!inDarkMode) TextField_ErrorIcon_Light else TextField_ErrorIcon_Dark,      // red

        // show trailing Icon color after the 'value'
        focusedTrailingIconColor = if (!inDarkMode) TextField_FocusedIcon_Light else TextField_FocusedIcon_Dark,   // green
        unfocusedTrailingIconColor = if (!inDarkMode) TextField_UnfocusedIcon_Light else TextField_UnfocusedIcon_Dark, // gray
        disabledTrailingIconColor = if (!inDarkMode) TextField_DisabledIcon_Light else TextField_DisabledIcon_Dark,  // light gray
        errorTrailingIconColor = if (!inDarkMode) TextField_ErrorIcon_Light else TextField_ErrorIcon_Dark,     // red

        // show 'label' color
        focusedLabelColor = if (inDarkMode) TextField_FocusedLabel_Dark else TextField_FocusedLabel_Light,      // green
        unfocusedLabelColor = if (inDarkMode) TextField_UnfocusedLabel_Dark else TextField_UnfocusedLabel_Light,    // gray
        disabledLabelColor = if (inDarkMode) TextField_DisabledLabel_Dark else TextField_DisabledLabel_Light,     // light gray
        errorLabelColor = if (inDarkMode) TextField_ErrorLabel_Dark else TextField_ErrorLabel_Light,        // red

        //  show the 'placeholder' color
        focusedPlaceholderColor = if (inDarkMode) TextField_FocusedPlaceholder_Dark else TextField_FocusedPlaceholder_Light,    // light green
        unfocusedPlaceholderColor = if (inDarkMode) TextField_UnfocusedPlaceholder_Dark else TextField_UnfocusedPlaceholder_Light,  // light gray
        disabledPlaceholderColor = if (inDarkMode) TextField_DisabledPlaceholder_Dark else TextField_DisabledPlaceholder_Light,//  lighter gray
        errorPlaceholderColor = if (inDarkMode) TextField_ErrorPlaceholder_Dark else TextField_ErrorPlaceholder_Light,  //  lighter red

        //  the supporting Text msg under textField
        focusedSupportingTextColor = if (inDarkMode) TextField_FocusedSupportingText_Dark else TextField_FocusedSupportingText_Light,     // green
        unfocusedSupportingTextColor = if (inDarkMode) TextField_UnfocusedSupportingText_Dark else TextField_UnfocusedSupportingText_Light,   // gray
        disabledSupportingTextColor = if (inDarkMode) TextField_DisabledSupportingText_Dark else TextField_DisabledSupportingText_Light,    // light gray
        errorSupportingTextColor = if (inDarkMode) TextField_ErrorSupportingText_Dark else TextField_ErrorSupportingText_Light,       // red

        //  the Prefix color before the 'value'
        focusedPrefixColor = if (!inDarkMode) TextField_FocusedPrefixSuffix_Light else TextField_FocusedPrefixSuffix_Dark,     // dark green
        unfocusedPrefixColor = if (!inDarkMode) TextField_UnfocusedPrefixSuffix_Light else TextField_UnfocusedPrefixSuffix_Dark,   // darker gray
        disabledPrefixColor = if (!inDarkMode) TextField_DisabledPrefixSuffix_Light else TextField_DisabledPrefixSuffix_Dark,    // light gray
        errorPrefixColor = if (!inDarkMode) TextField_ErrorPrefixSuffix_Light else TextField_ErrorPrefixSuffix_Dark,       // dark red

        // the Suffix color after the 'value'
        focusedSuffixColor = if (!inDarkMode) TextField_FocusedPrefixSuffix_Light else TextField_FocusedPrefixSuffix_Dark,     // dark green
        unfocusedSuffixColor = if (!inDarkMode) TextField_UnfocusedPrefixSuffix_Light else TextField_UnfocusedPrefixSuffix_Dark,   // darker gray
        disabledSuffixColor = if (!inDarkMode) TextField_DisabledPrefixSuffix_Light else TextField_DisabledPrefixSuffix_Dark,    // light gray
        errorSuffixColor = if (!inDarkMode) TextField_ErrorPrefixSuffix_Light else TextField_ErrorPrefixSuffix_Dark        // dark red
    )

    if(isOutlined == true){
        // OutlinedTextField()
        OutlinedTextField(
            value = textFieldVar,
            onValueChange = onVarChange,
            modifier = Modifier.padding(textFieldModifierPadding ?: 0.dp).fillMaxWidth(),

            readOnly = textFieldReadOnly ?: false,
            singleLine = textFieldSingleLine ?: true,
            label = textFieldLabel,

            enabled = textFieldEnabled ?: true,
            isError = textFieldIsError ?: false,

            placeholder = textFieldPlaceholder,
            leadingIcon = textFieldLeadingIcon,
            trailingIcon = textFieldTrailingIcon,
            prefix = textFieldPrefix,
            suffix = textFieldSuffix,
            supportingText = textFieldSupportingText,

            keyboardOptions = KeyboardOptions(
                keyboardType = textFieldKeyboardType ?: KeyboardType.Unspecified,
                imeAction = textFieldImeAction ?: ImeAction.Unspecified
            ),
            keyboardActions = textFieldKeyboardActions ?: KeyboardActions.Default,

            maxLines = textFieldMaxLines,
            minLines = textFieldMinLines,
            visualTransformation = textFieldVisualTransformation,
            colors = customEcoWiseTextFieldColors
        )
    }else{
        // TextField()
        TextField(
            value = textFieldVar,
            onValueChange = onVarChange,
            modifier = Modifier.padding(textFieldModifierPadding ?: 0.dp).fillMaxWidth(),

            readOnly = textFieldReadOnly ?: false,
            singleLine = textFieldSingleLine ?: true,
            label = textFieldLabel,

            enabled = textFieldEnabled ?: true,
            isError = textFieldIsError ?: false,

            placeholder = textFieldPlaceholder,
            leadingIcon = textFieldLeadingIcon,
            trailingIcon = textFieldTrailingIcon,
            prefix = textFieldPrefix,
            suffix = textFieldSuffix,
            supportingText = textFieldSupportingText,

            keyboardOptions = KeyboardOptions(
                keyboardType = textFieldKeyboardType ?: KeyboardType.Unspecified,
                imeAction = textFieldImeAction ?: ImeAction.Unspecified
            ),
            keyboardActions = textFieldKeyboardActions ?: KeyboardActions.Default,

            maxLines = textFieldMaxLines,
            minLines = textFieldMinLines,
            visualTransformation = textFieldVisualTransformation,
            colors = customEcoWiseTextFieldColors
        )
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun PreviewOnEcoWiseTextField(){
    EcoWiseTextField(
        textFieldVar = "EcoWise",
        onVarChange = {null},
        textFieldReadOnly = true,
        isOutlined = true,
        textFieldEnabled = true,
        textFieldIsError = false,

        textFieldPrefix = {EcoWiseText(textMsg = "MAD")},
        textFieldSuffix = {EcoWiseText(textMsg = "Project")},

        textFieldLeadingIcon = {
            EcoWiseIcon(iconPainterResourceID = R.drawable.eco)
        },
        textFieldTrailingIcon = {
            EcoWiseIcon(iconPainterResourceID = R.drawable.filled_heart)
        },

        textFieldSupportingText = {
            EcoWiseText(textMsg = "EcoWise OutlinedTextField")
        }
    )
}
