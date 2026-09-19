package com.example.EcoWise.homescreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.EcoWise.R
import com.example.EcoWise.components.EcoCircularGauge
import com.example.EcoWise.components.EcoWiseMainScaffold
import com.example.EcoWise.components.ProductVisualCard
import com.example.EcoWise.data.EcoWiseRepository
import com.example.EcoWise.model.ProductItem
import com.example.EcoWise.ui.theme.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    currentUserName: String    // get username from NavHost from login page
) {
    // get user stats
    val stats by EcoWiseRepository.userStats.collectAsState()
    // get recent analyzed items
    val recentList by EcoWiseRepository.recentAnalyzed.collectAsState()
    val currentRoute = navController.currentDestination?.route

    EcoWiseMainScaffold(
        navController = navController,
        currentRoute = currentRoute,
        title = "Eco Challenges",
        showBackButton = currentRoute != "EcoWise Home Screen" // show back button except home screen
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Green_Mint_BG),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header -- Greeting & Profile picture -> Route: user_profile (6)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        EcoWiseText(
                            textMsg = "Good morning,",
                            textSize = 24.sp,
                            textWeight = FontWeight.Bold,
                            textColor = Green_Primary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        EcoWiseText(
                            textMsg = currentUserName,
                            textSize = 15.sp,
                            textWeight = FontWeight.Medium,
                            textColor = Green_Forest
                        )
                    }

                    // user profile icon
                    EcoWiseButton(
                        buttonEnabled = true,
                        buttonSize = 48.dp,
                        buttonColor = Green_Forest,
                        buttonElevation = 2.dp,
                        iconPainterResourceID = R.drawable.outlined_user_profile,
                        iconContentDescription = "User Profile",
                        iconSize = 24.dp,
                        iconColor = Green_Mint_BG,
                        onClickChange = {
                            navController.navigate("EcoWise User Profile")
                        },
                        modifierPadding = 0.dp
                    )
                }
            }

            //   challenge (3)
            item {
                Card(
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurfaceVariant),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate("EcoWise Challenge") }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = Green_Forest
                            ) {
                                EcoWiseText(
                                    textMsg = "TODAY'S FOCUS",
                                    textSize = 11.sp,
                                    textWeight = FontWeight.Bold,
                                    textColor = Color.White,
                                    textPadding = 4.dp
                                )
                            }

                            Surface(
                                shape = CircleShape,
                                color = Color.White.copy(alpha = 0.6f),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    EcoWiseIcon(
                                        iconPainterResourceID = R.drawable.eco,
                                        iconContentDescription = null,
                                        iconSize = 20.dp,
                                        iconColor = Green_Forest
                                    )
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 12.dp)
                            ) {
                                EcoWiseText(
                                    textMsg = "Sprint: Zero Waste Week",
                                    textSize = 20.sp,
                                    textWeight = FontWeight.Bold,
                                    textColor = Green_Dark
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                EcoWiseText(
                                    textMsg = "75% of sustainability goal reached • 3/4 tasks",
                                    textSize = 13.sp,
                                    textWeight = FontWeight.Medium,
                                    textColor = Green_Bright
                                )
                            }

                            EcoCircularGauge(
                                percentage = 0.75f,
                                displayText = "75%",
                                sizeDp = 76.dp,
                                strokeWidth = 8.dp,
                                gaugeColor = Green_Forest,
                                trackColor = Color.White.copy(alpha = 0.7f)
                            )
                        }
                    }
                }
            }

            // Quick Action Grid -> analyze (1) & guide (2)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, CardBorder),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                navController.navigate("EcoWise Scanner")
                            }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Green_Mint_BG),
                                contentAlignment = Alignment.Center
                            ) {
                                EcoWiseIcon(
                                    iconPainterResourceID = R.drawable.filled_analyse,
                                    iconContentDescription = "Scan & Analyse",
                                    iconSize = 24.dp,
                                    iconColor = Green_Forest
                                )
                            }
                            EcoWiseText(
                                textMsg = "Analyse Item",
                                textSize = 14.sp,
                                textWeight = FontWeight.Bold,
                                textColor = TextPrimary
                            )
                        }
                    }

                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, CardBorder),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                navController.navigate("EcoWise Recycling Guide")
                            }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Green_Mint_BG),
                                contentAlignment = Alignment.Center
                            ) {
                                EcoWiseIcon(
                                    iconPainterResourceID = R.drawable.filled_guide,
                                    iconContentDescription = "Recycling Guide",
                                    iconSize = 24.dp,
                                    iconColor = Green_Medium
                                )
                            }
                            EcoWiseText(
                                textMsg = "Recycle Guide",
                                textSize = 14.sp,
                                textWeight = FontWeight.Bold,
                                textColor = TextPrimary
                            )
                        }
                    }
                }
            }

            // Active Challenge Card -> Route: challenge (3)
            item {
                Card(
                    shape = RoundedCornerShape(26.dp),
                    colors = CardDefaults.cardColors(containerColor = Green_Deep_Navy),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate("EcoWise Challenge") }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            EcoWiseText(
                                textMsg = "ACTIVE CHALLENGE",
                                textSize = 11.sp,
                                textWeight = FontWeight.Bold,
                                textColor = Color.White.copy(alpha = 0.7f)
                            )

                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.White.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                EcoWiseIcon(
                                    iconPainterResourceID = R.drawable.outlined_challenge,
                                    iconContentDescription = "Challenge",
                                    iconSize = 18.dp,
                                    iconColor = Color.White
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 12.dp)
                            ) {
                                EcoWiseText(
                                    textMsg = "Zero Waste Week",
                                    textSize = 18.sp,
                                    textWeight = FontWeight.Bold,
                                    textColor = Color.White
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                EcoWiseText(
                                    textMsg = "3/4 tasks completed • 75 points",
                                    textSize = 12.sp,
                                    textColor = Color.White.copy(alpha = 0.8f)
                                )
                            }

                            EcoCircularGauge(
                                percentage = 0.75f,
                                displayText = "75%",
                                sizeDp = 64.dp,
                                strokeWidth = 6.dp,
                                gaugeColor = Green_Bright,
                                trackColor = Color.White.copy(alpha = 0.2f)
                            )
                        }
                    }
                }
            }

            item {
                EcoWiseButton(
                    buttonEnabled = true,
                    buttonColor = StarYellow,
                    buttonSize = 45.dp,
                    buttonIsFillMaxWidth = true,
                    text = "Get More Challenge",
                    textSize = 15.sp,
                    textWeight = FontWeight.Bold,
                    textColor = DeepBlack,
                    iconPainterResourceID = R.drawable.arrowforward,
                    iconPosition = "End",
                    iconColor = DeepBlack,
                    iconSize = 18.dp,
                    onClickChange = {
                        navController.navigate("EcoWise Challenge")
                    },
                    modifierPadding = 0.dp
                )
            }

            // Eco Points Card -> Route: rewards_shop (5)
            item {
                Card(
                    shape = RoundedCornerShape(26.dp),
                    colors = CardDefaults.cardColors(containerColor = Green_Deep_Navy),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                EcoWiseText(
                                    textMsg = "ECO REWARD BALANCE",
                                    textSize = 11.sp,
                                    textWeight = FontWeight.Bold,
                                    textColor = Color.White.copy(alpha = 0.7f)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                EcoWiseText(
                                    textMsg = "${stats.ecoPoints} Points",
                                    textSize = 24.sp,
                                    textWeight = FontWeight.Bold,
                                    textColor = Color.White
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                EcoWiseIcon(
                                    iconPainterResourceID = R.drawable.filled_star,
                                    iconContentDescription = "Points star",
                                    iconSize = 24.dp,
                                    iconColor = StarYellow
                                )
                            }
                        }
                        Button(
                            onClick = { navController.navigate("EcoWise Rewards Shop") },
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White, // 强制纯白背景
                                contentColor = Green_Deep_Navy
                            ),
                            elevation = ButtonDefaults.buttonElevation(0.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                        ) {
                            EcoWiseText(
                                textMsg = "Redeem Rewards",
                                textSize = 14.sp,
                                textWeight = FontWeight.Bold,
                                textColor = Green_Deep_Navy
                            )
                        }
                    }
                }
            }

            // Daily Tip Card -> Route: about_us (7)
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, CardBorder),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate("About Us") }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(AmberBg),
                            contentAlignment = Alignment.Center
                        ) {
                            EcoWiseIcon(
                                iconPainterResourceID = R.drawable.lightbulb,
                                iconContentDescription = "Tip",
                                iconSize = 22.dp,
                                iconColor = AmberText
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            EcoWiseText(
                                textMsg = "Daily Eco Tip",
                                textSize = 14.sp,
                                textWeight = FontWeight.Bold,
                                textColor = TextPrimary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            EcoWiseText(
                                textMsg = "Use a reusable bottle to save 150 points today!",
                                textSize = 12.sp,
                                textColor = LightHeader
                            )
                        }
                    }
                }
            }

            // Recent Analyze Section Header -> Route: analyze_history (4)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    EcoWiseText(
                        textMsg = "Recent Analyzed Items",
                        textSize = 18.sp,
                        textWeight = FontWeight.Bold,
                        textColor = TextPrimary
                    )

                    Box(
                        modifier = Modifier
                            .clickable { navController.navigate("Analyse History") }
                            .padding(4.dp)
                    ) {
                        EcoWiseText(
                            textMsg = "View All",
                            textSize = 14.sp,
                            textWeight = FontWeight.Bold,
                            textColor = Green_Forest
                        )
                    }
                }
            }

            // Recent Analyzed Items Horizontal Row using explicit items(items = ..., key = ...) syntax
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    modifier = Modifier.fillMaxWidth().height(210.dp)
                ) {
                    items(
                        items = recentList,
                        key = { product -> product.id }
                    ) { product ->
                        RecentProductCard(
                            product = product,
                            onClick = {
                                navController.navigate("Product Analysis Result/${product.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RecentProductCard(
    product: ProductItem,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, CardBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .width(165.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFF8FAFC))
            ) {
                ProductVisualCard(
                    visualType = product.visualType,
                    modifier = Modifier.fillMaxSize()
                )

                if (product.ecoScore >= 50) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(6.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Green_Mint_BG)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        EcoWiseIcon(
                            iconPainterResourceID = R.drawable.ecoscore,
                            iconContentDescription = "Eco Score",
                            iconSize = 14.dp,
                            iconColor = Green_Bright
                        )
                        EcoWiseText(
                            textMsg = "${product.ecoScore}",
                            textSize = 11.sp,
                            textWeight = FontWeight.Bold,
                            textColor = Green_Bright
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(6.dp)
                            .size(22.dp)
                            .clip(CircleShape)
                            .background(AlertRedLight),
                        contentAlignment = Alignment.Center
                    ) {
                        EcoWiseIcon(
                            iconPainterResourceID = R.drawable.warning,
                            iconContentDescription = "Alert",
                            iconSize = 14.dp,
                            iconColor = AlertRed
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            EcoWiseText(
                textMsg = product.name,
                textSize = 14.sp,
                textWeight = FontWeight.Bold,
                textColor = TextPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            EcoWiseText(
                textMsg = product.category,
                textSize = 11.sp,
                textColor = LightHeader
            )
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun PreviewOn(){
    val navController = rememberNavController()
    HomeScreen(
        navController = navController,
        modifier = Modifier,
        currentUserName = "EcoWise User"
    )
}


