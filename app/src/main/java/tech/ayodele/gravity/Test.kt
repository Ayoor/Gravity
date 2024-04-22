package tech.ayodele.gravity

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.DecimalFormat
import java.time.LocalDate

fun calculatePercentageDifference(oldValue: Double, newValue: Double): Double {
    return ((newValue - oldValue) / oldValue) * 100
}



    @RequiresApi(Build.VERSION_CODES.O)
    fun main() {
        val PD = calculatePercentageDifference(100.0, 70.0)
        println(PD)
        when {
            PD < 0 -> {
                val pdString = (-PD).toString() // Use the absolute value of PD
                println("$pdString less")
            }
            PD > 0 -> {
                println("$PD % more")
            }
            else -> {
                println("No change")
            }
        }
    }

