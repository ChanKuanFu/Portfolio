package com.example.EcoWise.module_1_EcoScanner

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.EcoWise.ComposableFunction_self_defined.EcoWiseButton
import com.example.EcoWise.ComposableFunction_self_defined.EcoWiseIcon
import com.example.EcoWise.ComposableFunction_self_defined.EcoWiseText
import com.example.EcoWise.R
import com.example.EcoWise.RoomDB.EcoWiseViewModel
import com.example.EcoWise.components.EcoWiseMainScaffold
import com.example.EcoWise.ui.theme.*


@Composable
fun AnalyseHistoryScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: EcoWiseViewModel = viewModel()
) {
    val currentRoute = navController.currentDestination?.route
    var isEditMode by remember { mutableStateOf(false) }

    val historyItems by viewModel.allProducts.collectAsStateWithLifecycle()
    var selectedItems by remember { mutableStateOf(setOf<String>()) }

    LaunchedEffect(Unit) {
        viewModel.syncWithCloud()
    }

    EcoWiseMainScaffold(
        navController = navController,
        currentRoute = currentRoute,
        title = "Analysis History",
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        EcoWiseText(
                            textMsg = "${historyItems.size} items analyzed",
                            textSize = 13.sp,
                            textColor = TextSecondary
                        )
                    }

                    EcoWiseButton(
                        buttonEnabled = true,
                        buttonSize = 40.dp,
                        buttonRoundedCornerShapeTopStart = 25,
                        buttonRoundedCornerShapeTopEnd = 25,
                        buttonRoundedCornerShapeBottomStart = 25,
                        buttonRoundedCornerShapeBottomEnd = 25,
                        buttonElevation = 0.dp,
                        buttonColor = if (isEditMode) Green_Forest else Color.White,
                        buttonBorderWidth = if (isEditMode) 0.dp else 1.dp,
                        buttonBorderColor = if (isEditMode) CardBorder else Color.Transparent,
                        buttonContentPadding = 0.dp,
                        text = if (isEditMode) "Done" else "Edit",
                        textSize = 12.sp,
                        textWeight = FontWeight.Bold,
                        textColor = if (isEditMode) Color.White else TextPrimary,
                        onClickChange = { isEditMode = !isEditMode },
                        modifierPadding = 0.dp
                    )
                }
            }

            if (isEditMode && selectedItems.isNotEmpty()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = {
                                selectedItems.forEach { id ->
                                    val item = historyItems.find { it.id == id }
                                    if (item != null) viewModel.toggleFavorite(item)
                                }
                                selectedItems = emptySet()
                                isEditMode = false
                            },
                            enabled = true,
                            colors = ButtonDefaults.buttonColors(containerColor = Green_Forest),
                            shape = RoundedCornerShape(25.dp),
                            elevation = ButtonDefaults.buttonElevation(0.dp),
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier.size(height = 44.dp, width = 140.dp)
                        ) {
                            EcoWiseText(
                                textMsg = "Add to Favorites",
                                textSize = 12.sp,
                                textWeight = FontWeight.Bold,
                                textColor = Color.White
                            )
                        }

                        Button(
                            onClick = {
                                selectedItems.forEach { id ->
                                    val item = historyItems.find { it.id == id }
                                    if (item != null) viewModel.deleteProduct(item)
                                }
                                selectedItems = emptySet()
                                isEditMode = false
                            },
                            enabled = true,
                            colors = ButtonDefaults.buttonColors(containerColor = AlertRed),
                            shape = RoundedCornerShape(25.dp),
                            elevation = ButtonDefaults.buttonElevation(0.dp),
                            contentPadding = PaddingValues(0.dp),
                            modifier = Modifier.size(height = 44.dp, width = 100.dp)
                        ) {
                            EcoWiseText(
                                textMsg = "Delete",
                                textSize = 12.sp,
                                textWeight = FontWeight.Bold,
                                textColor = Color.White
                            )
                        }
                    }
                }
            }

            items(historyItems, key = { it.id }) { product ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isEditMode && selectedItems.contains(product.id))
                            Green_Ultra_Light else Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            if (isEditMode) {
                                selectedItems = if (selectedItems.contains(product.id)) {
                                    selectedItems - product.id
                                } else {
                                    selectedItems + product.id
                                }
                            } else {
                                navController.navigate("Product Analysis Result/${product.id}")
                            }
                        }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (isEditMode) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(
                                        if (selectedItems.contains(product.id))
                                            Green_Forest else Color.White
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (selectedItems.contains(product.id)) {
                                    EcoWiseIcon(
                                        iconPainterResourceID = R.drawable.check,
                                        iconContentDescription = "Selected",
                                        iconSize = 14.dp,
                                        iconColor = Color.White
                                    )
                                }
                            }
                        }

                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Green_Mint_BG),
                            contentAlignment = Alignment.Center
                        ) {
                            EcoWiseIcon(
                                iconPainterResourceID = R.drawable.eco,
                                iconContentDescription = "Product",
                                iconSize = 24.dp,
                                iconColor = Green_Forest
                            )
                        }

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            EcoWiseText(
                                textMsg = product.name,
                                textSize = 14.sp,
                                textWeight = FontWeight.Bold,
                                textColor = TextPrimary
                            )
                            EcoWiseText(
                                textMsg = "${product.brand} • ${product.category}",
                                textSize = 11.sp,
                                textColor = TextSecondary
                            )
                        }

                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Green_Mint_BG),
                            contentAlignment = Alignment.Center
                        ) {
                            EcoWiseText(
                                textMsg = "${product.ecoScore}",
                                textSize = 14.sp,
                                textWeight = FontWeight.Bold,
                                textColor = Green_Forest
                            )
                        }
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
fun PreviewOnAnalyseHistoryScreen(){
    AnalyseHistoryScreen(
        navController = rememberNavController(),
        modifier = Modifier
    )
}