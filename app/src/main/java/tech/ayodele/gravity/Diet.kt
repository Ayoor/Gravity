package tech.ayodele.gravity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.material.navigation.NavigationBarView
import okhttp3.OkHttpClient
import retrofit2.Call
import tech.ayodele.gravity.databinding.ActivityDietBinding

open class Diet : AppCompatActivity() {
    private lateinit var binding: ActivityDietBinding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(0, 0)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //set up binding
        binding = ActivityDietBinding.inflate(layoutInflater)
        setContentView(binding.root)
//adapter

        val recommendedMeals  = Meals.getMealDetails(this)
        val userType = Meals.getUserType(this)
        val userTyp= when(userType) {
            "Active User" -> "AU"
            "Passive User" -> "PU"
            "Sedentary User" -> "SU"
        else ->{
            "DU"
        }
        }

        binding.dietTitle.text = "Your Meal Suggestions ($userTyp)"
        val adapter = DietRecyclerAdapter(recommendedMeals, this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter


        //bottom nav
        val bottomNavigation = binding.bottomNavigation
        bottomNavigation.selectedItemId = R.id.dieting
        bottomNavigation.setOnItemSelectedListener(object :
            NavigationBarView.OnItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.dashboard_home -> {
                        startActivity(Intent(this@Diet, Dashboard::class.java))
                        return true

                    }

                    R.id.community -> {
                        startActivity(Intent(this@Diet, Community::class.java))
                        finish()
                        return true
                    }

                    R.id.liveHelp -> {
                        startActivity(Intent(this@Diet, LiveHelp::class.java))
                        finish()
                        return true
                    }

                    R.id.insightIcon -> {
                        startActivity(Intent(this@Diet, Insights::class.java))
                        finish()
                        return true
                    }

                    R.id.dieting -> {

                        return true
                    }

                    else -> return false
                }
            }
        })


    }







}