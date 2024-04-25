package tech.ayodele.gravity

data class MealData(
    val meal: String,
    val description: String,
    val nutritionProfile: String,
    val nutritionFacts: NutritionFacts,
    val img: Int
)
data class  NutritionFacts(
    val calories: String,
    val fat: String,
    val carbs: String,
    val protein: String
)