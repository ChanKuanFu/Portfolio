package com.example.EcoWise.module_2_RecyclingGuide

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.EcoWise.ComposableFunction_self_defined.EcoWiseButton
import com.example.EcoWise.ComposableFunction_self_defined.EcoWiseIcon
import com.example.EcoWise.ComposableFunction_self_defined.EcoWiseText
import com.example.EcoWise.components.EcoWiseMainScaffold
import com.example.EcoWise.data.EcoWiseRepository
import com.example.EcoWise.ui.theme.*
import com.example.EcoWise.R


@Composable
fun RecyclingGuideDetailScreen(
    navController: NavController,
    categoryId: String,
    modifier: Modifier = Modifier
) {
    val currentRoute = navController.currentDestination?.route

    // get all guides from EcoWiseRepository
    val repositoryGuides = EcoWiseRepository.recyclingGuides
    val selectedRepoGuide = repositoryGuides.find { it.id == categoryId } ?: repositoryGuides.first()

    val (iconRes, color) = when (selectedRepoGuide.id) {
        "plastic" -> Pair(R.drawable.recycle, PlasticText)
        "paper" -> Pair(R.drawable.paper, PaperText)
        "glass" -> Pair(R.drawable.glass, Green_Forest)
        "metal" -> Pair(R.drawable.metal, MetalText)
        "organic" -> Pair(R.drawable.organic, Green_Bright)
        else -> Pair(R.drawable.electronic, PurpleText)
    }

    EcoWiseMainScaffold(
        navController = navController,
        currentRoute = currentRoute,
        title = "${selectedRepoGuide.title} Guide",
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
            item {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, CardBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Header with Category Icon & Title
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(color.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                EcoWiseIcon(
                                    iconPainterResourceID = iconRes,
                                    iconContentDescription = null,
                                    iconColor = color,
                                    iconSize = 22.dp
                                )
                            }
                            EcoWiseText(
                                textMsg = "${selectedRepoGuide.title} Recycling",
                                textSize = 18.sp,
                                textWeight = FontWeight.Bold,
                                textColor = TextPrimary
                            )
                        }

                        EcoWiseText(
                            textMsg = selectedRepoGuide.fullInstructions,
                            textSize = 13.sp,
                            textColor = TextSecondary
                        )

                        // ACCEPTED ITEMS Section
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            EcoWiseText(
                                textMsg = "ACCEPTED ITEMS:",
                                textSize = 11.sp,
                                textWeight = FontWeight.Bold,
                                textColor = TextMuted
                            )

                            selectedRepoGuide.acceptedItems.forEach { item ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(20.dp)
                                            .clip(CircleShape)
                                            .background(Green_Mint_BG),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        EcoWiseIcon(
                                            iconPainterResourceID = R.drawable.check,
                                            iconContentDescription = null,
                                            iconColor = Green_Forest,
                                            iconSize = 13.dp
                                        )
                                    }
                                    EcoWiseText(
                                        textMsg = item,
                                        textSize = 13.sp,
                                        textColor = TextPrimary
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Fill-in Product Information Button -- Back to Product Analysis Screen
                        EcoWiseButton(
                            buttonEnabled = true,
                            buttonColor = Green_Forest,
                            buttonSize = 48.dp,
                            buttonIsFillMaxWidth = true,
                            text = "Fill-in Product Information",
                            textSize = 14.sp,
                            textWeight = FontWeight.Bold,
                            textColor = Color.White,
                            iconPainterResourceID = R.drawable.filled_in_information,
                            iconContentDescription = null,
                            iconSize = 16.dp,
                            iconColor = Color.White,
                            onClickChange = {
                                navController.navigate("EcoWise Scanner/${selectedRepoGuide.title}")
                            },
                            modifierPadding = 0.dp
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun PreviewOnRecyclingGuideDetailScreen() {
    RecyclingGuideDetailScreen(
        navController = rememberNavController(),
        categoryId = "plastic"
    )
}