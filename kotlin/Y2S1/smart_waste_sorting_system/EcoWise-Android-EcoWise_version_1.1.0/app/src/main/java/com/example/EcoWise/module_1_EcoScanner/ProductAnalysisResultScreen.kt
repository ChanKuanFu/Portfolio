package com.example.EcoWise.module_1_EcoScanner

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.EcoWise.ComposableFunction_self_defined.EcoWiseButton
import com.example.EcoWise.ComposableFunction_self_defined.EcoWiseIcon
import com.example.EcoWise.ComposableFunction_self_defined.EcoWiseText
import com.example.EcoWise.RoomDB.EcoWiseViewModel
import com.example.EcoWise.components.EcoCircularGauge
import com.example.EcoWise.components.EcoWiseMainScaffold
import com.example.EcoWise.components.ProductVisualCard
import com.example.EcoWise.model.CarbonLevel
import com.example.EcoWise.model.ProductItem
import com.example.EcoWise.model.RecyclabilityLevel
import com.example.EcoWise.ui.theme.*
import com.example.EcoWise.data.EcoWiseRepository
import com.example.EcoWise.R

@Composable
fun ProductAnalysisResultScreen(
    navController: NavController,
    productId: String?,
    modifier: Modifier = Modifier,
    viewModel: EcoWiseViewModel = viewModel()
) {
    val currentRoute = navController.currentDestination?.route
    var product by remember { mutableStateOf<ProductItem?>(null) }

    LaunchedEffect(productId) {
        if (productId != null) {
            product = viewModel.getProductItem(productId)
        }
    }

    val displayProduct = product ?: ProductItem(
        id = productId ?: "default",
        name = "Loading...",
        brand = "EcoWise",
        category = "General",
        weightSize = "500g",
        packagingMaterial = "Plastic",
        ecoScore = 75,
        carbonFootprint = CarbonLevel.LOW,
        recyclability = RecyclabilityLevel.HIGH,
        visualType = "water_bottle"
    )

    val score = displayProduct.ecoScore
    val scoreColor = when {
        score >= 75 -> Green_Forest
        score >= 45 -> AmberText
        else -> AlertRed
    }

    EcoWiseMainScaffold(
        navController = navController,
        currentRoute = currentRoute,
        title = "Product Analysis",
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
            // Product Header Card
            item {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ProductVisualCard(
                            visualType = displayProduct.visualType,
                            modifier = Modifier.size(68.dp)
                        )

                        Column(modifier = Modifier.weight(1f)) {
                            EcoWiseText(
                                textMsg = displayProduct.name,
                                textSize = 17.sp,
                                textWeight = FontWeight.Bold,
                                textColor = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            EcoWiseText(
                                textMsg = listOfNotNull(
                                    displayProduct.brand.ifBlank { null },
                                    displayProduct.packagingMaterial.ifBlank { null },
                                    displayProduct.weightSize.ifBlank { null }
                                ).joinToString(" • "),
                                textSize = 12.sp,
                                textColor = TextSecondary
                            )

                            if (displayProduct.ecoLabels.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    displayProduct.ecoLabels.take(2).forEach { label ->
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = Green_Ultra_Light
                                        ) {
                                            EcoWiseText(
                                                textMsg = "🌿 $label",
                                                textSize = 10.sp,
                                                textWeight = FontWeight.SemiBold,
                                                textColor = Green_Forest,
                                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Eco Score Card with Gauge & Metrics
            item {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBackground),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            EcoCircularGauge(
                                percentage = score / 100f,
                                displayText = "$score%",
                                sizeDp = 90.dp,
                                strokeWidth = 8.dp,
                                gaugeColor = scoreColor,
                                trackColor = Color.White.copy(alpha = 0.2f)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(color = Green_Mint_BG)
                        Spacer(modifier = Modifier.height(14.dp))

                        // Carbon Footprint & Recyclability
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                EcoWiseIcon(
                                    iconPainterResourceID = R.drawable.carbon,
                                    iconContentDescription = "Carbon Footprint",
                                    iconSize = 14.dp,
                                    iconColor = Green_Forest
                                )
                                EcoWiseText(
                                    textMsg = "Carbon Footprint",
                                    textSize = 14.sp,
                                    textColor = TextPrimary
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = when (displayProduct.carbonFootprint) {
                                    CarbonLevel.LOW -> Green_Mint_BG
                                    CarbonLevel.MEDIUM -> NoticeYellow.copy(alpha = 0.3f)
                                    CarbonLevel.HIGH -> AlertRedLight
                                }
                            ) {
                                EcoWiseText(
                                    textMsg = displayProduct.carbonFootprint.name.lowercase().replaceFirstChar { it.uppercase() },
                                    textSize = 12.sp,
                                    textWeight = FontWeight.Bold,
                                    textColor = when (displayProduct.carbonFootprint) {
                                        CarbonLevel.LOW -> Green_Forest
                                        CarbonLevel.MEDIUM -> AmberText
                                        CarbonLevel.HIGH -> AlertRed
                                    },
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                EcoWiseIcon(
                                    iconPainterResourceID = R.drawable.recycle,
                                    iconContentDescription = "Recyclability",
                                    iconSize = 14.dp,
                                    iconColor = Green_Forest
                                )
                                EcoWiseText(
                                    textMsg = "Recyclability   ",
                                    textSize = 14.sp,
                                    textColor = TextPrimary
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = when (displayProduct.recyclability) {
                                    RecyclabilityLevel.HIGH -> Green_Mint_BG
                                    RecyclabilityLevel.MEDIUM -> NoticeYellow.copy(alpha = 0.3f)
                                    RecyclabilityLevel.LOW -> AlertRedLight
                                }
                            ) {
                                EcoWiseText(
                                    textMsg = displayProduct.recyclability.name.lowercase().replaceFirstChar { it.uppercase() },
                                    textSize = 12.sp,
                                    textWeight = FontWeight.Bold,
                                    textColor = when (displayProduct.recyclability) {
                                        RecyclabilityLevel.HIGH -> Green_Forest
                                        RecyclabilityLevel.MEDIUM -> AmberText
                                        RecyclabilityLevel.LOW -> AlertRed
                                    },
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Impact Breakdown Header
            item {
                EcoWiseText(
                    textMsg = "Impact Breakdown",
                    textSize = 18.sp,
                    textWeight = FontWeight.Bold,
                    textColor = TextPrimary
                )
            }

            // Impact Details List
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    ImpactDetailItem(
                        icon = R.drawable.eco,
                        label = "Carbon Footprint",
                        value = "${displayProduct.packagingMaterial} material impact",
                        color = Green_Forest
                    )
                    ImpactDetailItem(
                        icon = R.drawable.eco,
                        label = "Material Type",
                        value = displayProduct.packagingMaterial,
                        color = Green_Forest
                    )
                    ImpactDetailItem(
                        icon = R.drawable.eco,
                        label = "Category",
                        value = displayProduct.category,
                        color = AmberText
                    )
                }
            }

            // Recycling Guide Card
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        EcoWiseText(
                            textMsg = "How to Recycle",
                            textSize = 15.sp,
                            textWeight = FontWeight.Bold,
                            textColor = TextPrimary
                        )

                        Row(
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(PlasticBg),
                                contentAlignment = Alignment.Center
                            ) {
                                EcoWiseIcon(
                                    iconPainterResourceID = R.drawable.recycle,
                                    iconContentDescription = "Recycle",
                                    iconSize = 18.dp,
                                    iconColor = PlasticText
                                )
                            }

                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                EcoWiseText(
                                    textMsg = "${displayProduct.packagingMaterial} Recycling Bin",
                                    textSize = 13.sp,
                                    textWeight = FontWeight.Bold,
                                    textColor = TextPrimary
                                )
                                EcoWiseText(
                                    textMsg = displayProduct.recyclingGuide.instructions,
                                    textSize = 12.sp,
                                    textColor = TextSecondary
                                )
                            }
                        }
                    }
                }
            }

            // Action Buttons (Drop-offs & Favorites & History)
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Find Nearby Drop-offs Button
                    EcoWiseButton(
                        buttonEnabled = true,
                        buttonColor = Green_Forest,
                        buttonIsFillMaxWidth = true,
                        buttonElevation = 2.dp,
                        buttonRoundedCornerShapeTopStart = 14,
                        buttonRoundedCornerShapeTopEnd = 14,
                        buttonRoundedCornerShapeBottomStart = 14,
                        buttonRoundedCornerShapeBottomEnd = 14,
                        text = "Find Nearby Drop-offs",
                        textSize = 14.sp,
                        textWeight = FontWeight.Bold,
                        textColor = Color.White,
                        iconPainterResourceID = R.drawable.location,
                        iconSize = 18.dp,
                        iconColor = Color.White,
                        onClickChange = {
                            navController.navigate("EcoWise Recycling View Drop-offs Center Maps")
                        },
                        modifierPadding = 0.dp
                    )

                    // Save to Favorites Button
                    Button(
                        onClick = {
                            EcoWiseRepository.moveToFavorites(setOf(displayProduct.id))
                            navController.navigate("Favorite List")
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Green_Bright,
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        EcoWiseText(
                            textMsg = "Save to Favorites",
                            textSize = 14.sp,
                            textWeight = FontWeight.Bold,
                            textColor = Color.White
                        )
                    }

                    // View History
                    Button(
                        onClick = {
                            navController.navigate("Analyse History")
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Green_Mint_BG,
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        EcoWiseText(
                            textMsg = "View Recent History",
                            textSize = 14.sp,
                            textWeight = FontWeight.Bold,
                            textColor = Green_Bright
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
fun ImpactDetailItem(
    icon: Int,
    label: String,
    value: String,
    color: Color
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                EcoWiseIcon(
                    iconPainterResourceID = icon,
                    iconContentDescription = label,
                    iconSize = 20.dp,
                    iconColor = color
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                EcoWiseText(
                    textMsg = label,
                    textSize = 12.sp,
                    textColor = TextSecondary
                )
                EcoWiseText(
                    textMsg = value,
                    textSize = 14.sp,
                    textWeight = FontWeight.Bold,
                    textColor = TextPrimary
                )
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun PreviewOnProductAnalysisResultScreen() {
    ProductAnalysisResultScreen(
        navController = rememberNavController(),
        productId = "product_1"
    )
}