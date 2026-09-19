package com.example.EcoWise.module_1_EcoScanner

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.EcoWise.ComposableFunction_self_defined.EcoWiseIcon
import com.example.EcoWise.ComposableFunction_self_defined.EcoWiseText
import com.example.EcoWise.RoomDB.EcoWiseViewModel
import com.example.EcoWise.components.EcoWiseMainScaffold
import com.example.EcoWise.ui.theme.*
import com.example.EcoWise.R


@Composable
fun FavouriteListScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: EcoWiseViewModel = viewModel()
) {
    val currentRoute = navController.currentDestination?.route
    val favoriteItems by viewModel.favoriteProducts.collectAsStateWithLifecycle()

    EcoWiseMainScaffold(
        navController = navController,
        currentRoute = currentRoute,
        title = "Favourite List",
        showBackButton = true,
        onBackClick = { navController.popBackStack() }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Green_Mint_BG),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (favoriteItems.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        EcoWiseText(
                            textMsg = "No favorite items yet.",
                            textSize = 14.sp,
                            textColor = TextSecondary
                        )
                    }
                }
            } else {
                items(favoriteItems, key = { it.id }) { item ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate("Product Analysis Result/${item.id}")
                            }
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
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                EcoWiseText(
                                    textMsg = item.name,
                                    textSize = 13.sp,
                                    textWeight = FontWeight.Bold,
                                    textColor = TextPrimary
                                )
                                EcoWiseText(
                                    textMsg = "${item.brand} • ${item.category}",
                                    textSize = 11.sp,
                                    textColor = TextSecondary
                                )
                            }

                            IconButton(
                                onClick = {
                                    viewModel.toggleFavorite(item)
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Green_Mint_BG),
                                    contentAlignment = Alignment.Center
                                ) {
                                    EcoWiseIcon(
                                        iconPainterResourceID = R.drawable.filled_star,
                                        iconContentDescription = "Remove Favorite",
                                        iconSize = 18.dp,
                                        iconColor = StarYellow
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun PreviewOnFavouriteListScreen(){
    FavouriteListScreen(
        navController = rememberNavController(),
        modifier = Modifier
    )
}