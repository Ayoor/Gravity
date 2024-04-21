package tech.ayodele.gravity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.navigation.NavigationBarView
import tech.ayodele.gravity.databinding.ActivityDietBinding

class Diet : AppCompatActivity() {
    private lateinit var binding: ActivityDietBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        enableEdgeToEdge()
        setContentView(R.layout.activity_diet)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //bottom nav
        binding = ActivityDietBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigation = binding.bottomNavigation
        bottomNavigation.selectedItemId = R.id.dieting
        bottomNavigation.setOnItemSelectedListener(object :
            NavigationBarView.OnItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.dashboard_home -> {
                        startActivity(Intent(this@Diet, Dashboard::class.java))
                        finish()
                        return true
                    }

                    R.id.community -> {
                        startActivity(Intent(this@Diet, Community::class.java))
                        finish()
                        return true
                    }

                    R.id.insightIcon -> {
                        startActivity(Intent(this@Diet, Insights::class.java))
                        finish()
                        return true
                    }

                    R.id.liveHelp -> {
                        startActivity(Intent(this@Diet, LiveHelp::class.java))
                        finish()
                        return true
                    }

                    R.id.dieting -> {
                        return true
                    }
                    // Add more cases for other menu items as needed
                    else -> return false
                }
            }
        })
    }
}