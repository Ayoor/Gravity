package tech.ayodele.gravity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.Response
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import tech.ayodele.gravity.databinding.ActivityDashboardBinding
import tech.ayodele.gravity.databinding.DrawerHeaderBinding
import java.time.LocalDate


//this is the landing page of the app
class Dashboard : AppCompatActivity() {

    //   initialisation
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var prefs: SharedPreferences
    private var lastDate: LocalDate? = null

    @RequiresApi(Build.VERSION_CODES.O)
    private var currentDate = LocalDate.now()

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = getSharedPreferences("dashboardData", Context.MODE_PRIVATE)
        val lastInspoDate = prefs.getString("lastDate", "$currentDate")
        lastDate = LocalDate.parse(lastInspoDate)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
//        apiCall()
        getData()
        //set up element binding
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // get the user details object from the Signin Activity using Parcelables
        val userData: UserDetails? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("userData", UserDetails::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<UserDetails>("userData")
        }

        val weight = userData?.weight ?: 0
        val height = userData?.height ?: 0
        val name = userData?.name.toString()
        val firstName = firstName(name)
        val userID = userData?.id.toString()
        val email = userData?.email.toString()

        setProfileDetails(name, email)

//        binding.weight.text = weight.toString()
//        binding.height.text = height.toString()


//        binding.BMI.text = " BMI: ${(calculateBMI(weight, height))}"
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


//bottom nav
        val bottomNavigation = binding.bottomNavigation
        bottomNavigation.selectedItemId = R.id.home
        bottomNavigation.setOnItemSelectedListener(object :
            NavigationBarView.OnItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.home -> {
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

// side nav
        val drawerLayout = binding.drawerLayout
        val navDrawer = binding.sideNav
        val burgerMenu = binding.hamburgerMenu

// Set OnClickListener for burger menu icon to toggle the drawer
        burgerMenu.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }
//            user profile edit
        val editIcon = findViewById<ImageView>(R.id.edit)
        editIcon.setOnClickListener {
            Toast.makeText(this@Dashboard, "Edit", Toast.LENGTH_LONG).show()
        }


        // Handle navigation item clicks
        navDrawer.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nightmode -> {
                    Toast.makeText(this@Dashboard, "Night Mode", Toast.LENGTH_LONG).show()
                    true
                }

                R.id.stats -> {
                    Toast.makeText(this@Dashboard, "Stats", Toast.LENGTH_LONG).show()
                    true
                }

                R.id.logout -> {
                    logoutUser()
                    true
                }

                else -> false
            }
        }

    }

    //log user our

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

    private fun clearUserData() {
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

    private fun setProfileDetails(name: String, email: String) {
        // Get the NavigationView from your layout XML file
        val navigationView: NavigationView = findViewById(R.id.sideNav)

// Find the header view inside the NavigationView
        val headerView = navigationView.getHeaderView(0)

// Bind the header layout using data binding
        val headerBinding = DrawerHeaderBinding.bind(headerView)

        headerBinding.profileName.text = name
        headerBinding.profileEmail.text = email
    }


    private fun getData() {
        val call = RetrofitClient.apiService.fetchData()
        call.enqueue(object : Callback<CategoriesResponse> {
            override fun onResponse(
                call: Call<CategoriesResponse>,
                response: retrofit2.Response<CategoriesResponse>
            ) {
                if (response.isSuccessful) {
                    val categoriesResponse = response.body()
                    categoriesResponse?.let {
                        for (category in it.categories) {
                            Log.i("response", category.toString())
                        }
                    }
                }
            }

            override fun onFailure(call: Call<CategoriesResponse>, t: Throwable) {
                Toast.makeText(
                    this@Dashboard,
                    "An error occurred while Retrieving Data, please try again Later.",
                    Toast.LENGTH_LONG
                ).show()
                Log.i("response", t.toString())
            }
        })
    }


}




