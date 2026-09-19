package com.example.EcoWise.data

import com.example.EcoWise.RoomDB.ChallengeProgressDao
import com.example.EcoWise.RoomDB.ChallengeProgressEntity
import com.example.EcoWise.RoomDB.ProductDao
import com.example.EcoWise.RoomDB.ProductEntity
import com.example.EcoWise.RoomDB.UserDao
import com.example.EcoWise.RoomDB.UserEntity
import com.example.EcoWise.model.*
import com.example.EcoWise.network.ProductRemoteDto
import com.example.EcoWise.network.ProductService
import com.example.EcoWise.network.UserRemoteDto
import com.example.EcoWise.network.UserService
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

/**
 * EcoWiseRepository bridges local Room database, Supabase cloud sync,
 * and provides state flows compatible with all UI screens.
 */
object EcoWiseRepository {

    // Daos and Services will be initialized or injected via ViewModel
    private var productDao: ProductDao? = null
    private var userDao: UserDao? = null
    private var challengeProgressDao: ChallengeProgressDao? = null
    private var rewardDao: com.example.EcoWise.RoomDB.RewardDao? = null
    private var currentUserId: String? = null
    private val _currentUserIdFlow = MutableStateFlow<String?>(null)
    val currentUserIdFlow: StateFlow<String?> = _currentUserIdFlow.asStateFlow()

    private val productService = ProductService()
    private val userService = UserService()
    private val challengeProgressService = com.example.EcoWise.network.ChallengeProgressService()
    private val rewardService = com.example.EcoWise.network.RewardService()

    fun initialize(pDao: ProductDao, uDao: UserDao, cpDao: ChallengeProgressDao, rDao: com.example.EcoWise.RoomDB.RewardDao) {
        productDao = pDao
        userDao = uDao
        challengeProgressDao = cpDao
        rewardDao = rDao
    }

    fun clearSession() {
        currentUserId = null
        _currentUserIdFlow.value = null
        _recentAnalyzed.value = emptyList()
        _favoriteProducts.value = emptyList()
        _redeemedRewards.value = emptyList()
        _userStats.value = UserEcoStats()
        _userName.value = "Guest"
        _userEmail.value = ""
        totalEcoScoreSum = 0
        
        // Reset challenges to initial state
        _challenges.value = listOf(
            EcoChallenge("ch-1", "Plastic-Free Week", "Avoid all single-use plastics today.", 0, 7, "days", "ACTIVE", 500),
            EcoChallenge("ch-2", "Ride a Bike", "Commute via cycling for 5 days.", 0, 5, "rides", "ACTIVE", 350),
            EcoChallenge("ch-3", "Bring Your Own Bag", "Use reusable shopping bags for groceries.", 0, 5, "trips", "ACTIVE", 200),
            EcoChallenge("ch-4", "Plant a Tree", "Contribute to reforestation by planting a tree.", 0, 1, "tree", "ACTIVE", 1000),
            EcoChallenge("ch-5", "Zero Waste Day", "Go an entire day without producing trash.", 0, 1, "day", "ACTIVE", 400),
            EcoChallenge("ch-6", "Cold Shower", "Save energy by taking a cold shower.", 0, 3, "showers", "ACTIVE", 150),
            EcoChallenge("ch-7", "Unplug Electronics", "Unplug devices when not in use.", 0, 7, "days", "ACTIVE", 250),
            EcoChallenge("ch-8", "Compost Scraps", "Start composting your organic kitchen waste.", 0, 5, "days", "ACTIVE", 300)
        )
    }

    fun getCurrentUserId(): String? = currentUserId

    private val _userName = MutableStateFlow("Guest")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _userEmail = MutableStateFlow("alex@ecowise.app")
    val userEmail: StateFlow<String> = _userEmail.asStateFlow()

