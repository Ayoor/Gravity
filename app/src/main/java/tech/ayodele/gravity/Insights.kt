package tech.ayodele.gravity

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import tech.ayodele.gravity.databinding.ActivityInsightsBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Insights : AppCompatActivity() {
    private lateinit var binding: ActivityInsightsBinding
    private lateinit var userprefs: SharedPreferences
    private lateinit var viewModel: InsightsViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        userprefs = getSharedPreferences("saveData", Context.MODE_PRIVATE)
        enableEdgeToEdge()
        binding = ActivityInsightsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[InsightsViewModel::class.java]

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.bottomNavigation.selectedItemId = R.id.insightIcon
        binding.bottomNavigation.setOnItemSelectedListener(object :
            NavigationBarView.OnItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.dashboard_home -> {
                        startActivity(Intent(this@Insights, Dashboard::class.java))
                        finish()
                        return true
                    }
                    R.id.community -> {
                        startActivity(Intent(this@Insights, Community::class.java))
                        finish()
                        return true
                    }
                    R.id.liveHelp -> {
                        startActivity(Intent(this@Insights, LiveHelp::class.java))
                        return true
                    }
                    R.id.insightIcon -> {
                        return true
                    }
                    R.id.dieting -> {
                        startActivity(Intent(this@Insights, Diet::class.java))
                        finish()
                        return true
                    }
                    else -> return false
                }
            }
        })

        val userdata = retrieveUserData(userprefs)
        val firstname = firstName(userdata.name ?: "")
        binding.name.text = "Hi $firstname"

        binding.backArrow.setOnClickListener {
            startActivity(Intent(this@Insights, Dashboard::class.java))
            finish()
        }

        observeViewModel()
        updateWeeklyInsights(binding)
