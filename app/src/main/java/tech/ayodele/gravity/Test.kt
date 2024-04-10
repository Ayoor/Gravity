package tech.ayodele.gravity

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

val inspirations = Inspirations.getInspirations()
private var lastDate: LocalDate? = null

@RequiresApi(Build.VERSION_CODES.O)
fun getDailyInspiration(): String {
    val currentDate = LocalDate.now()
    return if (currentDate != lastDate) {
        lastDate = currentDate
        inspirations.random()
    } else {
        "Keep going! You've got this."
    }
}



@RequiresApi(Build.VERSION_CODES.O)
fun main() {
    val currentDate = LocalDate.now()
    println(currentDate)
}