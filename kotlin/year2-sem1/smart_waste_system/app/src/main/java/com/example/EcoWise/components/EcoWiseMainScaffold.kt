package com.example.EcoWise.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.EcoWise.ComposableFunction_self_defined.EcoWiseButton
import com.example.EcoWise.ComposableFunction_self_defined.EcoWiseIcon
import com.example.EcoWise.ComposableFunction_self_defined.EcoWiseText
import com.example.EcoWise.R
import com.example.EcoWise.ui.theme.*

data class NavigationBarName(
    val name: String,
    val textSize: TextUnit = 10.sp,
    val outlinedIcon: Int,  // icons not selected
    val filledIcon: Int,    // when selected
    val route: String,
    val textAlignment: TextAlign = TextAlign.Center
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EcoWiseMainScaffold(
    navController: NavController,
    currentRoute: String?,  // get current route
    title: String = "EcoWise",
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = { navController.popBackStack() },
    content: @Composable (PaddingValues) -> Unit    // receive padding values from each screen
) {
    val navigationBarItemList = listOf(
        NavigationBarName(
            name = "Home",
            outlinedIcon = R.drawable.outlined_home_button,
            filledIcon = R.drawable.filled_home_button,
            route = "EcoWise Home Screen"
        ),
        NavigationBarName(
            name = "Analyse",
            outlinedIcon = R.drawable.outlined_analyse,
            filledIcon = R.drawable.filled_analyse,
            route = "EcoWise Scanner"
        ),
        NavigationBarName(
            name = "Guide",
            outlinedIcon = R.drawable.outlined_guide,
            filledIcon = R.drawable.filled_guide,
            route = "EcoWise Recycling Guide"
        ),
        NavigationBarName(
            name = "Challenge",
            outlinedIcon = R.drawable.outlined_challenge,
            filledIcon = R.drawable.filled_challenge,
            route = "EcoWise Challenge"
        ),
        NavigationBarName(
            name = "History",
            outlinedIcon = R.drawable.outlined_analyse_history,
            filledIcon = R.drawable.filled_analyse_history,
            route = "Analyse History"
        ),
        NavigationBarName(
            name = "Rewards",
            outlinedIcon = R.drawable.outlined_rewards_shop,
            filledIcon = R.drawable.filled_rewards_shop,
            route = "EcoWise Rewards Shop"
        ),
        NavigationBarName(
            name = "Profile",
            outlinedIcon = R.drawable.outlined_user_profile,
            filledIcon = R.drawable.filled_user_profile,
            route = "EcoWise User Profile"
        ),
        NavigationBarName(
            name = "About",
            outlinedIcon = R.drawable.outlined_about_us,
            filledIcon = R.drawable.filled_about_us,
            route = "About Us"
        )
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    EcoWiseText(
                        textMsg = title,
                        textSize = 20.sp,
                        textWeight = FontWeight.Bold,
                        textColor = Color.White
                    )
                },
                navigationIcon = {
                    if (showBackButton) {
                        EcoWiseButton(
                            buttonEnabled = true,
                            buttonSize = 40.dp,
                            buttonRoundedCornerShapeTopStart = 25,
                            buttonRoundedCornerShapeTopEnd = 25,
                            buttonRoundedCornerShapeBottomStart = 25,
                            buttonRoundedCornerShapeBottomEnd = 25,
                            buttonElevation = 0.dp,
                            buttonBorderWidth = 0.dp,
                            buttonContentPadding = 0.dp,
                            iconPainterResourceID = R.drawable.arrow_back,
                            iconContentDescription = "Back",
                            iconSize = 25.dp,
                            iconColor = Color.White,
                            onClickChange = { navController.popBackStack() },
                            modifierPadding = 0.dp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Green_Mint)
            )
        },
        bottomBar = {
            Surface(
                color = Green_Mint,
                tonalElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 5.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    itemsIndexed(navigationBarItemList) { currentListIndex, item ->
                        val isSelected = if (item.route == "EcoWise Home Screen") {
                            currentRoute?.startsWith("EcoWise Home Screen") == true
                        } else {
                            currentRoute == item.route
                        }
                        val iconColor = if (isSelected) Green_Mint else Green_Mint_BG
                        val targetIconResource = if (isSelected) item.filledIcon else item.outlinedIcon

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) Green_Ultra_Light else Color.Transparent,
                                modifier = Modifier
                                    .width(60.dp)
                                    .clickable {

                                        val targetRoute = if (item.route == "EcoWise Home Screen") {
                                            "EcoWise Home Screen/User"
                                        } else {
                                            item.route
                                        }

                                        if (currentRoute != targetRoute && currentRoute?.startsWith(item.route) != true) {
                                            navController.navigate(targetRoute) {
                                                popUpTo(navController.graph.startDestinationId) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    }
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier.height(50.dp)
                                ) {
                                    EcoWiseIcon(
                                        iconPainterResourceID = targetIconResource,
                                        iconContentDescription = item.name,
                                        iconColor = iconColor,
                                        iconSize = 30.dp
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    EcoWiseText(
                                        textMsg = item.name,
                                        textSize = 10.sp,
                                        textColor = Color.White
                                    )
                                }
                            }

                            if (currentListIndex < navigationBarItemList.size - 1) {
                                Spacer(modifier = Modifier.width(20.dp))
                                VerticalDivider(
                                    modifier = Modifier.height(25.dp),
                                    color = Divider
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        // get content from each screen
        content(innerPadding)
    }
}