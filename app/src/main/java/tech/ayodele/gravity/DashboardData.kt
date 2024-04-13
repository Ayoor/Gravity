package tech.ayodele.gravity

data class DashboardData(

    val name: String,
    val userWeight: Int,
    val userHeight: Int,
    val inspiration: String,
)
data class DashboardMetricData(
    var waterProgress: Int?,
    var stepsProgress: Int?,
    var caloryProgress: Int?,
    var exerciseProgress: Int?,
)
