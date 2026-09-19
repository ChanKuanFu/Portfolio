package com.example.EcoWise.module_2_RecyclingGuide

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.EcoWise.ComposableFunction_self_defined.EcoWiseIcon
import com.example.EcoWise.ComposableFunction_self_defined.EcoWiseText
import com.example.EcoWise.components.EcoWiseMainScaffold
import com.example.EcoWise.ui.theme.*
import com.example.EcoWise.R


@Composable
fun EcoWiseRecyclingActivityTurnin(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val currentRoute = navController.currentDestination?.route

    EcoWiseMainScaffold(
        navController = navController,
        currentRoute = currentRoute,
        title = "Turn-in Success",
        showBackButton = false,
        onBackClick = {}
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Green_Mint_BG)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(Green_Ultra_Light),
                contentAlignment = Alignment.Center
            ) {
                EcoWiseIcon(
                    iconPainterResourceID = R.drawable.successfully,
                    iconContentDescription = "Success",
                    iconSize = 48.dp,
                    iconColor = Green_Forest
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            EcoWiseText(
                textMsg = "Activity Turned In Successfully!",
                textSize = 20.sp,
                textWeight = FontWeight.Bold,
                textColor = TextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            EcoWiseText(
                textMsg = "Thank you for contributing to a greener planet. Your recycling action has been recorded.",
                textSize = 14.sp,
                textColor = TextSecondary
            )

            Spacer(modifier = Modifier.height(48.dp))

            Button(
                onClick = {
                    navController.navigate("EcoWise Home Screen/User") {
                        popUpTo("EcoWise Home Screen/{userName}") { inclusive = false }
                    }
                },
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Green_Primary,
                    contentColor = SecondaryActionButton
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                EcoWiseText(
                    textMsg = "Back to Home",
                    textSize = 14.sp,
                    textWeight = FontWeight.Bold,
                    textColor = SecondaryActionButton
                )
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true, showBackground = true)
fun PreviewEcoWiseRecyclingActivityTurnIn() {
    EcoWiseRecyclingActivityTurnin(
        navController = rememberNavController()
    )
}