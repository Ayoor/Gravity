package tech.ayodele.gravity

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.DecimalFormat
import java.time.LocalDate

fun calculateBMI(weight: Int, height: Int): Double {
    val heightInMeters = height * 0.01
    val bmiDouble = (weight / (heightInMeters * heightInMeters)).toDouble()
    val decimalFormat = DecimalFormat("#.##")
    val bmi = decimalFormat.format(bmiDouble)
    return bmi.toDouble()
}


@RequiresApi(Build.VERSION_CODES.O)
fun main() {

    println(Inspirations.getInspirations().random())
}