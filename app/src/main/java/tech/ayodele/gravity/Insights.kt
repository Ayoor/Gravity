package tech.ayodele.gravity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.navigation.NavigationBarView
import com.google.gson.Gson
import tech.ayodele.gravity.databinding.ActivityInsightsBinding
import java.time.LocalDate

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
        drawLineChart()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun drawLineChart(){

       val prefs = getSharedPreferences("weekPrefs", Context.MODE_PRIVATE)
        // Initialize the LineChart
        val lineChart: LineChart = findViewById(R.id.lineChart)
        val defaultMetrics = WeeklyMetricsList(
            mutableListOf(), mutableListOf(), mutableListOf(), mutableListOf()
        )
        val retrievedMetricsString = prefs.getString("currentWeeklyMetrics", null)
        val retrievedMetrics = Gson().fromJson(retrievedMetricsString, WeeklyMetricsList::class.java) ?: defaultMetrics

        val waterList = retrievedMetrics.weeklyML
        val stepsList = retrievedMetrics.weeklySteps
        val caloriesList = retrievedMetrics.weeklyKcal

        // Prepare your data
        val entries1 = mutableListOf<Entry>()
        val entries2 = mutableListOf<Entry>()
        val entries3 = mutableListOf<Entry>()

        // Populate the entries for each line
        for (i in 0 until waterList.size) {
            entries1.add(Entry(i.toFloat(), waterList[i].toFloat()))
            entries2.add(Entry(i.toFloat(), stepsList[i].toFloat()))
            entries3.add(Entry(i.toFloat(), caloriesList[i].toFloat()))
        }

// Create LineDataSet for each line
        val dataSet1 = LineDataSet(entries1, "Water(ml)")
        val dataSet2 = LineDataSet(entries2, "Steps")
        val dataSet3 = LineDataSet(entries3, "Calories")

// Customize appearance for each dataset if needed
// Example: Changing line color
        dataSet1.color = Color.BLUE
        dataSet2.color = Color.RED
        dataSet3.color = Color.BLACK

// Add LineDataSet objects to LineData
        val lineData = LineData(dataSet1, dataSet2, dataSet3)

// Configure the chart
        lineChart.apply {
            description.isEnabled = false // Disable chart description
            xAxis.position = XAxis.XAxisPosition.BOTTOM // Position of X-axis
            axisRight.isEnabled = false // Disable right Y-axis
            legend.isEnabled = true // Enable legend
        }
        // Set custom labels for X-axis
//        val labels = listOf("7DAGO", "6DAGO", "5DAGO", "4DAGO", "3DAGO", "Yesterday", "Today")
        val labels = getLabelArray()
        lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
// Set data to the chart
        lineChart.data = lineData

// Refresh the chart
        lineChart.invalidate()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getLabelArray(): List<String> {
        val currentdate = LocalDate.now()
        val datelist= mutableListOf<String>()
        for (i in 0 until 7){
            val date = currentdate.minusDays(i.toLong())
            datelist.add("${date.dayOfMonth}-${date.month}")
//            Log.i("date", "${date.dayOfMonth}-${date.month}")
        }

        return datelist.reversed()
    }
    private fun observeViewModel() {
        viewModel.weeklySum.observe(this, Observer { weeklySum ->
            updateUIWithWeeklySum(weeklySum)
        })

        viewModel.insightPercentages.observe(this, Observer { insightPercentages ->
            updateUIWithInsightPercentages(insightPercentages)
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

        // Update UI with insight percentages
        updateInsightPercentage(binding.waterInsightPercentage, savedInsightPercentages.water)
        updateInsightPercentage(binding.stepsInsightPercentage, savedInsightPercentages.steps)
        updateInsightPercentage(binding.caloryInsightPercentage, savedInsightPercentages.calories)
        updateInsightPercentage(binding.exerciseInsightPercentage, savedInsightPercentages.exercise)

        // Save updated data to SharedPreferences
        currentlyWeeklySum = MetricsData(waterForTheWeek, stepsForTheWeek, caloriesForTheWeek, exerciseForTheWeek)
        val editor = prefs.edit()
        editor.putString("currentWeeklySum", Gson().toJson(currentlyWeeklySum))
        editor.putString("savedInsightPercentages", Gson().toJson(savedInsightPercentages))
        editor.putString("lastPercentageDate", currentDate.toString())
        editor.apply()
    }

    @SuppressLint("SetTextI18n")
    private fun updateInsightPercentage(textView: TextView, percentage: Double) {
        when {
            percentage < 0 -> {
                textView.setTextColor(ContextCompat.getColor(textView.context, R.color.red))
                textView.text = "${percentage * -1}%"
                textView.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.bottom_left_arrow,  // left drawable
                    0,                              // top drawable
                    0,                              // right drawable
                    0                               // bottom drawable
                )
            }
            percentage > 0 -> {
                textView.setTextColor(ContextCompat.getColor(textView.context, R.color.green))
                textView.text = "$percentage%"
                textView.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.top_right_arrow,  // left drawable
                    0,                              // top drawable
                    0,                              // right drawable
                    0                               // bottom drawable
                )
            }
            else -> {
                textView.text = "0%"
            }
        }
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

    @SuppressLint("SetTextI18n")
    private fun updateUIWithInsightPercentages(insightPercentages: SavedInsightPercentage) {
        updateInsightPercentage(binding.waterInsightPercentage, insightPercentages.water)
        updateInsightPercentage(binding.stepsInsightPercentage, insightPercentages.steps)
        updateInsightPercentage(binding.caloryInsightPercentage, insightPercentages.calories)
        updateInsightPercentage(binding.exerciseInsightPercentage, insightPercentages.exercise)
    }


}
