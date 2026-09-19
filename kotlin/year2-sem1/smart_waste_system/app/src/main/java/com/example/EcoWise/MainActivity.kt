package com.example.EcoWise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.EcoWise.GuidelinesScreen.AboutUsScreen
import com.example.EcoWise.GuidelinesScreen.GuidelinesScreen
import com.example.EcoWise.UserModule.UserLogin
import com.example.EcoWise.UserModule.UserProfileScreen
import com.example.EcoWise.data.EcoWiseRepository
import com.example.EcoWise.homescreen.HomeScreen
import com.example.EcoWise.module_1_EcoScanner.AnalyseHistoryScreen
import com.example.EcoWise.module_1_EcoScanner.AnalyseScreen
import com.example.EcoWise.module_1_EcoScanner.FavouriteListScreen
import com.example.EcoWise.module_1_EcoScanner.ProductAnalysisResultScreen
import com.example.EcoWise.module_2_RecyclingGuide.EcoWiseRecyclingActivityTurnin
import com.example.EcoWise.module_2_RecyclingGuide.RecyclingCentresResultScreen
import com.example.EcoWise.module_2_RecyclingGuide.RecyclingCentresScreen
import com.example.EcoWise.module_2_RecyclingGuide.RecyclingGuideDetailScreen
import com.example.EcoWise.module_2_RecyclingGuide.RecyclingGuideScreen
import com.example.EcoWise.module_3_EcoChallenge.ChallengeProgressScreen
import com.example.EcoWise.module_3_EcoChallenge.ChallengeScreen
import com.example.EcoWise.module_4_EcoStatistics.MyRewardsScreen
import com.example.EcoWise.module_4_EcoStatistics.RewardsShopScreen
import com.example.EcoWise.module_4_EcoStatistics.UserStatisticsScreen
import com.example.EcoWise.ui.theme.MAD_EcoWise_TeeZhongKai_ChanKuanFu_LeeGinShyang_LimChunChenTheme

/*
    @Composable
    public fun CenterAlignedTopAppBar(
        title: @Composable (() -> Unit),
        modifier: Modifier = Modifier,
        navigationIcon: @Composable (() -> Unit) = {},
        actions: @Composable (RowScope.() -> Unit) = {},
        expandedHeight: Dp = TopAppBarDefaults.TopAppBarExpa…,
        windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
        colors: TopAppBarColors = TopAppBarDefaults.topAppBarColo…,
        scrollBehavior: TopAppBarScrollBehavior? = null
    ): Unit

 */

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MAD_EcoWise_TeeZhongKai_ChanKuanFu_LeeGinShyang_LimChunChenTheme {
                Scaffold(){innerPadding ->
                    EcoWiseAppNavigation(modifier = Modifier.padding(innerPadding).fillMaxSize())
                }
            }
        }
    }
}
/*
App Navigation Graph:
Login (-1)

Home (0)
-> Dashboard	->  {
	if -> [Analyse Item]  { -> Analyse (1)}
	else if -> [Recycle Guide]  { -> Guide (2)}
	else if -> [**new** Active Challenge]  { -> Challenge (3)}
	else if -> [**new** View All Recent Analyzed Item]  { -> **new** Analyse History (4)}
	else if -> [Redeem Rewards]  { -> **new** Rewards Shop (5)}
	else if -> [Profile]  { -> User Profile (6)}
	else if -> [**new** About Us]  { -> **new** About Us (7)}
}

Guide (2)
-> Category  	->  [**new** Fill-in Product Information]
-> Analyse (1)  	->  [View Analysis]
-> Product Analysis  	->  [Find Nearby Drop-offs]
-> Recycling Centres Map	->  [**new** Select Place]
-> Recycling Center Result	->  [**new** Turn-in]
-> Recycling Turn-in Result	->  {
	if  ->  [**new** Return Home]  { -> EcoWise Home Screen }
	else if -> [**new** Recycling Guide]  { -> Guide (2)} //loop
	else if -> LazyRow[**new** <Category_icon>] { -> Analyse (1)} //loop
}


Challenge (3)
-> Active Challenges  ->  {
	if  ->  [+]  {
	   -> Challenge Progress  ->  {
	   	if  ->  [Choose Image]  -> Gallery  ->  [**new** Selected]
		else if  ->  [**new** Delete Photo X]  ->  // Remove photo
		else if  ->  [Submit]  ->  // Update challenge progress
	}
	else if -> [**new** Redeem Rewards]  { -> **new** Rewards Shop (5) }
}


Analyse History (4)
-> Recent Analyzed Item  ->  [**new** Manage History]  // tick to select each ACTIVITY
-> {
	if  ->  [**new** Keep in Favorites]  { -> **new** Favorite List}
	else if  ->  [**new** Delete Records]   ->  // Delete records
}


Rewards Shop (5)
-> EcoWise Rewards Shop
THREE Category:
Digital & Vouchers (TnG Reload PIN, GrabFood Vouchers, Shopee Vouchers)
Daily Essentials (Mugs, Folding Umbrellas, Custom Notebooks, Tissues, Storage Boxes)
Tech & Gadgets (Power Banks, Charging Cables, Mini USB Fans, Bluetooth Earphones)
Merchandise & Collectibles (Labubu Figures, Co-branded Merch)
Eco-Friendly (Biodegradable Bags, Reusable Straws, Wooden Cutlery Sets, Succulents
-> LazyColumn [ LazyRow [**new** <Rewards Icon>] ]
-> Rewards Information  -> [**new** Redeem]
-> My rewards


User Profile (6)
-> User Profile Page
-> {
	if  ->  ['Light/Dark Mode']  ->  // change theme
	else if  ->  [My Statistics]  { ->  Statistics }
	else if  ->  [My Rewards]  { ->  **new** My Rewards}
	else if  ->  [Badge Journey]  { ->  **new** Badge Journey}
	else if  ->  [Sign Out]  { ->  Login (-1)}

}


About Us (7)
-> About Us
-> LazyColumn [ IMAGE + ESSAY (About Us) ]


* */


