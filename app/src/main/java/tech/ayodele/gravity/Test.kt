package tech.ayodele.gravity

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.DecimalFormat
import java.time.LocalDate

fun main (){
val userSurveyResponse = listOf ("Very active", "Very Easily", "Regularly", "Very Comfortable", "No",
    "Balanced and healthy", "No", "No significant impact")
println(getUserType(userSurveyResponse))
}

fun getUserType(userSurveyResponse: List<String>): String {
    var activeUser = 0
    var passiveUser = 0
    var sedentaryUser = 0
    var userType = ""

    when (userSurveyResponse[0]) {
        "Sedentary" -> sedentaryUser++
        "Lightly active" -> passiveUser++
        "Moderately active" -> passiveUser++
        "Very active" -> activeUser++
    }

    when (userSurveyResponse[1]) {
        "Very Easily" -> activeUser++
        "Easily" -> passiveUser++
        "Normal" -> passiveUser++
        "Difficult" -> sedentaryUser++
        "Very Difficult" -> sedentaryUser++
    }

    when (userSurveyResponse[2]) {
        "Very Comfortable" -> activeUser++
        "Somewhat Comfortable" -> passiveUser++
        "Neutral" -> passiveUser++
        "Somewhat Uncomfortable" -> sedentaryUser++
        "Very Uncomfortable" -> sedentaryUser++
    }

    if (userSurveyResponse[3] == "Yes") {
        sedentaryUser++
    }

    when (userSurveyResponse[4]) {
        "Balanced and healthy" -> activeUser++
        "Excessive consumption of processed foods" -> passiveUser++
        "Regular consumption of energy-dense foods" -> passiveUser++
        "Eating disorders or irregular eating patterns" -> sedentaryUser++
    }

    if (userSurveyResponse[5] == "Yes") {
        sedentaryUser++
    }

    when (userSurveyResponse[6]) {
        "Regularly (several times a week)" -> activeUser++
        "Occasionally (once or twice a week)" -> passiveUser++
        "Rarely (less than once a week)" -> passiveUser++
        "Never" -> sedentaryUser++
    }

    if (userSurveyResponse[7] == "Yes") {
        sedentaryUser++
    }

    // Determine user type based on the counts
    userType = if (activeUser > passiveUser && activeUser > sedentaryUser) {
        "Active User"
    } else if (passiveUser > activeUser && passiveUser > sedentaryUser) {
        "Passive User"
    } else {
        "Sedentary User"
    }

    return userType
}
