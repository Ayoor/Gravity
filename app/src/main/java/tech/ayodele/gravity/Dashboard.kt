package tech.ayodele.gravity

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import tech.ayodele.gravity.databinding.ActivityDashboardBinding
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
//        binding.waterProgressIndicator.setProgress(70, true)
//        binding.stepsProgressIndicator.setProgress(30, true)
//
//        val caloryProgress= binding.caloryProgressIndicator
//        caloryProgress.setProgress(80, true)
//        caloryProgress.trackColor = ContextCompat.getColor(this, R.color.white);
//        caloryProgress.setIndicatorColor(ContextCompat.getColor(this, R.color.purple));
//
////        second progress indicator to increase the overall thickness
//
//        val caloryProgress2= binding.caloryProgressIndicator2
//        caloryProgress2.setProgress(80, true)
//        caloryProgress2.trackColor = ContextCompat.getColor(this, R.color.white);
//        caloryProgress2.setIndicatorColor(ContextCompat.getColor(this, R.color.purple));
//        binding.name.text = "Hello Ayodele"
//
//        val exerciseProgress= binding.exerciseProgressIndicator
//        exerciseProgress.setProgress(80, true)
//        exerciseProgress.trackColor = ContextCompat.getColor(this, R.color.white);
//        exerciseProgress.setIndicatorColor(ContextCompat.getColor(this, R.color.purple))
//
////        second progress indicator to increase the overall thickness
//        val exerciseProgress2= binding.exerciseProgressIndicator2
//        exerciseProgress2.setProgress(80, true)
//        exerciseProgress2.trackColor = ContextCompat.getColor(this, R.color.white);
//        exerciseProgress2.setIndicatorColor(ContextCompat.getColor(this, R.color.purple))

//        binding.weight.text = weight.toString()
//        binding.height.text = height.toString()


//        binding.BMI.text = " BMI: ${(calculateBMI(weight, height))}"

        val progressData = DashboardData(
            waterProgress = 70,
            stepsProgress = 30,
            caloryProgress = 80,
            caloryProgress2 = 80,
            exerciseProgress = 80,
            exerciseProgress2 = 80,
            userName = "Hello Ayodele"
        )
        val adapter = DashboardRecyclerAdapter(progressData)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

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
