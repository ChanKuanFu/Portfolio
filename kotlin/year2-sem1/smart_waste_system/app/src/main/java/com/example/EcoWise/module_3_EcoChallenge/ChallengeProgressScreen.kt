package com.example.EcoWise.module_3_EcoChallenge

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.EcoWise.ComposableFunction_self_defined.EcoWiseButton
import com.example.EcoWise.ComposableFunction_self_defined.EcoWiseIcon
import com.example.EcoWise.ComposableFunction_self_defined.EcoWiseText
import com.example.EcoWise.ComposableFunction_self_defined.EcoWiseTextField
import com.example.EcoWise.components.EcoWiseMainScaffold
import com.example.EcoWise.data.EcoWiseRepository
import com.example.EcoWise.model.EcoChallenge
import com.example.EcoWise.ui.theme.*
import com.example.EcoWise.R
import kotlinx.coroutines.launch


@Composable
fun ChallengeProgressScreen(
    navController: NavController,
    challenge: EcoChallenge? = null,
    modifier: Modifier = Modifier
) {
    val currentRoute = navController.currentDestination?.route
    var activityText by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var activityError by remember { mutableStateOf(false) }
    var hasAttemptedSubmit by remember { mutableStateOf(false) }
    var isSubmitting by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val challengeTitle = challenge?.title ?: "Plastic-Free Week"

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
        }
    }

    EcoWiseMainScaffold(
        navController = navController,
        currentRoute = currentRoute,
        title = "Challenge Progress",
        showBackButton = true,
        onBackClick = { navController.popBackStack() }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Green_Mint_BG),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            item {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    EcoWiseText(
                        textMsg = "Today's Activity",
                        textSize = 20.sp,
                        textWeight = FontWeight.Bold,
                        textColor = Green_Primary
                    )
                }
            }

            // Activity Description Label
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    EcoWiseText(
                        textMsg = "What did you do?",
                        textSize = 14.sp,
                        textColor = TextSecondary
                    )
                    EcoWiseText(
                        textMsg = "*",
                        textSize = 14.sp,
                        textWeight = FontWeight.Bold,
                        textColor = AlertRed
                    )
                }
            }

            // Error Banner
            if (hasAttemptedSubmit && activityError) {
                item {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = AlertRedLight
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                1.dp,
                                AlertRed.copy(alpha = 0.4f),
                                RoundedCornerShape(12.dp)
                            )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            EcoWiseIcon(
                                iconPainterResourceID = R.drawable.warning,
                                iconContentDescription = "Warning",
                                iconSize = 18.dp,
                                iconColor = NoticeYellow
                            )
                            EcoWiseText(
                                textMsg = "Please describe what you did before submitting.",
                                textSize = 12.sp,
                                textWeight = FontWeight.SemiBold,
                                textColor = AlertRed
                            )
                        }
                    }
                }
            }

            // Activity Text Field
            item {
                EcoWiseTextField(
                    isOutlined = false,
                    textFieldVar = activityText,
                    onVarChange = {
                        activityText = it
                        if (it.trim().isNotBlank()) {
                            activityError = false
                        }
                    },
                    textFieldModifierPadding = 0.dp,
                    textFieldSingleLine = false,
                    textFieldMaxLines = 5,
                    textFieldMinLines = 3,
                    textFieldPlaceholder = {
                        EcoWiseText(
                            textMsg = "Describe your eco activity for $challengeTitle...",
                            textSize = 13.sp,
                            textColor = TextMuted
                        )
                    },
                    textFieldIsError = activityError
                )
            }

            if (activityError) {
                item {
                    EcoWiseText(
                        textMsg = "Please enter information about what you did",
                        textSize = 11.sp,
                        textColor = AlertRed,
                        textWeight = FontWeight.Medium
                    )
                }
            }

            // Photo Upload Section Label
            item {
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(
                    thickness = 2.dp,
                    color = Divider,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(20.dp))
                EcoWiseText(
                    textMsg = "Add Photo (Optional)",
                    textSize = 14.sp,
                    textColor = TextSecondary
                )
            }

            // Choose Image Button
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = CardBackground,
                            contentColor = Green_Primary
                        ),
                        elevation = CardDefaults.cardElevation(2.dp),
                        modifier = Modifier
                            .clickable {
                                // to access phone gallery
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )
                            }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            EcoWiseIcon(
                                iconPainterResourceID = R.drawable.lightbulb,
                                iconContentDescription = "Photo",
                                iconSize = 16.dp,
                                iconColor = Green_Primary
                            )
                            EcoWiseText(
                                textMsg = if (selectedImageUri != null) "Change Image" else "Choose Image",
                                textSize = 12.sp,
                                textWeight = FontWeight.Medium,
                                textColor = Green_Primary
                            )
                        }
                    }

                    if (selectedImageUri != null) {
                        Card(
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Green_Mint_BG
                            ),
                            modifier = Modifier.border(
                                1.dp,
                                CardBorder,
                                RoundedCornerShape(8.dp)
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                EcoWiseIcon(
                                    iconPainterResourceID = R.drawable.check,
                                    iconContentDescription = "Selected",
                                    iconSize = 14.dp,
                                    iconColor = Green_Forest
                                )
                                EcoWiseText(
                                    textMsg = "Photo selected",
                                    textSize = 11.sp,
                                    textWeight = FontWeight.Medium,
                                    textColor = Green_Dark
                                )
                                Box(modifier = Modifier.clickable { selectedImageUri = null }) {
                                    EcoWiseIcon(
                                        iconPainterResourceID = R.drawable.close,
                                        iconContentDescription = "Remove",
                                        iconSize = 14.dp,
                                        iconColor = TextSecondary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Image Preview
            if (selectedImageUri != null) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .border(1.dp, CardBorder, RoundedCornerShape(12.dp))
                    ) {
                        AsyncImage(
                            model = selectedImageUri,
                            contentDescription = "Selected photo preview",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        EcoWiseButton(
                            buttonEnabled = true,
                            buttonSize = 32.dp,
                            buttonColor = Color.Black.copy(alpha = 0.6f),
                            buttonRoundedCornerShapeTopStart = 25,
                            buttonRoundedCornerShapeTopEnd = 25,
                            buttonRoundedCornerShapeBottomStart = 25,
                            buttonRoundedCornerShapeBottomEnd = 25,
                            iconPainterResourceID = R.drawable.close,
                            iconContentDescription = "Remove",
                            iconSize = 16.dp,
                            iconColor = Color.White,
                            onClickChange = { selectedImageUri = null },
                            modifierPadding = 8.dp
                        )
                    }
                }
            }

            // Submit Button
            item {
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(
                    thickness = 2.dp,
                    color = Divider,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    // get current context
                    val currentToastContextScreen = LocalContext.current
                    Button(
                        onClick = {
                            if (isSubmitting) return@Button
                            hasAttemptedSubmit = true
                            if (activityText.trim().isBlank()) {
                                activityError = true
                            } else {
                                activityError = false
                                isSubmitting = true

                                // save activity and its info (photo)
                                val challengeId = challenge?.id ?: "ch-1"
                                scope.launch {
                                    try {
                                        val errorMsg = EcoWiseRepository.submitChallengeActivity(
                                            challengeId = challengeId,
                                            activityText = activityText.trim(),
                                            hasPhoto = selectedImageUri != null
                                        )

                                        if (errorMsg == null) {
                                            // Toast to show alert msg
                                            Toast.makeText(
                                                currentToastContextScreen,
                                                "Activity submitted successfully!!!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            navController.popBackStack()
                                        } else {
                                            Toast.makeText(
                                                currentToastContextScreen,
                                                errorMsg,
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    } finally {
                                        isSubmitting = false
                                    }
                                }
                            }
                        },
                        enabled = !isSubmitting,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSubmitting) Color.Gray else Green_Primary,
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                    ) {
                        EcoWiseText(
                            textMsg = if (isSubmitting) "Submitting..." else "Submit",
                            textSize = 16.sp,
                            textWeight = FontWeight.Bold,
                            textColor = Color.White
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun PreviewOnChallengeProgressScreen(){
    ChallengeProgressScreen(
        navController = rememberNavController()
    )
}