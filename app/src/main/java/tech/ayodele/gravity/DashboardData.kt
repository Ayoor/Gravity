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

data class  WeeklyMetricsList(
    val weeklyML: MutableList<Int>,
    val weeklySteps: MutableList<Int>,
    val weeklyKcal: MutableList<Int>,
    val weeklyExercise: MutableList<Int>
)
