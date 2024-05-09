package tech.ayodele.gravity

data class DashboardData(

    val name: String,
    val userWeight: Number,
    val userHeight: Int,
    val inspiration: String,
    val email: String,
    val userID: String
)
data class DashboardMetricData(
    var waterProgress: Int?,
    var stepsProgress: Int?,
    var caloryProgress: Int?,
    var exerciseProgress: Int?,
)

data class MetricsData(
    val water: Int,
    val steps: Int,
    val calories: Int,
    val exercise: Int,
)

data class WeeklyMetricsList(
    val weeklyML: MutableList<Int> = mutableListOf(),
    val weeklySteps: MutableList<Int> = mutableListOf(),
    val weeklyKcal: MutableList<Int> = mutableListOf(),
    val weeklyExercise: MutableList<Int> = mutableListOf(),
    val date: String = ""
)

data class SavedInsightPercentage(
    var water: Double,
    var steps: Double,
    var calories: Double,
    var exercise: Double,
)
