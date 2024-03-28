package tech.ayodele.gravity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import tech.ayodele.gravity.databinding.ActivityDashboardBinding
import tech.ayodele.gravity.databinding.ActivitySigninBinding
import java.text.DecimalFormat
//this is the landing page of the app
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

        binding.name.text = name
        binding.weight.text = weight.toString()
        binding.height.text = height.toString()


        binding.BMI.text = " BMI: ${(calculateBMI(weight, height))}"

    }
    // this function calculates the BMI of the user
    private fun calculateBMI(weight: Int, height: Int): Double {
        val heightInMeters = height / 100
        val bmiDouble = (weight / (heightInMeters * heightInMeters)).toDouble()
        val decimalFormat = DecimalFormat("#.##")
        val bmi = decimalFormat.format(bmiDouble)
        return bmi.toDouble()
    }

}
