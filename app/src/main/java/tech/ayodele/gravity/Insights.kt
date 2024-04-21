package tech.ayodele.gravity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.navigation.NavigationBarView
import com.google.gson.Gson
import tech.ayodele.gravity.databinding.ActivityInsightsBinding

class Insights : AppCompatActivity() {
    private lateinit var binding: ActivityInsightsBinding
    private lateinit var userprefs: SharedPreferences
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        userprefs = getSharedPreferences("saveData", Context.MODE_PRIVATE)
        enableEdgeToEdge()
        setContentView(R.layout.activity_live_help)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        //bottom nav
        binding = ActivityInsightsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigation = binding.bottomNavigation
        bottomNavigation.selectedItemId = R.id.insightIcon
        bottomNavigation.setOnItemSelectedListener(object :
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
                    // Add more cases for other menu items as needed
                    else -> return false
                }
            }
        })

        //set username
        val userdata = retrieveUserData(userprefs)
        val firstname = userdata.name?.let { firstName(it) }
        binding.name.text = "Hi $firstname"

        binding.backArrow.setOnClickListener {
            startActivity(Intent(this@Insights, Dashboard::class.java))
            finish()
        }
    }
    private fun retrieveUserData(preferences: SharedPreferences): UserDetails {
        val userDataJson = preferences.getString("userdata", null)
        val gson = Gson()
        return gson.fromJson(userDataJson, UserDetails::class.java)
    }

    private fun firstName(name: String): String {
        val indexOfSpace = name.indexOf(" ")
        return if (indexOfSpace != -1) {
            name.substring(0, indexOfSpace)
        } else {
            // If no space found, return the entire name as the first name
            name
        }
    }



}