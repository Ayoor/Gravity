package tech.ayodele.gravity.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.google.firebase.database.*
import tech.ayodele.gravity.model.MetricsData
import tech.ayodele.gravity.model.SavedInsightPercentage
import tech.ayodele.gravity.model.WeeklyMetricsList
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class InsightsViewModel : ViewModel() {
    private val _weeklySum = MutableLiveData<MetricsData>()
    val weeklySum: LiveData<MetricsData>
        get() = _weeklySum

    private val _lineChartData = MutableLiveData<List<DataEntry>>()
    val lineChartData: LiveData<List<DataEntry>>
        get() = _lineChartData

    private val _insightPercentages = MutableLiveData<SavedInsightPercentage>()
    val insightPercentages: LiveData<SavedInsightPercentage>
        get() = _insightPercentages

    private val _waterInWeek = MutableLiveData<Int>()
    val waterInWeek: LiveData<Int>
        get() = _waterInWeek

    private val _kcalInWeek = MutableLiveData<Int>()
    val kcalInWeek: LiveData<Int>
        get() = _kcalInWeek

    private val _stepsInWeek = MutableLiveData<Int>()
    val stepsInWeek: LiveData<Int>
        get() = _stepsInWeek

    private val _exercisesInWeek = MutableLiveData<Int>()
    val exercisesInWeek: LiveData<Int>
        get() = _exercisesInWeek

    private val databaseReference: DatabaseReference by lazy {
        FirebaseDatabase.getInstance().getReference("Insights")
    }

    fun fetchDataForLineChart(userID: String, onDataLoaded: (List<List<DataEntry>>) -> Unit, onError: (String) -> Unit) {
        val waterList = mutableListOf<Int>()
        val stepsList = mutableListOf<Int>()
        val caloriesList = mutableListOf<Int>()

        val databaseReference = FirebaseDatabase.getInstance().getReference("Insights/$userID")

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val weeklyMetrics = dataSnapshot.getValue(WeeklyMetricsList::class.java)
                    if (weeklyMetrics != null) {
                        waterList.addAll(weeklyMetrics.weeklyML)
                        stepsList.addAll(weeklyMetrics.weeklySteps)
                        caloriesList.addAll(weeklyMetrics.weeklyKcal)

                        _waterInWeek.value = waterList.sum()
                        _kcalInWeek.value = caloriesList.sum()
                        _stepsInWeek.value = stepsList.sum()
                        _exercisesInWeek.value = weeklyMetrics.weeklyExercise.sum()

                        Log.i("newd", "${_waterInWeek.value}, ${_kcalInWeek.value}, ${_stepsInWeek.value}, ${_exercisesInWeek.value}")

                        val data = processDataForLineChart(waterList, stepsList, caloriesList)
                        onDataLoaded(data)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                onError("Failed to read value: ${error.message}")
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun processDataForLineChart(waterList: List<Int>, stepsList: List<Int>, caloriesList: List<Int>): List<List<DataEntry>> {
        val data1 = mutableListOf<DataEntry>()
        val data2 = mutableListOf<DataEntry>()
        val data3 = mutableListOf<DataEntry>()

        val currentdate = LocalDate.now()
        val xDates = mutableListOf<String>()

        for (i in waterList.indices) {
            val date = currentdate.minusDays(i.toLong())
            val xDate = date.format(DateTimeFormatter.ofPattern("dd-MMM"))
            xDates.add(xDate)
        }
        xDates.reverse()

        for (i in waterList.indices) {
            val xDate = xDates[i]

            data1.add(ValueDataEntry(xDate, waterList[i]))
            data2.add(ValueDataEntry(xDate, stepsList[i]))
            data3.add(ValueDataEntry(xDate, caloriesList[i]))
        }

        return listOf(data1, data2, data3)
    }
}
