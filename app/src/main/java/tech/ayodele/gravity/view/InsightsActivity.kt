package tech.ayodele.gravity.view


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.google.android.material.navigation.NavigationBarView
import com.google.gson.Gson
import tech.ayodele.gravity.viewmodel.InsightsViewModel
import tech.ayodele.gravity.R
import tech.ayodele.gravity.databinding.ActivityInsightsBinding
import tech.ayodele.gravity.model.UserDetails

class InsightsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInsightsBinding
    private lateinit var userprefs: SharedPreferences
    private lateinit var viewModel: InsightsViewModel

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
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
                        startActivity(Intent(this@InsightsActivity, Dashboard::class.java))
                        finish()
                        return true
                    }
                    R.id.community -> {
                        startActivity(Intent(this@InsightsActivity, Community::class.java))
                        finish()
                        return true
                    }
                    R.id.liveHelp -> {
                        startActivity(Intent(this@InsightsActivity, LiveHelp::class.java))
                        return true
                    }
                    R.id.insightIcon -> {
                        return true
                    }
                    R.id.dieting -> {
                        startActivity(Intent(this@InsightsActivity, Diet::class.java))
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
            startActivity(Intent(this@InsightsActivity, Dashboard::class.java))
            finish()
        }

        viewModel.fetchDataForLineChart(userdata.id.toString(),
            { data ->
                // Update UI with the chart data
                displayLineChart(data)
            },
            { error ->
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun displayLineChart(chartData: List<List<DataEntry>>) {
        val anyChartView: AnyChartView = findViewById(R.id.lineChart)
        val cartesian = AnyChart.line()

        cartesian.line(chartData[0]).name("Water")
        cartesian.line(chartData[1]).name("Steps")
        cartesian.line(chartData[2]).name("Calories")

        cartesian.title("Last 7 days ")
        cartesian.legend().enabled(true)

        anyChartView.setChart(cartesian)
    }
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



