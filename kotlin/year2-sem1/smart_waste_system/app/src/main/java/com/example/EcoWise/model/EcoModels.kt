package com.example.EcoWise.model

enum class CarbonLevel {
    LOW, MEDIUM, HIGH
}

enum class RecyclabilityLevel {
    HIGH, MEDIUM, LOW
}

data class ImpactDetail(
    val title: String,
    val description: String,
    val iconType: String // "carbon", "recyclable", "water", "material", "energy"
)

data class RecyclingGuideInfo(
    val binName: String,
    val binColorHex: String,
    val instructions: String
)

data class EcoAlternative(
    val title: String,
    val description: String
)

data class ProductItem(
    val id: String,
    val name: String,
    val brand: String = "",
    val category: String = "General",
    val weightSize: String = "",
    val packagingMaterial: String = "Mixed Material",
    val isOrganic: Boolean = false,
    val ecoLabels: List<String> = emptyList(),
    val ecoScore: Int = 75,
    val carbonFootprint: CarbonLevel = CarbonLevel.LOW,
    val recyclability: RecyclabilityLevel = RecyclabilityLevel.HIGH,
    val impactDetails: List<ImpactDetail> = emptyList(),
    val recyclingGuide: RecyclingGuideInfo = RecyclingGuideInfo("Yellow Bin", "#F59E0B", "Clean and dry before disposal."),
    val alternative: EcoAlternative? = null,
    val visualType: String = "generic" // "oat_milk", "water_bottle", "coffee", "styrofoam", "tote", "toothbrush"
)

data class RecyclingCategoryGuide(
    val id: String,
    val title: String,
    val iconType: String,
    val summary: String,
    val acceptedItems: List<String>,
    val fullInstructions: String
)

data class RecyclingCentre(
    val id: String,
    val name: String,
    val rating: Double,
    val isOpen: Boolean,
    val distanceKm: Double,
    val acceptedMaterials: List<String>,
    val address: String,
    val latitude: Double,  // real GPS latitude, used by GoogleMap Marker
    val longitude: Double, // real GPS longitude, used by GoogleMap Marker
    val isSaved: Boolean = false
)

data class EcoChallenge(
    val id: String,
    val title: String,
    val description: String,
    val currentProgress: Int,
    val totalProgress: Int,
    val unit: String,
    val statusBadge: String, // "IN PROGRESS", "ACTIVE", "COMPLETED"
    val pointsReward: Int
)

data class ShopReward(
    val id: String,
    val title: String,
    val subtitle: String,
    val pointsCost: Int,
    val rewardsCategory: String? = null,
    val discountBadge: String? = null,
    val imageType: String = "bottle"
)

data class RedeemedReward(
    val id: String,
    val title: String,
    val dateFormatted: String,
    val status: String // "REDEEMED", "CLAIMED", "USED"
)

data class UserEcoStats(
    val ecoPoints: Int = 0,
    val avgEcoScore: Int = 0,
    val productsAnalysed: Int = 0,
    val savedCentresCount: Int = 0,
    val challengesCompleted: Int = 0,
    val categoryStats: List<Pair<String, Int>> = listOf(
        "Food & Beverages" to 0,
        "Beauty & Personal Care" to 0,
        "Household" to 0,
        "Clothing & Fashion" to 0,
        "Electronics" to 0
    ),
    val monthPoints: Int = 0,
    val monthProducts: Int = 0,
    val monthChallenges: Int = 0
)

data class BadgeItem(
    val id: String,
    val title: String,
    val description: String,
    val isUnlocked: Boolean = false,
    val progressText: String? = null
)

data class AboutUsSection(
    val id: String,
    val heading: String,
    val essay: String? = null
)