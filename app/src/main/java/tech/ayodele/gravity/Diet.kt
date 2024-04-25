package tech.ayodele.gravity

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

class Diet : AppCompatActivity() {
    private lateinit var binding: ActivityDietBinding
    private lateinit var placesClient: PlacesClient
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val client = OkHttpClient()
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

        val recommendedMeals  = Meals.getMealDetails()
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

        getData()
    }




    private fun getData() {
        val call = RetrofitClient.apiService.fetchData()
        call.enqueue(object : retrofit2.Callback<RecipeInfo> {
            override fun onResponse(
                call: Call<RecipeInfo>,
                response: retrofit2.Response<RecipeInfo>
            ) {
                if (response.isSuccessful) {
                    Log.i("Recipe Label", "recipe.label")
                    val recipeInfo = response.body()
                    recipeInfo?.hits?.forEach { hit ->
                        val recipe = hit.recipe
                        Log.i("Recipe Label", recipe.label)
                        Log.i("Regular Image", recipe.image)
                        Log.i("Diet Labels", recipe.dietLabels.joinToString())
                        Log.i("Calories", recipe.calories.toString())
                    }
                } else {
                    // Handle unsuccessful response here
                    Log.i("Response", "Unsuccessful")
                }
            }



            override fun onFailure(call: Call<RecipeInfo>, t: Throwable) {
                Toast.makeText(
                    this@Diet,
                    "An error occurred while Retrieving Data, please try again Later.",
                    Toast.LENGTH_LONG
                ).show()
                Log.i("response", t.toString())
            }
        })
    }
}