@Composable
fun EcoWiseAppNavigation(modifier : Modifier){
    val ecowiseNav = rememberNavController()

    NavHost(
        navController = ecowiseNav,
        startDestination = "EcoWise Application Introduction",
        modifier = modifier
    ) {
        // 0. Guide & Login (-1 & 0)
        // Guide Page
        composable(route = "EcoWise Application Introduction") {
            GuidelinesScreen(
                navController = ecowiseNav,
                modifier = Modifier
            )
        }

        // Login page
        composable(route = "EcoWise Login") {
            UserLogin(
                navController = ecowiseNav,
                initialIsRegister = false, // first time must register OR login
                modifier = Modifier
            )
        }

        // Home Screen (0) - Dashboard
        composable(route = "EcoWise Home Screen/{userName}") { backStackEntry ->
            // get username from login page
            val getUserName = backStackEntry.arguments?.getString("userName")?: "EcoWise User (Default)"

            HomeScreen(
                modifier = Modifier,
                navController = ecowiseNav,
                currentUserName = getUserName
            )
        }

        // module 1 : EcoScanner -- analyze (1)
        // EcoScanner entry page
        // with null category
        composable(route = "EcoWise Scanner") {
            AnalyseScreen(
                navController = ecowiseNav,
                initialCategory = null, // null category -> user must select
                modifier = Modifier
            )
        }
        // EcoScanner entry page
        // with category from Fill-in Product Information
        composable(
            route = "EcoWise Scanner/{categoryTitle}",
            arguments = listOf(
                navArgument("categoryTitle") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val categoryTitle = backStackEntry.arguments?.getString("categoryTitle") ?: ""
            AnalyseScreen(
                navController = ecowiseNav,
                initialCategory = categoryTitle, // get category
                modifier = Modifier
            )
        }

        // Product Analysis Result with ID parameter
        composable(
            route = "Product Analysis Result/{productId}",
            arguments = listOf(
                navArgument("productId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            ProductAnalysisResultScreen(
                navController = ecowiseNav,
                productId = productId,
                modifier = Modifier
            )
        }


        /*  new -- add on */
        // Analyze History (4)
        composable(route = "Analyse History") {
            AnalyseHistoryScreen(
                navController = ecowiseNav,
                modifier = Modifier
            )
        }

        /*  new -- add on */
        // Favorite List
        composable(route = "Favorite List") {
            FavouriteListScreen(
                navController = ecowiseNav,
                modifier = Modifier
            )
        }


        // module 2 : Recycling Guide -- guide (2)
        // Recycling Guide Main Screen
        composable(route = "EcoWise Recycling Guide") {
            RecyclingGuideScreen(
                navController = ecowiseNav,
                modifier = Modifier
            )
        }

        /*  new -- add on */
        // Fill-in Product Information -- with product category information
        composable(
            route = "EcoWise Recycling Category Information/{categoryId}",
            arguments = listOf(
                navArgument("categoryId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId") ?: "plastic"
            RecyclingGuideDetailScreen(
                navController = ecowiseNav,
                categoryId = categoryId,
                modifier = Modifier
            )
        }

        // Recycling Centres Map View
        // Find Nearby Drop-offs Center
        composable(route = "EcoWise Recycling View Drop-offs Center Maps") {
            RecyclingCentresScreen(
                navController = ecowiseNav,
                modifier = Modifier
            )
        }

        /*  new -- add on */
        // Recycling Center Select Result
        composable(
            route = "EcoWise Recycling Drop-offs Center Result/{centreId}",
            arguments = listOf(
                navArgument("centreId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val centreId = backStackEntry.arguments?.getString("centreId") ?: "centre-1"
            RecyclingCentresResultScreen(
                navController = ecowiseNav,
                centreId = centreId,
                modifier = Modifier
            )
        }

        /*  new -- add on */
        // Recycling Turn-in Result
        composable(route = "EcoWise Recycling Turn-in Result") {
            /*  show 'successfully' msg  */
            /*  return 'home' OR 'recycling guide' button  */
            EcoWiseRecyclingActivityTurnin(
                navController = ecowiseNav,
                modifier = Modifier
            )
        }


        // module 3 : EcoChallenge -- challenge (3)
        // Active Challenges Main Screen
        composable(route = "EcoWise Challenge") {
            ChallengeScreen(
                navController = ecowiseNav,
                modifier = Modifier
            )
        }

        // Challenge Progress / Choose Image & Submit
        composable(
            route = "Challenge Progress Upload/{challengeId}",
            arguments = listOf(
                navArgument("challengeId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val challengeId = backStackEntry.arguments?.getString("challengeId") ?: ""

            val challenges by EcoWiseRepository.challenges.collectAsState()
            val challenge = challenges.find { it.id == challengeId }

            ChallengeProgressScreen(
                navController = ecowiseNav,
                challenge = challenge,
                modifier = Modifier
            )
        }


        // module 4 : EcoStatistics -- profile & extensions (5, 6, 7)
        /*  new -- add on */
        // Rewards Shop (5)
        composable(route = "EcoWise Rewards Shop") {
            RewardsShopScreen(
                navController = ecowiseNav,
                modifier = Modifier
            )
        }

        /*  new -- add on */
        // Rewards Detail & Redeem
        composable(
            route = "Rewards Information/{rewardId}",
            arguments = listOf(
                navArgument("rewardId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val rewardId = backStackEntry.arguments?.getString("rewardId") ?: ""
            RewardsShopScreen(
                navController = ecowiseNav,
                modifier = Modifier
            )
        }

        // User Profile Main Screen (6)
        composable(route = "EcoWise User Profile") {
            UserProfileScreen(
                navController = ecowiseNav,
                modifier = Modifier
            )
        }

        // User Statistics
        composable(route = "User Statistics") {
            UserStatisticsScreen(
                navController = ecowiseNav,
                modifier = Modifier
            )
        }

        /*  new -- add on */
        // My Rewards
        composable(route = "My Rewards") {
            MyRewardsScreen(
                navController = ecowiseNav,
                modifier = Modifier
            )
        }

        /*  new -- add on */
        // About Us (7)
        composable(route = "About Us") {
            AboutUsScreen(
                navController = ecowiseNav,
                modifier = Modifier
            )
        }
    }
}

