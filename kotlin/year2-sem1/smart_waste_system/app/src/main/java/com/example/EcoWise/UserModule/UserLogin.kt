package com.example.EcoWise.UserModule

import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.EcoWise.ComposableFunction_self_defined.EcoWiseButton
import com.example.EcoWise.ComposableFunction_self_defined.EcoWiseIcon
import com.example.EcoWise.ComposableFunction_self_defined.EcoWiseText
import com.example.EcoWise.ComposableFunction_self_defined.EcoWiseTextField
import com.example.EcoWise.ui.theme.*
import com.example.EcoWise.components.GoogleLogoIcon
import com.example.EcoWise.data.EcoWiseRepository
import com.example.EcoWise.RoomDB.EcoWiseViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.EcoWise.R

@Composable
fun UserLogin(
    navController: NavController,
    initialIsRegister: Boolean = false,
    modifier: Modifier = Modifier,
    viewModel: EcoWiseViewModel = viewModel()
) {
    var isRegister by remember { mutableStateOf(initialIsRegister) }
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var showForgotPasswordDialog by remember { mutableStateOf(false) }

    var fullNameError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    fun resetErrors() {
        fullNameError = null
        emailError = null
        passwordError = null
    }

    fun validateAndSubmit() : UserAccount? {  // must return an UserAccount obj or null
        var isValid = true      // check if input is valid

        // get username
        if (isRegister) {
            val trimmedName = fullName.trim()
            if (trimmedName.isEmpty()) {
                fullNameError = "Please enter your full name"
                isValid = false
            } else if (trimmedName.length < 2) {
                fullNameError = "Name must be at least 2 characters"
                isValid = false
            } else {
                fullNameError = null
            }
        } else {
            fullNameError = null
        }


        val trimmedEmail = email.trim()

        // regex for email format input validation
        val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

        if (trimmedEmail.isEmpty()) {
            emailError = "Please enter your email address"
            isValid = false
        } else if (!emailRegex.matches(trimmedEmail)) {
            emailError = "Please enter a valid email (e.g. user@domain.com)"
            isValid = false
        } else {
            emailError = null
        }

        if (password.isEmpty()) {
            passwordError = "Please enter your password"
            isValid = false
        } else if (password.length < 6) {
            passwordError = "Password must be at least 6 characters"
            isValid = false
        } else {
            passwordError = null
        }

        // return UserAccount obj when input is valid
        return if (isValid) {
            focusManager.clearFocus() // to hide the keyboard

            // create a new UserAccount obj
            UserAccount(
                // if in sign in mode, return a default name or default as EcoWise User
                fullName = if (isRegister) fullName.trim() else "EcoWise User",
                email = email.trim(),
                isGoogleAccount = false
            )
        } else {
            null
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkHeader)
    ) {
        // Top Header Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            // EcoWise Brand Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    EcoWiseIcon(
                        iconPainterResourceID = R.drawable.eco,
                        iconContentDescription = "EcoWise Logo",
                        iconColor = Color.White,
                        iconSize = 20.dp
                    )
                }
                EcoWiseText(
                    textMsg = "EcoWise",
                    textSize = 22.sp,
                    textWeight = FontWeight.Bold,
                    textColor = Color.White
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                EcoWiseText(
                    textMsg = if (isRegister) "Create account" else "Welcome back!",
                    textSize = 28.sp,
                    textWeight = FontWeight.Bold,
                    textColor = Color.White,
                    textPadding = 0.dp
                )
                if (isRegister) {
                    EcoWiseIcon(
                        iconPainterResourceID = R.drawable.ecoscore,
                        iconContentDescription = "Welcome Icon",
                        iconColor = Green_Mint,
                        iconSize = 40.dp
                    )
                } else {
                    EcoWiseIcon(
                        iconPainterResourceID = R.drawable.welcome,
                        iconContentDescription = "Welcome Icon",
                        iconColor = StarYellow,
                        iconSize = 40.dp
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Subtitle
            EcoWiseText(
                textMsg = if (isRegister) "Join the movement for a greener future" else "Sign in to continue your journey",
                textSize = 14.sp,
                textColor = Color(0xFFD1E7D4)
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        // White Bottom Sheet Section
        Surface(
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp, vertical = 24.dp)
                    .imePadding()
                    .navigationBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Segmented Toggle: [ Sign In | Register ]
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Green_Light_BG,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Sign In Pill
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (!isRegister) Color.White else PrimaryActionButtonTransparent)
                                .then(
                                    if (!isRegister) Modifier.shadow(
                                        elevation = 2.dp,
                                        shape = RoundedCornerShape(12.dp)
                                    ) else Modifier
                                )
                                .clickable {
                                    isRegister = false
                                    resetErrors()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            EcoWiseText(
                                textMsg = "Sign In",
                                textSize = 14.sp,
                                textWeight = if (!isRegister) FontWeight.Bold else FontWeight.Medium,
                                textColor = if (!isRegister) Color.Black else Color(0xFF374151)
                            )
                        }

                        // Register Pill
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isRegister) Color.White else Color.Transparent)
                                .then(
                                    if (isRegister) Modifier.shadow(
                                        elevation = 2.dp,
                                        shape = RoundedCornerShape(12.dp)
                                    ) else Modifier
                                )
                                .clickable {
                                    isRegister = true
                                    resetErrors()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            EcoWiseText(
                                textMsg = "Register",
                                textSize = 14.sp,
                                textWeight = if (isRegister) FontWeight.Bold else FontWeight.Medium,
                                textColor = if (isRegister) Color.Black else Color(0xFF374151)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Full Name field (Register only)
                AnimatedVisibility(
                    visible = isRegister,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                    ) {
                        EcoWiseText(
                            textMsg = "FULL NAME",
                            textSize = 11.sp,
                            textWeight = FontWeight.Bold,
                            textColor = if (fullNameError != null) Color(0xFFDC2626) else Color.Black
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        EcoWiseTextField(
                            isOutlined = true,
                            textFieldVar = fullName,
                            onVarChange = {
                                fullName = it
                                if (fullNameError != null) fullNameError = null
                            },
                            textFieldPlaceholder = {
                                EcoWiseText(
                                    textMsg = "John Doe",
                                    textColor = Color(0xFF4B5563),
                                    textSize = 14.sp
                                )
                            },
                            textFieldLeadingIcon = {
                                EcoWiseIcon(
                                    iconPainterResourceID = R.drawable.person,
                                    iconContentDescription = "Full Name",
                                    iconColor = if (fullNameError != null) Color(0xFFDC2626) else Color(0xFF4B5563),
                                    iconSize = 20.dp
                                )
                            },
                            textFieldIsError = fullNameError != null,
                            textFieldSupportingText = if (fullNameError != null) {
                                {
                                    EcoWiseText(
                                        textMsg = fullNameError,
                                        textColor = Color(0xFFDC2626),
                                        textSize = 12.sp
                                    )
                                }
                            } else null,
                            textFieldSingleLine = true,
                            textFieldImeAction = ImeAction.Next
                        )
                    }
                }

                // Email field
                Column(modifier = Modifier.fillMaxWidth()) {
                    // show color based on error state
                    val currentStateColor = if (emailError != null) ErrorHeader else DarkHeader
                    EcoWiseText(
                        textMsg = "EMAIL",
                        textSize = 11.sp,
                        textWeight = FontWeight.Bold,
                        textColor = currentStateColor
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    EcoWiseTextField(
                        isOutlined = true,
                        textFieldVar = email,
                        onVarChange = {
                            email = it
                            if (emailError != null) emailError = null
                        },
                        textFieldPlaceholder = {
                            EcoWiseText(
                                textMsg = "Your Email",
                                textColor = TextMuted,
                                textSize = 14.sp
                            )
                        },
                        textFieldLeadingIcon = {
                            EcoWiseIcon(
                                iconPainterResourceID = R.drawable.email,
                                iconContentDescription = "Email",
                                iconColor = currentStateColor,
                                iconSize = 20.dp
                            )
                        },
                        textFieldIsError = emailError != null,
                        textFieldSupportingText = if (emailError != null) {
                            {
                                EcoWiseText(
                                    textMsg = emailError,
                                    textColor = ErrorHeader,
                                    textSize = 12.sp
                                )
                            }
                        } else null,
                        textFieldSingleLine = true,
                        textFieldKeyboardType = KeyboardType.Email,
                        textFieldImeAction = ImeAction.Next
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Password field
                Column(modifier = Modifier.fillMaxWidth()) {
                    // show color based on its state
                    val currentStateColor = if (passwordError != null) ErrorHeader else DarkHeader
                    val iconTint = if (passwordError != null) ErrorHeader else DarkHeader

                    EcoWiseText(
                        textMsg = "PASSWORD",
                        textSize = 11.sp,
                        textWeight = FontWeight.Bold,
                        textColor = currentStateColor
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    EcoWiseTextField(
                        isOutlined = true,
                        textFieldVar = password,
                        onVarChange = {
                            password = it
                            if (passwordError != null) passwordError = null
                        },
                        textFieldPlaceholder = {
                            EcoWiseText(
                                textMsg = "••••••••",
                                textColor = TextMuted,
                                textSize = 14.sp
                            )
                        },
                        textFieldLeadingIcon = {
                            EcoWiseIcon(
                                iconPainterResourceID = R.drawable.unlock,
                                iconContentDescription = "Password",
                                iconColor = iconTint,
                                iconSize = 20.dp
                            )
                        },
                        textFieldTrailingIcon = {
                            EcoWiseButton(
                                iconPainterResourceID = if (passwordVisible) R.drawable.visibility_off else R.drawable.visibility,
                                iconContentDescription = if (passwordVisible) "Hide password" else "Show password",
                                iconColor = iconTint,
                                iconSize = 20.dp,
                                buttonSize = 40.dp,
                                buttonColor = Color.Transparent,
                                onClickChange = { passwordVisible = !passwordVisible }  // change state
                            )
                        },
                        textFieldIsError = passwordError != null,
                        textFieldSupportingText = if (passwordError != null) {
                            {
                                EcoWiseText(
                                    textMsg = passwordError,
                                    textColor = ErrorHeader,
                                    textSize = 12.sp
                                )
                            }
                        } else null,
                        textFieldSingleLine = true,
                        textFieldKeyboardType = KeyboardType.Password,
                        textFieldImeAction = ImeAction.Done,
                        textFieldVisualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        textFieldKeyboardActions = KeyboardActions(
                            onDone = { validateAndSubmit() }
                        )
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // get current context for Toast
                val currentSigninRegisterContext = LocalContext.current

                // Submit Button -- "Sign In →" | "Register →"
                EcoWiseButton(
                    buttonEnabled = true,
                    buttonIsFillMaxWidth = true,
                    buttonColor = TextField_FocusedBorder_Light,
                    buttonRoundedCornerShapeTopStart = 15,
                    buttonRoundedCornerShapeTopEnd = 15,
                    buttonRoundedCornerShapeBottomStart = 15,
                    buttonRoundedCornerShapeBottomEnd = 15,
                    buttonElevation = 2.dp,

                    text = if (isRegister) "Register" else "Sign In",
                    textSize = 16.sp,
                    textWeight = FontWeight.SemiBold,
                    textColor = AgreeText,

                    iconPainterResourceID = R.drawable.arrowforward,
                    iconPosition = "End",
                    iconColor = AgreeText,
                    iconSize = 18.dp,

                    onClickChange = {
                        val currentUser = validateAndSubmit()

                        if (currentUser != null) {
                            val normalizedEmail = (if (email.contains("@")) email.trim() else "${email.trim()}@gmail.com").lowercase()

                            scope.launch {
                                try {
                                    if (isRegister) {
                                        val signUpSuccess = viewModel.signUp(
                                            currentUser.id,
                                            currentUser.fullName,
                                            normalizedEmail,
                                            password,
                                            false
                                        )
                                        if (signUpSuccess) {
                                            Toast.makeText(
                                                currentSigninRegisterContext,
                                                "Successfully Registered a New Account",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            navController.navigate(route = "EcoWise Home Screen/${currentUser.fullName}") {
                                                popUpTo("EcoWise Login") {
                                                    inclusive = true
                                                }
                                            }
                                        } else {
                                            Toast.makeText(
                                                currentSigninRegisterContext,
                                                "Account already exists!",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    } else {
                                        val signedInUser = viewModel.signIn(normalizedEmail, password)
                                        if (signedInUser != null) {
                                            Toast.makeText(
                                                currentSigninRegisterContext,
                                                "Successfully Signed In",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            val displayName = signedInUser.fullName.ifBlank { "EcoWise User" }
                                            navController.navigate(route = "EcoWise Home Screen/$displayName") {
                                                popUpTo("EcoWise Login") {
                                                    inclusive = true
                                                }
                                            }
                                        } else {
                                            Toast.makeText(
                                                currentSigninRegisterContext,
                                                "Invalid email or password!",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        currentSigninRegisterContext,
                                        "Authentication failed: ${e.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(22.dp))

                // "or continue with" Divider
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = Color(0xFFE5E7EB)
                    )
                    EcoWiseText(
                        textMsg = if (isRegister) "OR CONTINUE WITH" else "or continue with",
                        textSize = 12.sp,
                        textColor = TextSecondary,
                        textPadding = 14.dp
                    )
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = Divider
                    )
                }

                Spacer(modifier = Modifier.height(22.dp))

                // get current context from screen for Toast
                val ecoWiseLoginScreenContext = LocalContext.current

                // "Continue with Google" Button
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Green_Light_BG,
                    border = BorderStroke(1.dp, Green_Mint_BG),
                    shadowElevation = 1.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clickable {
                            focusManager.clearFocus()

                            val googleUser = UserAccount(
                                fullName = "Google User",
                                email = "user@gmail.com",
                                isGoogleAccount = true
                            )

                            scope.launch {
                                try {
                                    val signUpResult = viewModel.signUp(googleUser.id, googleUser.fullName, googleUser.email, "GOOGLE_AUTH", true)
                                    val signedIn = if (signUpResult) {
                                        googleUser.fullName
                                    } else {
                                        val existing = viewModel.signIn(googleUser.email, "GOOGLE_AUTH")
                                        existing?.fullName ?: googleUser.fullName
                                    }

                                    Toast.makeText(
                                        ecoWiseLoginScreenContext,
                                        "Successfully logged in with ${googleUser.email} !!!",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    navController.navigate(route = "EcoWise Home Screen/$signedIn") {
                                        popUpTo("EcoWise Login") {
                                            inclusive = true
                                        }
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(
                                        ecoWiseLoginScreenContext,
                                        "Google Sign-In failed: ${e.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        GoogleLogoIcon(modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        EcoWiseText(
                            textMsg = "Continue with Google",
                            textSize = 15.sp,
                            textWeight = FontWeight.SemiBold,
                            textColor = DeepBlack
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // "Forgot password?" or "Already have an account?"
                if (isRegister) {
                    EcoWiseButton(
                        text = "Already have an account?",
                        textSize = 14.sp,
                        textWeight = FontWeight.SemiBold,
                        textColor = Green_Primary,
                        onClickChange = {
                            isRegister = false
                            resetErrors()
                        }
                    )
                } else {
                    EcoWiseButton(
                        text = "Forgot password?",
                        textSize = 14.sp,
                        textWeight = FontWeight.SemiBold,
                        textColor = Green_Primary,
                        onClickChange = {
                            showForgotPasswordDialog = true
                        }
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }

    // Simple confirmation dialog for "Forgot password?"
    if (showForgotPasswordDialog) {
        AlertDialog(
            onDismissRequest = { showForgotPasswordDialog = false },
            title = {
                EcoWiseText(
                    textMsg = "Reset Password",
                    textWeight = FontWeight.Bold,
                    textSize = 18.sp,
                    textColor = Green_Primary
                )
            },
            text = {
                EcoWiseText(
                    textMsg = "A password reset link has been sent to your email address.",
                    textSize = 14.sp,
                    textColor = TextTertiary
                )
            },
            confirmButton = {
                EcoWiseButton(
                    text = "Got It",
                    textColor = AgreeButton,
                    textWeight = FontWeight.Bold,
                    onClickChange = { showForgotPasswordDialog = false }
                )
            },
            containerColor = Color.White,
            shape = RoundedCornerShape(20.dp)
        )
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun PreviewOnUserLogin(){
    UserLogin(
        navController = rememberNavController(),
        initialIsRegister = false,
        modifier = Modifier
    )
}
