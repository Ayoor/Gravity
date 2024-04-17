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
import tech.ayodele.gravity.databinding.ActivityLiveHelpBinding

class LiveHelp : AppCompatActivity() {
    private lateinit var binding : ActivityLiveHelpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_live_help)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        //bottom nav
        binding = ActivityLiveHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigation = binding.bottomNavigation
        bottomNavigation.selectedItemId = R.id.liveHelp
        bottomNavigation.setOnItemSelectedListener(object :
            NavigationBarView.OnItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.home -> {
                        startActivity(Intent(this@LiveHelp, Dashboard::class.java))
                        finish()
                        return true
                    }

                    R.id.community -> {
                        startActivity(Intent(this@LiveHelp, Community::class.java))
                        finish()
                        return true
                    }

                    R.id.liveHelp -> {
                        return true
                    }

                    R.id.dieting -> {
                        startActivity(Intent(this@LiveHelp, Diet::class.java))
                        finish()
                        return true
                    }
                    // Add more cases for other menu items as needed
                    else -> return false
                }
            }
        })
    }
}