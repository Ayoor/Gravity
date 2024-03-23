package tech.ayodele.gravity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import tech.ayodele.gravity.databinding.ActivityDashboardBinding
import tech.ayodele.gravity.databinding.ActivitySigninBinding

class Dashboard : AppCompatActivity() {



    private lateinit var binding: ActivityDashboardBinding
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val userData = intent.getParcelableExtra<UserDetails>("userData")


        binding.name.text = userData?.name
        binding.weight.text = userData?.weight.toString()
        binding.height.text = userData?.height.toString()


    }

}
