package tech.ayodele.gravity.view


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationBarView
import tech.ayodele.gravity.DashboardRecyclerAdapter
import tech.ayodele.gravity.R
import tech.ayodele.gravity.databinding.ActivityDashboardBinding
import tech.ayodele.gravity.model.UserDetails
import tech.ayodele.gravity.viewmodel.DashboardViewModel
import java.time.LocalDate

class Dashboard : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var viewModel: DashboardViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    private val currentDate = LocalDate.now()

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        overridePendingTransition(0, 0)
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[DashboardViewModel::class.java]

        // Configure UI
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Retrieve user data
        val userData: UserDetails = viewModel.retrieveUserData(this@Dashboard)
        val firstName = viewModel.firstName(userData.name.toString())

        Log.i("post", userData.toString())

        // Set up RecyclerView
        val viewModel = ViewModelProvider(this)[DashboardViewModel::class.java]
        viewModel.initSharedPreferences(this)
        val dashboardData = viewModel.retrieveDashboardData(this)

// Ensure prefs is properly initialized
        val prefs = getSharedPreferences("dashboardData", Context.MODE_PRIVATE)

        val adapter = dashboardData?.let { DashboardRecyclerAdapter(it, prefs) }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // Bottom navigation
        binding.bottomNavigation.selectedItemId = R.id.dashboard_home
        binding.bottomNavigation.setOnItemSelectedListener(object :
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
                        startActivity(Intent(this@Dashboard, InsightsActivity::class.java))
                        finish()
                        return true
                    }
                    R.id.dieting -> {
                        startActivity(Intent(this@Dashboard, Diet::class.java))
                        finish()
                        return true
                    }
                    else -> return false
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        // Clear any references to the ViewModel to prevent memory leaks

    }
}
