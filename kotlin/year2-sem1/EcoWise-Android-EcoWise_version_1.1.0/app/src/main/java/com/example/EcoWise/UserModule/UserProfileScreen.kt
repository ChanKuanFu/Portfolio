package com.example.EcoWise.UserModule

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.VerticalDivider
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
import com.example.EcoWise.ComposableFunction_self_defined.EcoWiseIcon
import com.example.EcoWise.ComposableFunction_self_defined.EcoWiseText
import com.example.EcoWise.components.EcoWiseMainScaffold
import com.example.EcoWise.data.EcoWiseRepository
import com.example.EcoWise.ui.theme.*
import com.example.EcoWise.R

@Composable
fun UserProfileScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val currentRoute = navController.currentDestination?.route

    // get user stats from EcoWiseRepository
    val userStats by EcoWiseRepository.userStats.collectAsState()
    val currentPoints = userStats.ecoPoints
    val totalProductsAnalysed = userStats.productsAnalysed // times for analyse

    // get user info from EcoWiseRepository
    val userName by EcoWiseRepository.userName.collectAsState()
    val userEmail by EcoWiseRepository.userEmail.collectAsState()

    EcoWiseMainScaffold(
        navController = navController,
        currentRoute = currentRoute,
        title = "Profile",
        showBackButton = false,
        onBackClick = {}
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Green_Mint_BG),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // Header Profile Section
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Green_Dark_BG)
                        .padding(bottom = 40.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, start = 20.dp, end = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // profile picture
                        Box(
                            modifier = Modifier
                                .size(85.dp)
                                .clip(CircleShape)
                                .background(Divider),
                            contentAlignment = Alignment.Center
                        ){
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .background(Green_Forest),
                                contentAlignment = Alignment.Center
                            ) {
                                EcoWiseIcon(
                                    iconPainterResourceID = R.drawable.person,
                                    iconContentDescription = "Avatar",
                                    iconSize = 40.dp,
                                    iconColor = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Name update
                        EcoWiseText(
                            textMsg = userName,
                            textSize = 25.sp,
                            textWeight = FontWeight.Bold,
                            textColor = PrimaryActionButtonText
                        )

                        // Email update
                        EcoWiseText(
                            textMsg = userEmail,
                            textSize = 15.sp,
                            textColor = PrimaryActionButtonText
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }

            // Stats Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = (-20).dp)
                        .padding(horizontal = 16.dp)
                ) {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // times for analyse
                                EcoWiseText(
                                    textMsg = "$totalProductsAnalysed",
                                    textSize = 20.sp,
                                    textWeight = FontWeight.Bold,
                                    textColor = TextPrimary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                EcoWiseText(
                                    textMsg = "Products",
                                    textSize = 12.sp,
                                    textColor = Divider
                                )
                            }
                            Spacer(modifier = Modifier.width(40.dp))
                            VerticalDivider(modifier = Modifier.width(1.dp), color = TextSecondary)
                            Spacer(modifier = Modifier.width(40.dp))

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // point
                                EcoWiseText(
                                    textMsg = "$currentPoints",
                                    textSize = 20.sp,
                                    textWeight = FontWeight.Bold,
                                    textColor = TextPrimary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                EcoWiseText(
                                    textMsg = "Points",
                                    textSize = 12.sp,
                                    textColor = TextSecondary
                                )
                            }
                        }
                    }
                }
            }

            // Menu Items
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .offset(y = (-8).dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // My Statistics
                            MenuItem(
                                icon = R.drawable.barchart,
                                iconBg = Green_Mint_BG,
                                iconTint = Green_Forest,
                                title = "My Statistics",
                                onClick = {
                                    navController.navigate("User Statistics")
                                }
                            )

                            HorizontalDivider(
                                color = Color(0xFFF0F4EF),
                                thickness = 1.dp
                            )

                            // My Rewards
                            MenuItem(
                                icon = R.drawable.filled_rewards_shop,
                                iconBg = Green_Mint_BG,
                                iconTint = Green_Forest,
                                title = "My Rewards",
                                onClick = {
                                    navController.navigate("My Rewards")
                                }
                            )
                        }
                    }
                }
            }

            // Sign Out Button
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .offset(y = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = {
                            navController.navigate("EcoWise Login") {
                                popUpTo(0)
                            }
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AlertRed,
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        EcoWiseText(
                            textMsg = "Sign Out",
                            textSize = 14.sp,
                            textWeight = FontWeight.Bold,
                            textColor = Color.White
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
fun MenuItem(
    icon: Int,
    iconBg: Color,
    iconTint: Color,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                EcoWiseIcon(
                    iconPainterResourceID = icon,
                    iconContentDescription = title,
                    iconSize = 20.dp,
                    iconColor = iconTint
                )
            }

            EcoWiseText(
                textMsg = title,
                textSize = 14.sp,
                textWeight = FontWeight.Medium,
                textColor = TextPrimary
            )
        }

        EcoWiseIcon(
            iconPainterResourceID = R.drawable.arrowforward,
            iconContentDescription = "Navigate",
            iconSize = 20.dp,
            iconColor = TextSecondary
        )
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun PreviewOnUserProfileScreen(){
    UserProfileScreen(
        navController = rememberNavController()
    )
}