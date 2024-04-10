package tech.ayodele.gravity

import DashboardViewHolder
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import tech.ayodele.gravity.databinding.DashboardItemsBinding
import java.text.DecimalFormat
import java.time.LocalDate

class DashboardRecyclerAdapter(private val dashboarddata: DashboardData) : RecyclerView.Adapter<DashboardViewHolder>() {
//   initialisation
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DashboardItemsBinding.inflate(inflater, parent, false)
        return DashboardViewHolder(binding.root)

    }

    override fun getItemCount(): Int {
        return 1
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val binding = holder.binding
        val context = binding.root.context

        // Set progress for each indicator
        binding.waterProgressIndicator.setProgress(dashboarddata.waterProgress, true)
        binding.stepsProgressIndicator.setProgress(dashboarddata.stepsProgress, true)
        binding.caloryProgressIndicator.setProgress(dashboarddata.caloryProgress, true)
        binding.caloryProgressIndicator2.setProgress(dashboarddata.caloryProgress2, true)
        binding.exerciseProgressIndicator.setProgress(dashboarddata.exerciseProgress, true)
        binding.exerciseProgressIndicator2.setProgress(dashboarddata.exerciseProgress2, true)

        // Set colors for indicators
        val whiteColor = ContextCompat.getColor(context, R.color.white)
        val purpleColor = ContextCompat.getColor(context, R.color.purple)
        binding.caloryProgressIndicator.trackColor = whiteColor
        binding.caloryProgressIndicator.setIndicatorColor(purpleColor)
        binding.caloryProgressIndicator2.trackColor = whiteColor
        binding.caloryProgressIndicator2.setIndicatorColor(purpleColor)
        binding.exerciseProgressIndicator.trackColor = whiteColor
        binding.exerciseProgressIndicator.setIndicatorColor(purpleColor)
        binding.exerciseProgressIndicator2.trackColor = whiteColor
        binding.exerciseProgressIndicator2.setIndicatorColor(purpleColor)

        // userdetails
        val weight = dashboarddata.userWeight
        val height = dashboarddata.userHeight
        val name = dashboarddata.name



        // Set dashboard details
        binding.name.text = name
        binding.name.text = dashboarddata.name
        binding.userWeight.text = "$weight Kg"
        binding.userHeight.text = "$height Cm"
        binding.userBMI.text = "${calculateBMI( weight,height)} BMI"
        binding.quote.text = dashboarddata.inspiration
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