    fun updateUserInfo(name: String, email: String) {
        try {
            if (name.isNotBlank()) _userName.value = name
            if (email.isNotBlank()) _userEmail.value = email
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun signUpUser(id: String, fullName: String, email: String, password: String, isGoogle: Boolean): Boolean = withContext(Dispatchers.IO) {
        val normalizedEmail = email.trim().lowercase()
        return@withContext try {
            // Check if user already exists in Supabase
            val existingRemote = userService.fetchUserFromSupabase(normalizedEmail)
            if (existingRemote != null) {
                return@withContext false // Account already exists
            }
            
            // Also check local DB for extra safety
            val existingLocal = userDao?.getUserByEmail(normalizedEmail)
            if (existingLocal != null) {
                return@withContext false
            }

            // Important: Clear previous session state before setting new user
            clearSession()

            currentUserId = id
            _currentUserIdFlow.value = id
            val userEntity = UserEntity(
                id = id,
                fullName = fullName.trim(),
                email = normalizedEmail,
                password = password,
                profilePictureUrl = null,
                isGoogleAccount = isGoogle,
                ecoPoints = 0,
                productsAnalysed = 0,
                challengesCompleted = 0
            )
            // Save locally and sync to Supabase
            saveUserToDb(userEntity)
            
            updateUserInfo(userEntity.fullName, normalizedEmail)
            _userStats.value = UserEcoStats() // Reset stats for new user
            
            // Load fresh challenges and rewards for new user
            loadLocalChallenges()
            loadLocalRewards()
            
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    suspend fun signInUser(email: String, password: String): UserEntity? = withContext(Dispatchers.IO) {
        val normalizedEmail = email.trim().lowercase()

        // 1. Fetch remote user from Supabase
        val remoteUser = try {
            userService.fetchUserFromSupabase(normalizedEmail)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

        // 2. Fetch local user from Room DB as candidate / fallback
        val localUser = try {
            userDao?.getUserByEmail(normalizedEmail)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

        // 3. Authenticate against remote or local credentials
        var authenticatedUser: UserEntity? = null

        if (remoteUser != null) {
            if (remoteUser.password.isNotEmpty() && remoteUser.password == password) {
                // Direct remote authentication match
                authenticatedUser = UserEntity(
                    id = remoteUser.id,
                    fullName = remoteUser.fullName,
                    email = remoteUser.email,
                    password = remoteUser.password,
                    profilePictureUrl = remoteUser.profilePictureUrl,
                    isGoogleAccount = remoteUser.isGoogleAccount,
                    ecoPoints = remoteUser.ecoPoints,
                    productsAnalysed = remoteUser.productsAnalysed,
                    challengesCompleted = remoteUser.challengesCompleted
                )
            } else if (remoteUser.password.isEmpty() && localUser != null && localUser.password == password) {
                // Legacy healing: Remote user had empty password, but local password matched
                authenticatedUser = UserEntity(
                    id = remoteUser.id,
                    fullName = remoteUser.fullName,
                    email = remoteUser.email,
                    password = password,
                    profilePictureUrl = remoteUser.profilePictureUrl,
                    isGoogleAccount = remoteUser.isGoogleAccount,
                    ecoPoints = remoteUser.ecoPoints,
                    productsAnalysed = remoteUser.productsAnalysed,
                    challengesCompleted = remoteUser.challengesCompleted
                )
                // Self-heal remote password
                try {
                    userService.upsertUserToSupabase(
                        UserRemoteDto(
                            id = remoteUser.id,
                            fullName = remoteUser.fullName,
                            email = remoteUser.email,
                            password = password,
                            profilePictureUrl = remoteUser.profilePictureUrl,
                            isGoogleAccount = remoteUser.isGoogleAccount,
                            ecoPoints = remoteUser.ecoPoints,
                            productsAnalysed = remoteUser.productsAnalysed,
                            challengesCompleted = remoteUser.challengesCompleted
                        )
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        // Fallback: If remote lookup failed (table uninitialized, offline, etc.) but local user exists
        if (authenticatedUser == null && localUser != null && localUser.password == password) {
            authenticatedUser = localUser
            // Attempt to sync local user to Supabase
            try {
                userService.upsertUserToSupabase(
                    UserRemoteDto(
                        id = localUser.id,
                        fullName = localUser.fullName,
                        email = localUser.email,
                        password = localUser.password,
                        profilePictureUrl = localUser.profilePictureUrl,
                        isGoogleAccount = localUser.isGoogleAccount,
                        ecoPoints = localUser.ecoPoints,
                        productsAnalysed = localUser.productsAnalysed,
                        challengesCompleted = localUser.challengesCompleted
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        if (authenticatedUser != null) {
            clearSession()

            currentUserId = authenticatedUser.id
            _currentUserIdFlow.value = authenticatedUser.id

            try {
                userDao?.insertUser(authenticatedUser)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            updateUserInfo(authenticatedUser.fullName, authenticatedUser.email)

            // Restore session state
            _userStats.value = UserEcoStats(
                ecoPoints = authenticatedUser.ecoPoints,
                productsAnalysed = authenticatedUser.productsAnalysed,
                challengesCompleted = authenticatedUser.challengesCompleted
            )

            // Sync user data after login
            syncFromCloud()

            // Ensure local state is loaded even if sync fails
            loadLocalChallenges()
            loadLocalRewards()

            return@withContext authenticatedUser
        }

        return@withContext null
    }

    suspend fun loadLocalChallenges() = withContext(Dispatchers.IO) {
        val userId = currentUserId ?: return@withContext
        val allProgress = try {
            challengeProgressDao?.getAllChallengeProgress(userId)?.first()
        } catch (e: Exception) {
            null
        } ?: emptyList()
        
        val currentChallenges = _challenges.value
        val updatedChallenges = currentChallenges.map { ch ->
            val progressCount = allProgress.count { it.challengeId == ch.id }
            val newProg = progressCount.coerceAtMost(ch.totalProgress)
            ch.copy(
                currentProgress = newProg,
                statusBadge = if (newProg >= ch.totalProgress) "COMPLETED" else if (newProg > 0) "IN PROGRESS" else "ACTIVE"
            )
        }
        _challenges.value = updatedChallenges
    }

    suspend fun loadLocalRewards() = withContext(Dispatchers.IO) {
        val userId = currentUserId ?: return@withContext
        val allRewards = try {
            rewardDao?.getRedeemedRewards(userId)?.first()
        } catch (e: Exception) {
            null
        } ?: emptyList()
        
        _redeemedRewards.value = allRewards.map { entity ->
            RedeemedReward(entity.id, entity.title, entity.dateFormatted, entity.status)
        }
    }

    // Pre-populated reference products
    val sampleCoffeeProduct = ProductItem(
        id = "coffee-101",
        name = "Organic Coffee Pack",
        brand = "Mount Hagen",
        category = "Food & Beverages",
        weightSize = "100g",
        packagingMaterial = "Mixed Material",
        isOrganic = true,
        ecoLabels = listOf("Organic", "Fair trade"),
        ecoScore = 88,
        carbonFootprint = CarbonLevel.LOW,
        recyclability = RecyclabilityLevel.HIGH,
        impactDetails = listOf(
            ImpactDetail("Low Carbon Footprint", "Produced with renewable energy and sustainable farming methods.", "carbon"),
            ImpactDetail("Recyclable Packaging", "Outer foil layer is separable and fully curbside recyclable.", "recyclable"),
            ImpactDetail("Water Efficient", "Washing and drying processes utilize closed-loop water recirculation.", "water")
        ),
        recyclingGuide = RecyclingGuideInfo(
            binName = "Yellow Bin",
            binColorHex = "#EAB308",
            instructions = "The packaging is 100% recyclable in the yellow bin. Ensure it is clean and dry before disposal."
        ),
        alternative = EcoAlternative(
            title = "Local Fair-Trade Coffee",
            description = "Consider buying loose beans in your own container from local roasters to achieve zero waste."
        ),
        visualType = "coffee"
    )

    val sampleOatMilk = ProductItem(
        id = "oat-milk-1",
        name = "Oat Milk",
        brand = "Oatly",
        category = "Food & Beverages",
        weightSize = "1L",
        packagingMaterial = "FSC Paperboard",
        isOrganic = true,
        ecoLabels = listOf("Organic", "FSC Certified", "Recyclable"),
        ecoScore = 92,
        carbonFootprint = CarbonLevel.LOW,
        recyclability = RecyclabilityLevel.HIGH,
        impactDetails = listOf(
            ImpactDetail("Low Carbon Emissions", "Oat crops consume 80% less land and produce minimal CO2.", "carbon"),
            ImpactDetail("FSC Paper Carton", "Renewable packaging sourced from sustainably managed forests.", "recyclable"),
            ImpactDetail("Low Water Footprint", "Requires up to 7x less water than dairy or almond milk.", "water")
        ),
        recyclingGuide = RecyclingGuideInfo(
            binName = "Yellow Bin",
            binColorHex = "#EAB308",
            instructions = "Rinse carton and flatten before placing in recycling bin."
        ),
        alternative = EcoAlternative(
            title = "Homemade Oat Milk",
            description = "Make quick fresh oat milk at home with oats and water for near-zero emissions."
        ),
        visualType = "oat_milk"
    )

    val samplePlasticBottle = ProductItem(
        id = "plastic-bottle-2",
        name = "Plastic Water Bottle",
        brand = "Coca-cola",
        category = "Food & Beverages",
        weightSize = "500ml",
        packagingMaterial = "PET Plastic",
        isOrganic = false,
        ecoLabels = emptyList(),
        ecoScore = 24,
        carbonFootprint = CarbonLevel.HIGH,
        recyclability = RecyclabilityLevel.LOW,
        impactDetails = listOf(
            ImpactDetail("High Carbon Footprint", "Single-use petroleum-based virgin plastic manufacturing.", "carbon"),
            ImpactDetail("Low Degradability", "Takes up to 450 years to decompose in natural environments.", "recyclable"),
            ImpactDetail("Microplastic Risk", "Degrades into harmful microplastics under heat and sunlight.", "water")
        ),
        recyclingGuide = RecyclingGuideInfo(
            binName = "Yellow Bin / Drop-off",
            binColorHex = "#EAB308",
            instructions = "Empty contents, crush bottle, and leave cap on for proper sorting."
        ),
        alternative = EcoAlternative(
            title = "Stainless Steel Reusable Bottle",
            description = "Switching to an insulated bottle eliminates over 150 single-use plastic bottles per year."
        ),
        visualType = "water_bottle"
    )

    val sampleStyrofoam = ProductItem(
        id = "styrofoam-3",
        name = "Styrofoam container",
        brand = "Generic Takeout",
        category = "Packaging",
        weightSize = "10g",
        packagingMaterial = "Expanded Polystyrene",
        isOrganic = false,
        ecoLabels = emptyList(),
        ecoScore = 24,
        carbonFootprint = CarbonLevel.HIGH,
        recyclability = RecyclabilityLevel.LOW,
        impactDetails = listOf(
            ImpactDetail("High Carbon Footprint", "Synthesized from petroleum hydrocarbons.", "carbon"),
            ImpactDetail("Non-Biodegradable", "Fills landfills indefinitely without breaking down.", "recyclable"),
            ImpactDetail("Leaching Hazards", "Can leach styrene when exposed to high heat foods.", "water")
        ),
        recyclingGuide = RecyclingGuideInfo(
            binName = "Red Trash Bin",
            binColorHex = "#EF4444",
            instructions = "Not accepted in curbside recycling bins. Must be disposed in general waste."
        ),
        alternative = EcoAlternative(
            title = "Sugarcane Bagasse Container",
            description = "100% commercially compostable made from natural agricultural by-products."
        ),
        visualType = "styrofoam"
    )

    private val _recentAnalyzed = MutableStateFlow<List<ProductItem>>(
        listOf(sampleOatMilk, samplePlasticBottle, sampleCoffeeProduct, sampleStyrofoam)
    )
    val recentAnalyzed: StateFlow<List<ProductItem>> = _recentAnalyzed.asStateFlow()

    private val _currentAnalyzedProduct = MutableStateFlow<ProductItem>(sampleCoffeeProduct)
    val currentAnalyzedProduct: StateFlow<ProductItem> = _currentAnalyzedProduct.asStateFlow()

    val aboutUsSections = listOf(
        com.example.EcoWise.model.AboutUsSection(
            id = "mission",
            heading = "Our Mission",
            essay = "EcoWise is dedicated to empowering consumers to make environmentally responsible choices. We believe that small daily changes in individual consumption habits add up to a significant positive impact on our planet's future."
        ),
        com.example.EcoWise.model.AboutUsSection(
            id = "vision",
            heading = "Our Vision",
            essay = "We envision a world where sustainability is seamlessly integrated into every buying decision, transparent product life-cycle data is easily accessible, and eco-conscious living is celebrated and rewarded."
        )
    )

    val recyclingGuides = listOf(
        RecyclingCategoryGuide(
            id = "plastic",
            title = "Plastic",
            iconType = "plastic",
            summary = "Check the resin code (1–7). Rinse before recycling.",
            acceptedItems = listOf("PET bottles", "HDPE containers", "Plastic bags"),
            fullInstructions = "Check the resin code (1-7). Rinse before recycling."
        ),
        RecyclingCategoryGuide(
            id = "glass",
            title = "Glass",
            iconType = "glass",
            summary = "Separate by colour. Remove caps and lids.",
            acceptedItems = listOf("Wine bottles", "Glass jars", "Food containers"),
            fullInstructions = "Separate by colour. Remove metal caps and lids. Never mix window glass or ceramics."
        ),
        RecyclingCategoryGuide(
            id = "paper",
            title = "Paper",
            iconType = "paper",
            summary = "Keep dry. Remove plastic windows from envelopes.",
            acceptedItems = listOf("Newspapers", "Cardboard", "Magazines", "Office Paper"),
            fullInstructions = "Keep paper clean and completely dry. Flatten cardboard boxes to save bin space."
        ),
        RecyclingCategoryGuide(
            id = "metal",
            title = "Metal",
            iconType = "metal",
            summary = "Rinse containers. Crush cans to save space.",
            acceptedItems = listOf("Aluminium cans", "Steel tins", "Foil wrap"),
            fullInstructions = "Rinse containers cleanly. Crush aluminium beverage cans. Aerosol cans must be fully empty."
        ),
        RecyclingCategoryGuide(
            id = "electronics",
            title = "Electronics",
            iconType = "electronics",
            summary = "Never bin e-waste. Use certified drop-off points.",
            acceptedItems = listOf("Smartphones", "Laptops", "Batteries", "Power cables"),
            fullInstructions = "Never place e-waste in curbside bins. Drop off at designated collection depots."
        ),
        RecyclingCategoryGuide(
            id = "organic",
            title = "Organic",
            iconType = "organic",
            summary = "Compost at home or use green waste bins.",
            acceptedItems = listOf("Food scraps", "Garden clippings", "Coffee grounds"),
            fullInstructions = "Separate compostable materials from plastic wrap. Use aerated compost bins or municipal green bins."
        )
    )

    // Recycling Centres
    private val _recyclingCentres = MutableStateFlow(
        listOf(
            RecyclingCentre(
                id = "centre-1",
                name = "GreenDrop Recycling Hub",
                rating = 4.8,
                isOpen = true,
                distanceKm = 0.4,
                acceptedMaterials = listOf("Plastic", "Glass", "Paper"),
                address = "742 Evergreen Terrace, Eco District",
                latitude = 3.1390,
                longitude = 101.6869,
                isSaved = false
            ),
            RecyclingCentre(
                id = "centre-2",
                name = "City Eco Centre",
                rating = 4.5,
                isOpen = true,
                distanceKm = 1.2,
                acceptedMaterials = listOf("Electronics", "Metal", "Plastic"),
                address = "120 Sustainable Blvd",
                latitude = 3.1478,
                longitude = 101.6953,
                isSaved = false
            ),
            RecyclingCentre(
                id = "centre-3",
                name = "Community Compost Point",
                rating = 4.2,
                isOpen = false,
                distanceKm = 2.0,
                acceptedMaterials = listOf("Organic", "Paper"),
                address = "45 Greenway Park Road",
                latitude = 3.1256,
                longitude = 101.6743,
                isSaved = false
            ),
            RecyclingCentre(
                id = "centre-4",
                name = "E-Waste Specialists",
                rating = 4.9,
                isOpen = true,
                distanceKm = 3.1,
                acceptedMaterials = listOf("Electronics"),
                address = "88 Tech Loop, Innovation Park",
                latitude = 3.1601,
                longitude = 101.7002,
                isSaved = false
            ),
            RecyclingCentre(
                id = "centre-5",
                name = "Metro Glass & Metal Depot",
                rating = 4.6,
                isOpen = true,
                distanceKm = 4.5,
                acceptedMaterials = listOf("Glass", "Metal"),
                address = "210 Riverfront Way",
                latitude = 3.1198,
                longitude = 101.6802,
                isSaved = false
            )
        )
    )
    val recyclingCentres: StateFlow<List<RecyclingCentre>> = _recyclingCentres.asStateFlow()

    fun toggleSaveCentre(centreId: String) {
        try {
            val updated = _recyclingCentres.value.map { centre ->
                if (centre.id == centreId) {
                    centre.copy(isSaved = !centre.isSaved)
                } else {
                    centre
                }
            }
            _recyclingCentres.value = updated
            val savedCount = updated.count { it.isSaved }
            val curr = _userStats.value
            _userStats.value = curr.copy(savedCentresCount = savedCount)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // Active Challenges
    private val _challenges = MutableStateFlow<List<EcoChallenge>>(
        listOf(
            EcoChallenge("ch-1", "Plastic-Free Week", "Avoid all single-use plastics today.", 0, 7, "days", "ACTIVE", 500),
            EcoChallenge("ch-2", "Ride a Bike", "Commute via cycling for 5 days.", 0, 5, "rides", "ACTIVE", 350),
            EcoChallenge("ch-3", "Bring Your Own Bag", "Use reusable shopping bags for groceries.", 0, 5, "trips", "ACTIVE", 200),
            EcoChallenge("ch-4", "Plant a Tree", "Contribute to reforestation by planting a tree.", 0, 1, "tree", "ACTIVE", 1000),
            EcoChallenge("ch-5", "Zero Waste Day", "Go an entire day without producing trash.", 0, 1, "day", "ACTIVE", 400),
            EcoChallenge("ch-6", "Cold Shower", "Save energy by taking a cold shower.", 0, 3, "showers", "ACTIVE", 150),
            EcoChallenge("ch-7", "Unplug Electronics", "Unplug devices when not in use.", 0, 7, "days", "ACTIVE", 250),
            EcoChallenge("ch-8", "Compost Scraps", "Start composting your organic kitchen waste.", 0, 5, "days", "ACTIVE", 300)
        )
    )
    val challenges: StateFlow<List<EcoChallenge>> = _challenges.asStateFlow()

    // Shop Rewards
    val rewardCategories = listOf("Digital & Vouchers", "Daily Essentials", "Tech & Gadgets", "Fans Merch Item", "Eco-Friendly")

    val shopRewards = listOf(
        ShopReward("REW_DV_1", "TNG Reload PIN", "RM10 eWallet reload", 500, "Digital & Vouchers", "POPULAR", "bottle"),
        ShopReward("REW_DV_2", "GrabFood Voucher", "RM8 off your next order", 400, "Digital & Vouchers", null, "bottle"),
        ShopReward("REW_DV_3", "SHOPEE Voucher", "RM5 off storewide", 300, "Digital & Vouchers", null, "bottle"),
        ShopReward("REW_DE_1", "EcoWise Mug", "Ceramic reusable mug", 450, "Daily Essentials", "20% OFF", "bottle"),
        ShopReward("REW_DE_2", "Folding Umbrella", "Compact & windproof", 600, "Daily Essentials", null, "tote"),
        ShopReward("REW_DE_3", "Custom Notebook", "Recycled-paper notebook", 250, "Daily Essentials", null, "tote"),
        ShopReward("REW_DE_4", "Tissue Pack (6)", "Bamboo pulp tissues", 150, "Daily Essentials", null, "bamboo"),
        ShopReward("REW_DE_5", "Storage Box", "Stackable organiser box", 700, "Daily Essentials", null, "tote"),
        ShopReward("REW_TG_1", "Power Bank 10000mAh", "Fast-charge power bank", 1200, "Tech & Gadgets", "15% OFF", "bottle"),
        ShopReward("REW_TG_2", "Charging Cable", "USB-C braided cable", 350, "Tech & Gadgets", null, "bottle"),
        ShopReward("REW_TG_3", "Mini USB Fan", "Portable desk fan", 500, "Tech & Gadgets", null, "bottle"),
        ShopReward("REW_TG_4", "Bluetooth Earphones", "Wireless earbuds", 1500, "Tech & Gadgets", "NEW", "bottle"),
        ShopReward("REW_FMI_1", "LABUBU", "Limited eco-edition figure", 2000, "Fans Merch Item", "LIMITED", "tote"),
        ShopReward("REW_FMI_2", "EcoWise Co-branded Tee", "100% organic cotton", 900, "Fans Merch Item", null, "tote"),
        ShopReward("REW_ECO_1", "Biodegradable Bags (10)", "Compostable shopping bags", 200, "Eco-Friendly", null, "tote"),
        ShopReward("REW_ECO_2", "Reusable Straw Set", "Stainless steel + brush", 250, "Eco-Friendly", null, "bamboo"),
        ShopReward("REW_ECO_3", "Wooden Cutlery Set", "Portable travel kit", 300, "Eco-Friendly", "15% OFF", "bamboo"),
        ShopReward("REW_ECO_4", "Succulent Plant", "Desk-friendly potted plant", 400, "Eco-Friendly", null, "tote")
    )

    fun rewardById(id: String): ShopReward? = shopRewards.find { it.id == id }

    private val _selectedReward = MutableStateFlow<ShopReward?>(null)
    val selectedReward: StateFlow<ShopReward?> = _selectedReward.asStateFlow()
    fun setSelectedReward(reward: ShopReward) { _selectedReward.value = reward }

    private val _redeemedRewards = MutableStateFlow<List<RedeemedReward>>(emptyList())
    val redeemedRewards: StateFlow<List<RedeemedReward>> = _redeemedRewards.asStateFlow()

    private var totalEcoScoreSum = 0

    private val _userStats = MutableStateFlow(UserEcoStats())
    val userStats: StateFlow<UserEcoStats> = _userStats.asStateFlow()

    fun addPoints(points: Int) {
        try {
            val curr = _userStats.value
            val newStats = curr.copy(
                ecoPoints = (curr.ecoPoints + points).coerceAtLeast(0)
            )
            _userStats.value = newStats
            
            // Persist points immediately
            @OptIn(DelicateCoroutinesApi::class)
            GlobalScope.launch(Dispatchers.IO) {
                syncUserToCloudAndDb(newStats)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun syncUserToCloudAndDb(stats: UserEcoStats) {
        val userId = currentUserId ?: return
        val currentPassword = try {
            userDao?.getUserByEmail(_userEmail.value)?.password ?: ""
        } catch (e: Exception) {
            ""
        }
        val updatedUser = UserEntity(
            id = userId,
            fullName = _userName.value,
            email = _userEmail.value,
            password = currentPassword,
            profilePictureUrl = null,
            isGoogleAccount = false, // This might need to be tracked more carefully
            ecoPoints = stats.ecoPoints,
            productsAnalysed = stats.productsAnalysed,
            challengesCompleted = stats.challengesCompleted
        )
        
        // Save to local Room
        userDao?.insertUser(updatedUser)
        
        // Sync to Supabase
        userService.upsertUserToSupabase(UserRemoteDto(
            id = updatedUser.id,
            fullName = updatedUser.fullName,
            email = updatedUser.email,
            password = currentPassword,
            profilePictureUrl = updatedUser.profilePictureUrl,
            isGoogleAccount = updatedUser.isGoogleAccount,
            ecoPoints = updatedUser.ecoPoints,
            productsAnalysed = updatedUser.productsAnalysed,
            challengesCompleted = updatedUser.challengesCompleted
        ))
    }

    fun setCurrentProduct(product: ProductItem) {
        try {
            _currentAnalyzedProduct.value = product
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getProductItemById(id: String): ProductItem? = withContext(Dispatchers.IO) {
        val userId = currentUserId ?: return@withContext null
        // First check memory
        val inMemory = _recentAnalyzed.value.find { it.id == id }
        if (inMemory != null) return@withContext inMemory

        // Then check Room
        val entity = try {
            productDao?.getProductById(id, userId)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
        
        return@withContext entity?.let { pEntity ->
            // Re-derive the full ProductItem from Entity
            val carbon = try { CarbonLevel.valueOf(pEntity.carbonFootprint) } catch (e: Exception) { CarbonLevel.LOW }
            val recyclability = try { RecyclabilityLevel.valueOf(pEntity.recyclability) } catch (e: Exception) { RecyclabilityLevel.HIGH }

            ProductItem(
                id = pEntity.id,
                name = pEntity.name,
                brand = pEntity.brand,
                category = pEntity.category,
                weightSize = pEntity.weightSize,
                packagingMaterial = pEntity.packagingMaterial,
                isOrganic = pEntity.isOrganic,
                ecoScore = pEntity.ecoScore,
                carbonFootprint = carbon,
                recyclability = recyclability,
                visualType = pEntity.visualType
            )
        }
    }

    fun analyzeNewProduct(
        name: String,
        category: String,
        brand: String,
        weightSize: String,
        packagingMaterial: String,
        isOrganic: Boolean,
        ecoLabels: List<String>
    ): ProductItem {
        var score = 50
        if (isOrganic) score += 20
        if (ecoLabels.contains("Organic")) score += 10
        if (ecoLabels.contains("FSC Certified")) score += 10
        if (ecoLabels.contains("Fair Trade")) score += 10
        if (ecoLabels.contains("Recyclable")) score += 10
        if (ecoLabels.contains("BPA Free")) score += 5

        when (packagingMaterial.lowercase()) {
            "plastic" -> score -= 20
            "electronics" -> score -= 15
            "metal" -> score += 10
            "glass" -> score += 15
            "paper" -> score += 20
            "biodegradable" -> score += 25
        }
        score = score.coerceIn(15, 98)

        val carbon = if (score >= 75) CarbonLevel.LOW else if (score >= 45) CarbonLevel.MEDIUM else CarbonLevel.HIGH
        val recyclability = if (score >= 40) RecyclabilityLevel.HIGH else RecyclabilityLevel.LOW

        val impacts = listOf(
            ImpactDetail("Carbon Footprint", "Environmentally conscious production with reduced greenhouse emissions.", "carbon"),
            ImpactDetail("Recyclable Packaging", "Easily separated and processed by standard municipal recycling streams.", "recyclable")
        )

        val binInfo = RecyclingGuideInfo("Yellow Recycling Bin", "#EAB308", "The packaging is recyclable in the yellow bin. Ensure it is clean and dry.")
        val alt = EcoAlternative("Zero-Waste Bulk Alternative", "Purchase in refillable containers at a local zero-waste pantry.")

        val newProduct = ProductItem(
            id = "prod_" + System.currentTimeMillis(),
            name = name.ifBlank { "Eco Product" },
            brand = brand,
            category = category,
            weightSize = weightSize,
            packagingMaterial = packagingMaterial,
            isOrganic = isOrganic,
            ecoLabels = ecoLabels,
            ecoScore = score,
            carbonFootprint = carbon,
            recyclability = recyclability,
            impactDetails = impacts,
            recyclingGuide = binInfo,
            alternative = alt,
            visualType = "generic"
        )

        _currentAnalyzedProduct.value = newProduct
        _recentAnalyzed.value = listOf(newProduct) + _recentAnalyzed.value.take(5)

        val curr = _userStats.value
        val newProductsAnalysed = curr.productsAnalysed + 1
        totalEcoScoreSum += score
        val newAvgEcoScore = totalEcoScoreSum / newProductsAnalysed

        val newCategoryStats = curr.categoryStats.map { (catName, count) ->
            val matches = catName.contains(category, ignoreCase = true) ||
                    category.contains(catName, ignoreCase = true)
            if (matches) catName to (count + 1) else catName to count
        }

        _userStats.value = curr.copy(
            productsAnalysed = newProductsAnalysed,
            monthProducts = curr.monthProducts + 1,
            ecoPoints = curr.ecoPoints + 20,
            monthPoints = curr.monthPoints + 20,
            avgEcoScore = newAvgEcoScore,
            categoryStats = newCategoryStats
        )
        
        // Persist analysis stats
        @OptIn(DelicateCoroutinesApi::class)
        GlobalScope.launch(Dispatchers.IO) {
            syncUserToCloudAndDb(_userStats.value)
        }

        return newProduct
    }

    suspend fun submitChallengeActivity(challengeId: String, activityText: String, hasPhoto: Boolean): String? = withContext(Dispatchers.IO) {
        val userId = currentUserId ?: return@withContext "User not logged in"
        val existing = _challenges.value.find { it.id == challengeId }
        if (existing == null) return@withContext "Challenge not found"
        
        // 100% completion restriction
        if (existing.currentProgress >= existing.totalProgress) {
            return@withContext "Challenge already completed!"
        }

        // 24-Hour Cooldown
        val lastProgress = try {
            challengeProgressDao?.getLastProgressForChallenge(challengeId, userId)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
        
        if (lastProgress != null) {
            val currentTime = System.currentTimeMillis()
            val diff = currentTime - lastProgress.timestamp
            val hours24 = 24 * 60 * 60 * 1000L
            if (diff < hours24) {
                val remainingMillis = hours24 - diff
                val remainingHours = remainingMillis / (60 * 60 * 1000)
                val remainingMinutes = (remainingMillis % (60 * 60 * 1000)) / (60 * 1000)
                return@withContext "Cooldown active. Try again in $remainingHours h $remainingMinutes m."
            }
        }

        val list = _challenges.value.map { ch ->
            if (ch.id == challengeId) {
                val nextProgress = (ch.currentProgress + 1).coerceAtMost(ch.totalProgress)
                val isCompleted = nextProgress >= ch.totalProgress
                ch.copy(
                    currentProgress = nextProgress,
                    statusBadge = if (isCompleted) "COMPLETED" else "IN PROGRESS"
                )
            } else ch
        }
        _challenges.value = list

        val updated = list.find { it.id == challengeId }
        val justCompleted = updated != null && updated.statusBadge == "COMPLETED"

        // Update Stats
        val curr = _userStats.value
        val newStats = curr.copy(
            ecoPoints = curr.ecoPoints + 50,
            monthPoints = curr.monthPoints + 50,
            challengesCompleted = if (justCompleted) curr.challengesCompleted + 1 else curr.challengesCompleted,
            monthChallenges = if (justCompleted) curr.monthChallenges + 1 else curr.monthChallenges
        )
        _userStats.value = newStats

        // Save to Room
        val entity = ChallengeProgressEntity(
            userId = userId,
            challengeId = challengeId,
            challengeTitle = existing.title,
            imagePath = if (hasPhoto) "internal_path_placeholder" else "",
            submissionDate = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date()),
            timestamp = System.currentTimeMillis(),
            isSubmitted = true,
            isSynced = false
        )
        try {
            challengeProgressDao?.insertProgress(entity)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Sync to Supabase
        try {
            challengeProgressService.insertProgressToSupabase(
                com.example.EcoWise.network.ChallengeProgressRemoteDto(
                    userId = entity.userId,
                    challengeId = entity.challengeId,
                    challengeTitle = entity.challengeTitle,
                    imagePath = entity.imagePath,
                    submissionDate = entity.submissionDate,
                    timestamp = entity.timestamp
                )
            )
            // Persist points and completion stats
            syncUserToCloudAndDb(newStats)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return@withContext null // No error
    }

    suspend fun redeemReward(reward: ShopReward): Boolean = withContext(Dispatchers.IO) {
        val userId = currentUserId ?: return@withContext false
        return@withContext try {
            val curr = _userStats.value
            if (curr.ecoPoints >= reward.pointsCost) {
                val updatedStats = curr.copy(ecoPoints = curr.ecoPoints - reward.pointsCost)
                _userStats.value = updatedStats
                
                val newRedeemed = RedeemedReward(
                    id = UUID.randomUUID().toString(),
                    title = reward.title,
                    dateFormatted = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date()),
                    status = "CLAIMED"
                )
                
                // Save to Room
                val entity = com.example.EcoWise.RoomDB.RewardEntity(
                    id = newRedeemed.id,
                    userId = userId,
                    title = newRedeemed.title,
                    dateFormatted = newRedeemed.dateFormatted,
                    status = newRedeemed.status
                )
                rewardDao?.insertReward(entity)

                // Persist points change
                syncUserToCloudAndDb(updatedStats)

                // Sync reward to Supabase
                try {
                    rewardService.insertRewardToSupabase(
                        com.example.EcoWise.network.RewardRemoteDto(
                            id = entity.id,
                            userId = entity.userId,
                            title = entity.title,
                            dateFormatted = entity.dateFormatted,
                            status = entity.status
                        )
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                _redeemedRewards.value = listOf(newRedeemed) + _redeemedRewards.value
                true
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private val _favoriteProducts = MutableStateFlow<List<ProductItem>>(emptyList())
    val favoriteProducts: StateFlow<List<ProductItem>> = _favoriteProducts.asStateFlow()

    fun moveToFavorites(productIds: Set<String>) {
        try {
            val toMove = _recentAnalyzed.value.filter { it.id in productIds }
            val existingIds = _favoriteProducts.value.map { it.id }.toSet()
            _favoriteProducts.value = _favoriteProducts.value + toMove.filter { it.id !in existingIds }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteAnalyzedRecords(productIds: Set<String>) {
        try {
            _recentAnalyzed.value = _recentAnalyzed.value.filterNot { it.id in productIds }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun removeFromFavorites(productId: String) {
        try {
            _favoriteProducts.value = _favoriteProducts.value.filterNot { it.id == productId }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun saveRecyclingActivity(centre: RecyclingCentre) = withContext(Dispatchers.IO) {
        val userId = currentUserId ?: return@withContext
        val entity = ProductEntity(
            id = "recycle_" + System.currentTimeMillis(),
            userId = userId,
            name = "Recycling @ ${centre.name}",
            category = "Recycling Activity",
            isRecyclingActivity = true,
            centreName = centre.name,
            centreAddress = centre.address,
            latitude = centre.latitude,
            longitude = centre.longitude,
            createdAt = System.currentTimeMillis()
        )
        try {
            productDao?.insertProduct(entity)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            productService.insertProductToSupabase(
                ProductRemoteDto(
                    id = entity.id,
                    userId = entity.userId,
                    name = entity.name,
                    category = entity.category,
                    isRecyclingActivity = true,
                    centreName = entity.centreName,
                    centreAddress = entity.centreAddress,
                    latitude = entity.latitude,
                    longitude = entity.longitude,
                    createdAt = entity.createdAt
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // --- Room & Supabase Synchronization Methods ---
    suspend fun saveUserToDb(user: UserEntity) = withContext(Dispatchers.IO) {
        try {
            userDao?.insertUser(user)
        } catch (e: Exception) {
            e.printStackTrace()
            throw Exception("Failed to save user to local database: ${e.message}")
        }
        
        try {
            userService.upsertUserToSupabase(
                UserRemoteDto(
                    id = user.id,
                    fullName = user.fullName,
                    email = user.email,
                    password = user.password,
                    profilePictureUrl = user.profilePictureUrl,
                    isGoogleAccount = user.isGoogleAccount,
                    ecoPoints = user.ecoPoints,
                    productsAnalysed = user.productsAnalysed,
                    challengesCompleted = user.challengesCompleted
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            // We might not want to throw here if we want to allow offline mode, 
            // but for registration, it's often critical.
        }
    }

    suspend fun insertProductToDb(product: ProductEntity) = withContext(Dispatchers.IO) {
        try {
            productDao?.insertProduct(product)
        } catch (e: Exception) {
            e.printStackTrace()
            // Not throwing here to allow the app to continue, but logging the error
        }
        try {
            productService.insertProductToSupabase(
                ProductRemoteDto(
                    id = product.id,
                    userId = product.userId,
                    name = product.name,
                    brand = product.brand,
                    category = product.category,
                    weightSize = product.weightSize,
                    packagingMaterial = product.packagingMaterial,
                    isOrganic = product.isOrganic,
                    ecoScore = product.ecoScore,
                    carbonFootprint = product.carbonFootprint,
                    recyclability = product.recyclability,
                    visualType = product.visualType,
                    createdAt = product.createdAt,
                    isFavorite = product.isFavorite
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun updateFavoriteStatusInDb(id: String, isFavorite: Boolean) = withContext(Dispatchers.IO) {
        val userId = currentUserId ?: return@withContext
        try {
            productDao?.updateFavoriteStatus(id, userId, isFavorite)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun deleteProductFromDb(id: String) = withContext(Dispatchers.IO) {
        val userId = currentUserId ?: return@withContext
        try {
            productDao?.deleteProductsByIds(listOf(id), userId)
            productService.deleteProductFromSupabase(id, userId)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun syncFromCloud() = withContext(Dispatchers.IO) {
        val userId = currentUserId ?: return@withContext
        try {
            // Sync Products
            val remoteProducts = productService.fetchProductsFromSupabase(userId)
            for (remote in remoteProducts) {
                val entity = ProductEntity(
                    id = remote.id,
                    userId = remote.userId,
                    name = remote.name,
                    brand = remote.brand,
                    category = remote.category,
                    weightSize = remote.weightSize,
                    packagingMaterial = remote.packagingMaterial,
                    isOrganic = remote.isOrganic,
                    ecoScore = remote.ecoScore,
                    carbonFootprint = remote.carbonFootprint,
                    recyclability = remote.recyclability,
                    visualType = remote.visualType,
                    createdAt = remote.createdAt,
                    isFavorite = remote.isFavorite
                )
                productDao?.insertProduct(entity)
            }

            // Sync Rewards
            val remoteRewards = rewardService.fetchRewardsFromSupabase(userId)
            for (remote in remoteRewards) {
                val entity = com.example.EcoWise.RoomDB.RewardEntity(
                    id = remote.id,
                    userId = remote.userId,
                    title = remote.title,
                    dateFormatted = remote.dateFormatted,
                    status = remote.status
                )
                rewardDao?.insertReward(entity)
            }
            // After sync, reload from local Room to update memory flows
            loadLocalRewards()

            // Sync Challenge Progress
            val remoteProgress = challengeProgressService.fetchProgressFromSupabase(userId)
            for (remote in remoteProgress) {
                val entity = ChallengeProgressEntity(
                    userId = remote.userId,
                    challengeId = remote.challengeId,
                    challengeTitle = remote.challengeTitle,
                    imagePath = remote.imagePath,
                    submissionDate = remote.submissionDate,
                    timestamp = remote.timestamp,
                    isSynced = true
                )
                challengeProgressDao?.insertProgress(entity)
            }
            
            // Re-fetch everything from local database to ensure internal state is exactly what's persisted
            loadLocalChallenges()
            
            // Re-fetch User Stats from Supabase to ensure in-sync
            val remoteUser = userService.fetchUserFromSupabase(_userEmail.value)
            if (remoteUser != null) {
                _userStats.value = UserEcoStats(
                    ecoPoints = remoteUser.ecoPoints,
                    productsAnalysed = remoteUser.productsAnalysed,
                    challengesCompleted = remoteUser.challengesCompleted
                )
                // Also update local UserEntity while preserving local password if remote is empty
                val localExisting = userDao?.getUserByEmail(_userEmail.value)
                val preservedPassword = remoteUser.password.ifEmpty { localExisting?.password ?: "" }
                val localUser = UserEntity(
                    id = remoteUser.id,
                    fullName = remoteUser.fullName,
                    email = remoteUser.email,
                    password = preservedPassword,
                    profilePictureUrl = remoteUser.profilePictureUrl,
                    isGoogleAccount = remoteUser.isGoogleAccount,
                    ecoPoints = remoteUser.ecoPoints,
                    productsAnalysed = remoteUser.productsAnalysed,
                    challengesCompleted = remoteUser.challengesCompleted
                )
                userDao?.insertUser(localUser)
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // 核心新增：分析商品并同时写入 Room 与 Supabase
    suspend fun analyzeAndSaveProduct(
        name: String,
        category: String,
        brand: String,
        weightSize: String,
        packagingMaterial: String,
        isOrganic: Boolean,
        ecoLabels: List<String>
    ): ProductItem = withContext(Dispatchers.IO) {
        val userId = currentUserId ?: ""
        val newProduct = analyzeNewProduct(
            name = name,
            category = category,
            brand = brand,
            weightSize = weightSize,
            packagingMaterial = packagingMaterial,
            isOrganic = isOrganic,
            ecoLabels = ecoLabels
        )

        val entity = ProductEntity(
            id = newProduct.id,
            userId = userId,
            name = newProduct.name,
            brand = newProduct.brand,
            category = newProduct.category,
            weightSize = newProduct.weightSize,
            packagingMaterial = newProduct.packagingMaterial,
            isOrganic = newProduct.isOrganic,
            ecoScore = newProduct.ecoScore,
            carbonFootprint = newProduct.carbonFootprint.name,
            recyclability = newProduct.recyclability.name,
            visualType = newProduct.visualType,
            createdAt = System.currentTimeMillis(),
            isFavorite = false
        )

        try {
            productDao?.insertProduct(entity)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            productService.insertProductToSupabase(
                ProductRemoteDto(
                    id = entity.id,
                    userId = entity.userId,
                    name = entity.name,
                    brand = entity.brand,
                    category = entity.category,
                    weightSize = entity.weightSize,
                    packagingMaterial = entity.packagingMaterial,
                    isOrganic = entity.isOrganic,
                    ecoScore = entity.ecoScore,
                    carbonFootprint = entity.carbonFootprint,
                    recyclability = entity.recyclability,
                    visualType = entity.visualType,
                    createdAt = entity.createdAt,
                    isFavorite = entity.isFavorite
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return@withContext newProduct
    }
}