//        drawLineChart()
        lineChart(userdata.id?:"User")
    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun lineChart(userID:String) {

        var waterList = mutableListOf<Int>()
        var stepsList = mutableListOf<Int>()
        var caloriesList = mutableListOf<Int>()

        val databaseReference = FirebaseDatabase.getInstance().getReference("Insights/$userID")

// Add a ValueEventListener to listen for changes in the data
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Check if the dataSnapshot exists and contains data
                if (dataSnapshot.exists()) {
                    val weeklyMetrics = dataSnapshot.getValue(WeeklyMetricsList::class.java)
                    // Now you can use the retrieved weeklyMetrics object
                    if (weeklyMetrics != null) {
                        waterList = weeklyMetrics.weeklyML
                        stepsList = weeklyMetrics.weeklySteps
                        caloriesList = weeklyMetrics.weeklyKcal

                    }

//--------------------------------------- use data to update chart-----------------------
                    // Find the AnyChartView in your layout
                    val anyChartView: AnyChartView = findViewById(R.id.lineChart)

                    // Initialize AnyChart
                    val cartesian = AnyChart.line()

                    // Prepare data for the line chart
                    val data1: MutableList<DataEntry> = mutableListOf()
                    val data2: MutableList<DataEntry> = mutableListOf()
                    val data3: MutableList<DataEntry> = mutableListOf()

                    // Compile x dates list and reverse it
                    val currentdate = LocalDate.now()
                    val xDates = mutableListOf<String>()

//                    ----------- x axis date --------------------
                    for (i in waterList.indices) {
                        val date = currentdate.minusDays(i.toLong())
                        val xDate = date.format(DateTimeFormatter.ofPattern("dd-MMM"))
                        xDates.add(xDate)
                    }
                    xDates.reverse()

                    // Generate data for demonstration
                    for (i in waterList.indices) {
                        val xDate = xDates[i]

                        data1.add(ValueDataEntry(xDate, waterList[i]))
                        data2.add(ValueDataEntry(xDate, stepsList[i]))
                        data3.add(ValueDataEntry(xDate, caloriesList[i]))
                    }

                    // Add data to the line chart
                    cartesian.line(data1).name("Water")
                    cartesian.line(data2).name("Steps")
                    cartesian.line(data3).name("Calories")

                    // Set title for the line chart
                    cartesian.title("Last 7 days ")

                    // Add legend to the chart
                    cartesian.legend().enabled(true)

                    // Render the line chart
                    anyChartView.setChart(cartesian)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Failed to read value.", error.toException())
                Toast.makeText(this@Insights, "Failed to retrieve data from Database", Toast.LENGTH_SHORT).show()
            }
        })





    }




    private fun observeViewModel() {
        viewModel.weeklySum.observe(this, Observer { weeklySum ->
            updateUIWithWeeklySum(weeklySum)
        })

        viewModel.insightPercentages.observe(this, Observer { insightPercentages ->
//            updateUIWithInsightPercentages(insightPercentages)
        })
    }

    private fun retrieveUserData(preferences: SharedPreferences): UserDetails {
        val userDataJson = preferences.getString("userdata", null)
        return Gson().fromJson(userDataJson, UserDetails::class.java) ?: UserDetails("", "")
    }

    private fun firstName(name: String): String {
        val indexOfSpace = name.indexOf(" ")
        return if (indexOfSpace != -1) {
            name.substring(0, indexOfSpace)
        } else {
            name
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun updateWeeklyInsights(binding: ActivityInsightsBinding) {
        val defaultMetrics = WeeklyMetricsList(
            mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf()
        )
        val prefs = getSharedPreferences("weekPrefs", Context.MODE_PRIVATE)

        // Get the current weekly sum
        val currentlyWeeklySumJson = prefs.getString("currentWeeklySum", null)
        var currentlyWeeklySum = Gson().fromJson(currentlyWeeklySumJson, MetricsData::class.java)
        if (currentlyWeeklySum == null) {
            currentlyWeeklySum = MetricsData(0, 0, 0, 0)
        }

        // Get the retrieved weekly metrics
        val retrievedMetricsString = prefs.getString("currentWeeklyMetrics", null)
        val retrievedMetrics = Gson().fromJson(retrievedMetricsString, WeeklyMetricsList::class.java) ?: defaultMetrics

        // Calculate the sum of daily metrics for the week
        val waterForTheWeek = sumDailyMetrics(retrievedMetrics.weeklyML)
        val stepsForTheWeek = sumDailyMetrics(retrievedMetrics.weeklySteps)
        val caloriesForTheWeek = sumDailyMetrics(retrievedMetrics.weeklyKcal)
        val exerciseForTheWeek = sumDailyMetrics(retrievedMetrics.weeklyExercise)

        // Update UI with weekly metrics
        binding.waterText.text = "$waterForTheWeek ml"
        binding.stepsIndicatorText.text = "$stepsForTheWeek"
        binding.caloryIndicatorText.text = "$caloriesForTheWeek kcal"
        binding.exerciseText.text = "$exerciseForTheWeek"

        // Get the saved insight percentages
        val savedJson = prefs.getString("savedInsightPercentages", null)
        var savedInsightPercentages = Gson().fromJson(savedJson, SavedInsightPercentage::class.java)
        if (savedInsightPercentages == null) {
            savedInsightPercentages = SavedInsightPercentage(0.0, 0.0, 0.0, 0.0)
        }

        // Calculate percentage difference if changes were made today
        val currentDate = LocalDate.now()
        val lastMetricsDate = prefs.getString("lastPercentageDate", null)
        val isChange = prefs.getBoolean("isChange", false)
        if (lastMetricsDate == currentDate.toString() && isChange) {
            savedInsightPercentages.water = calculatePercentageDifference(currentlyWeeklySum.water, waterForTheWeek)
            savedInsightPercentages.steps = calculatePercentageDifference(currentlyWeeklySum.steps, stepsForTheWeek)
            savedInsightPercentages.calories = calculatePercentageDifference(currentlyWeeklySum.calories, caloriesForTheWeek)
            savedInsightPercentages.exercise = calculatePercentageDifference(currentlyWeeklySum.exercise, exerciseForTheWeek)
        }



        // Save updated data to SharedPreferences
        currentlyWeeklySum = MetricsData(waterForTheWeek, stepsForTheWeek, caloriesForTheWeek, exerciseForTheWeek)
        val editor = prefs.edit()
        editor.putString("currentWeeklySum", Gson().toJson(currentlyWeeklySum))
        editor.putString("savedInsightPercentages", Gson().toJson(savedInsightPercentages))
        editor.putString("lastPercentageDate", currentDate.toString())
        editor.apply()
    }




    private fun sumDailyMetrics(metricsList: MutableList<Int>): Int {
        return metricsList.sum()
    }

    private fun calculatePercentageDifference(oldValue: Int, newValue: Int): Double {
        return if (oldValue == 0) {
            0.0
        } else {
            val percentage = ((newValue.toDouble() - oldValue.toDouble()) / oldValue.toDouble()) * 100
            String.format("%.2f", percentage).toDouble()
        }
    }


    @SuppressLint("SetTextI18n")
    private fun updateUIWithWeeklySum(weeklySum: MetricsData) {
        binding.waterText.text = "${weeklySum.water} ml"
        binding.stepsIndicatorText.text = "${weeklySum.steps}"
        binding.caloryIndicatorText.text = "${weeklySum.calories} kcal"
        binding.exerciseText.text = "${weeklySum.exercise}"
    }


}
