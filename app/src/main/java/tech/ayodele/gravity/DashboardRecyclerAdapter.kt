package tech.ayodele.gravity

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import tech.ayodele.gravity.databinding.DashboardItemsBinding
import java.text.DecimalFormat
import java.time.LocalDate
import kotlin.math.roundToInt

class DashboardRecyclerAdapter(
    private val dashboardData: DashboardData,
    private var prefs: SharedPreferences
) :
    RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>() {

    private var lastDate: String? = null

    @RequiresApi(Build.VERSION_CODES.O)
    private var currentDate = LocalDate.now()


    var waterProgress = 0
    var stepsProgress = 0
    var caloryProgress = 0
    var exerciseProgress = 0

    var totalCups = prefs.getInt("totalCups", 0)
    var totalSteps = prefs.getInt("totalSteps", 0)
    var totalCalories = prefs.getInt("totalCalories", 0)
    var totalExercise = prefs.getInt("totalExercise", 0)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DashboardItemsBinding.inflate(inflater, parent, false)
        return DashboardViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return 1 // Or return the actual number of items in your dataset
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val binding = holder.binding
        val context = binding.root.context

        lastDate = prefs.getString("lastMetricsDate", "Unavailable")

        Log.i("Date1", currentDate.toString())
        Log.i("Date2", lastDate.toString())
        if (currentDate.toString() != lastDate) {

            dailyDashboardRefresh(binding)
            updateMetricsChart(binding)
        } else {
            updateIndicatorData(binding, totalCups, totalCalories, totalExercise, totalSteps)
            updateMetricsChart(binding)

        }


        // Set progress for each indicator


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
        val weight = dashboardData.userWeight
        val height = dashboardData.userHeight
        val name = dashboardData.name
        val userBMI = calculateBMI(weight, height)


        // Set dashboard details
        binding.name.text = name
        binding.name.text = dashboardData.name
        binding.userWeight.text = "$weight Kg"
        binding.userHeight.text = "$height Cm"
        binding.userBMI.text = "$userBMI BMI"
        binding.BMIscale.text = BMIScale(userBMI)
        binding.quote.text = dashboardData.inspiration


        val appcontext = holder.itemView.context
        holder.binding.updateMetric.setOnClickListener {
            //trigger metrics alert
            metricsAlert(appcontext, binding)
        }

    }

    inner class DashboardViewHolder(val binding: DashboardItemsBinding) :
        RecyclerView.ViewHolder(binding.root)

    private fun updateMetricsChart(binding: DashboardItemsBinding) {
        binding.waterProgressIndicator.setProgress(waterProgress, true)
        binding.stepsProgressIndicator.setProgress(stepsProgress, true)
        binding.caloryProgressIndicator.setProgress(caloryProgress, true)
        binding.caloryProgressIndicator2.setProgress(caloryProgress, true)
        binding.exerciseProgressIndicator.setProgress(exerciseProgress, true)
        binding.exerciseProgressIndicator2.setProgress(exerciseProgress, true)
    }

    // this function calculates the BMI of the user

    private fun calculateBMI(weight: Int, height: Int): Double {
        val heightInMeters = height * 0.01
        val bmiDouble = (weight / (heightInMeters * heightInMeters)).toDouble()
        val decimalFormat = DecimalFormat("#.##")
        val bmi = decimalFormat.format(bmiDouble)
        return bmi.toDouble()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun metricsAlert(context: Context, binding: DashboardItemsBinding) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dashboar_update_alert, null)
        var activityCount = dialogView.findViewById<Spinner>(R.id.exerciseCount)
        var stepsCount = dialogView.findViewById<EditText>(R.id.stepsCount)
        var caloriesCount = dialogView.findViewById<EditText>(R.id.caloriesCount)
        var waterCupsCount = dialogView.findViewById<Spinner>(R.id.waterCupsSpinner)
        var buttonSave = dialogView.findViewById<Button>(R.id.buttonSave)
        var buttonCancel = dialogView.findViewById<Button>(R.id.buttonCancel)


        stepsCount.setText(totalSteps.toString())
        caloriesCount.setText(totalCalories.toString())

        // Apply spinner logic
        setupSpinners(context, waterCupsCount, activityCount)

        val alertDialogBuilder = AlertDialog.Builder(context)
            .setView(dialogView)

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()

        buttonSave.setOnClickListener {
            val waterCups = waterCupsCount.selectedItem.toString()
            val waterInML = cupsToMl(waterCups)
            val exerciseCount = activityCount.selectedItem.toString()
            val steps = stepsCount.text.toString()
            val calories = caloriesCount.text.toString()

            updateIndicatorData(
                binding,
                waterInML.toInt(),
                calories.toInt(),
                exerciseCount.toInt(),
                steps.toInt()
            )
            updateMetricsChart(binding)
            Toast.makeText(context, "Metrics Updated", Toast.LENGTH_SHORT).show()
            // Save data here (e.g., to SharedPreferences, database, etc.)
            alertDialog.dismiss()
        }

        buttonCancel.setOnClickListener {
            alertDialog.dismiss()
        }
    }

    private fun setupSpinners(context: Context, waterCupsCount: Spinner, activityCount: Spinner) {
        // Dropdown items

        val waterCups: List<Any> = listOf(0, 1, 2, 3, 4, 5, 6, 7)
        val noOfExercise: List<Any> = listOf(0, 1, 2, 3)
        // ArrayAdapters for spinner layout
        val adapter =
            ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, waterCups)
        val adapter2 =
            ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, noOfExercise)

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        waterCupsCount.adapter = adapter
        activityCount.adapter = adapter2

        //initial selection
        waterCupsCount.setSelection(waterCups.indexOf(totalCups/250))
        activityCount.setSelection(noOfExercise.indexOf(totalExercise))

        // Set an item selected listener for the spinner
        waterCupsCount.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        activityCount.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

    }

    private fun cupsToMl(cups: String): String {
        val ml = cups.toInt() * 250
        return ml.toString()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun updateIndicatorData(
        binding: DashboardItemsBinding,
        water: Int,
        kcal: Int,
        exercise: Int,
        steps: Int
    ) {

        // Set progress for each indicator
        waterProgress = ((water.toDouble() / 1500) * 100).roundToInt()
        caloryProgress = ((kcal.toDouble() / 1900) * 100).roundToInt()
        exerciseProgress = ((exercise.toDouble() / 3) * 100).roundToInt()
        stepsProgress = ((steps.toDouble() / 10000) * 100).roundToInt()

        when {
            stepsProgress > 100 -> stepsProgress = 100
            caloryProgress > 100 -> caloryProgress = 100
            waterProgress > 100 -> waterProgress = 100
        }

        binding.waterText.text = "$water of 1500ml"
        binding.caloryText.text = "$kcal kcal"
        binding.exerciseText.text = "$exercise Exercises"
        binding.stepsText.text = "$steps of 10000 steps"

        //save to device memory
        lastDate = currentDate.toString()
        val editor = prefs.edit()
        editor.putString("lastMetricsDate", lastDate)
        editor.putInt("totalCups", water)
        editor.putInt("totalCalories", kcal)
        editor.putInt("totalExercise", exercise)
        editor.putInt("totalSteps", steps)

        editor.apply()

    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun dailyDashboardRefresh(binding: DashboardItemsBinding) {


        waterProgress = 0
        caloryProgress = 0
        exerciseProgress = 0
        stepsProgress = 0

        updateIndicatorData(binding, waterProgress, caloryProgress, exerciseProgress, stepsProgress)

//        binding.waterText.text = "0 of 1500ml"
//        binding.caloryText.text = "0 kcal"
//        binding.exerciseText.text = "0 Exercises"
//        binding.stepsText.text = "0 of 10000 steps"


//        editor.apply()


    }

    private fun BMIScale(BMI: Double): String {
        var scale = ""

        when {
            BMI < 18.5 -> {
                scale = "Underweight"
            }

            BMI < 24.9 -> {
                scale = "Normal"
            }

            BMI < 29.9 -> {
                scale = "Overweight"
            }

            BMI >= 30 -> {
                scale = "Obese"
            }


        }
        return scale
    }
}