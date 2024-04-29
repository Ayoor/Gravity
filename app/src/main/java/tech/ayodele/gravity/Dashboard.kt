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
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import tech.ayodele.gravity.databinding.ActivityDashboardBinding
import java.time.LocalDate


//this is the landing page of the app
class Dashboard : AppCompatActivity() {

    //   initialisation
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var prefs: SharedPreferences
    private lateinit var userprefs: SharedPreferences
    private var lastDate: LocalDate? = null

    @RequiresApi(Build.VERSION_CODES.O)
    private var currentDate = LocalDate.now()

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = getSharedPreferences("dashboardData", Context.MODE_PRIVATE)
        userprefs = getSharedPreferences("saveData", Context.MODE_PRIVATE)
        val lastInspoDate = prefs.getString("lastDate", "$currentDate")
        lastDate = LocalDate.parse(lastInspoDate)
        overridePendingTransition(0, 0)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //set up element binding
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // get the user details object from the Signin Activity using Parcelables
        val userData = retrieveUserData(userprefs)

        val weight = userData.weight ?: 0
        val height = userData.height ?: 0
        val name = userData.name.toString()
        val firstName = firstName(name)
        val userID = userData.id.toString()
        val email = userData.email.toString()

        Log.i("post", userData.toString())


        val progressData = inspirations()?.let {
            DashboardData(
                name = "Hello $firstName!",
                userWeight = weight,
                userHeight = height,
                inspiration = it,
                email = email,
                userID = userID
            )
        }
        val adapter = progressData?.let { DashboardRecyclerAdapter(it, prefs) }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

//        save user metrics at end of day

// Bottom nav
        val bottomNavigation = binding.bottomNavigation
        bottomNavigation.selectedItemId = R.id.dashboard_home
//        setIconColor(bottomNavigation, R.id.dashboard_home, R.color.Primary)
        bottomNavigation.setOnItemSelectedListener(object :
            NavigationBarView.OnItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.dashboard_home -> {

                        return true
                    }
                    R.id.community -> {
                        startActivity(Intent(this@Dashboard, Community::class.java))
                        finish()
                        return true
                    }
                    R.id.liveHelp -> {
                        startActivity(Intent(this@Dashboard, LiveHelp::class.java))
                        finish()
                        return true
                    }
                    R.id.insightIcon -> {
                        startActivity(Intent(this@Dashboard, Insights::class.java))
                        finish()
                        return true
                    }
                    R.id.dieting -> {
                        startActivity(Intent(this@Dashboard, Diet::class.java))
                        finish()
                        return true
                    }
                    // Add more cases for other menu items as needed
                    else -> return false
                }
            }
        })




    }



    //retrieve user data
    private fun retrieveUserData(preferences: SharedPreferences): UserDetails {
        val userDataJson = preferences.getString("userdata", null)
        val gson = Gson()
        return gson.fromJson(userDataJson, UserDetails::class.java)
    }


    //log user out

    private fun logoutUser() {
        // Sign out the user from Firebase Authentication
        FirebaseAuth.getInstance().signOut()

        // Clear user data stored in the device memory
        clearUserData()

        // Redirect the user to the login screen
        val intent = Intent(this, Signin::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    fun clearUserData() {
        val prefs = getSharedPreferences("saveData", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.clear().apply()
    }

    //    get user first name
    private fun firstName(name: String): String {
        val indexOfSpace = name.indexOf(" ")
        return if (indexOfSpace != -1) {
            name.substring(0, indexOfSpace)
        } else {
            // If no space found, return the entire name as the first name
            name
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun inspirations(): String? {
        prefs = getSharedPreferences("dashboardData", Context.MODE_PRIVATE)
        return if (currentDate != lastDate) {
            lastDate = currentDate
            val currentInspo = Inspirations.getInspirations().random()
            val editor = prefs.edit()
            editor.putString("lastDate", currentDate.toString())
            editor.putString("lastInspo", currentInspo)
            editor.apply()

            currentInspo

            //Save Weeekly Metrics

        } else {
            val editor = prefs.edit()
            editor.putString("lastDate", currentDate.toString())
            editor.apply()
            prefs.getString(
                "lastInspo",
                "Every step forward, no matter how small, is a victory on the path to a healthier you"
            )

        }

    }







}




