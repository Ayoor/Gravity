package tech.ayodele.gravity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.text.InputType
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import tech.ayodele.gravity.databinding.DashboardItemsBinding
import tech.ayodele.gravity.model.DashboardData
import tech.ayodele.gravity.model.MetricsData
import tech.ayodele.gravity.model.WeeklyMetricsList
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

class DashboardRecyclerAdapter(
    private val dashboardData: DashboardData,
    private var prefs: SharedPreferences
) :
    RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>() {



    private var lastDate: String? = null

    @RequiresApi(Build.VERSION_CODES.O)
    private var currentDate = LocalDate.now()

    private var waterProgress = 0
    private var stepsProgress = 0
    private var caloryProgress = 0
    private var exerciseProgress = 0

    private var totalMills = prefs.getInt("totalCups", 0)
    private var totalSteps = prefs.getInt("totalSteps", 0)
    private var totalCalories = prefs.getInt("totalCalories", 0)
    private var totalExercise = prefs.getInt("totalExercise", 0)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DashboardItemsBinding.inflate(inflater, parent, false)
        return DashboardViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return 1 // Or return the actual number of items in your dataset
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val binding = holder.binding
        val context = binding.root.context

        // userdetails

        val name = dashboardData.name

        val userID = dashboardData.userID

        lastDate = prefs.getString("lastMetricsDate", "Unavailable")

        if (currentDate.toString() != lastDate) { // it is a new day, use new data
//            save current Metrics
//
            //reset the metrics
            dailyDashboardReset(binding, holder)
            updateMetricsChart(binding)

        } else { // same day
            //use saved data
            updateIndicatorData(binding, totalMills, totalCalories, totalExercise, totalSteps, holder)
            updateMetricsChart(binding)

        }

//            saveDailyMetricsData(holder,MetricsData(totalMills,totalSteps, totalCalories,totalExercise), userID)





        // Set progress for each indicator


        // Set colors for indicators
        val whiteColor = ContextCompat.getColor(context, R.color.white)
        val indicatorColor = ContextCompat.getColor(context, R.color.navyBlue)
        binding.caloryProgressIndicator.trackColor = whiteColor
        binding.caloryProgressIndicator.setIndicatorColor(indicatorColor)
        binding.caloryProgressIndicator2.trackColor = whiteColor
        binding.caloryProgressIndicator2.setIndicatorColor(indicatorColor)
        binding.exerciseProgressIndicator.trackColor = whiteColor
        binding.exerciseProgressIndicator.setIndicatorColor(indicatorColor)
        binding.exerciseProgressIndicator2.trackColor = whiteColor
        binding.exerciseProgressIndicator2.setIndicatorColor(indicatorColor)

        binding.waterProgressIndicator.setIndicatorColor(indicatorColor)
        binding.waterProgressIndicator.trackColor = whiteColor
        binding.stepsProgressIndicator.setIndicatorColor(indicatorColor)
        binding.stepsProgressIndicator.trackColor = whiteColor



        // Set dashboard details
        binding.name.text = name
        binding.quote.text = dashboardData.inspiration
        binding.userHeight.text = dashboardData.userHeight.toString()

        refreshWeightData(userID, context, binding)

        val appcontext = holder.itemView.context

        holder.binding.logout.setOnClickListener {
            // Sign out the user from Firebase Authentication
            FirebaseAuth.getInstance().signOut()

            // Access the SharedPreferences from the context of the item view
            val prefs = holder.itemView.context.getSharedPreferences("saveData", Context.MODE_PRIVATE)
            prefs.edit().clear().apply()

            // Redirect the user to the login screen
            val intent = Intent(holder.itemView.context, Signin::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            holder.itemView.context.startActivity(intent)
            Toast.makeText(holder.itemView.context, "User Signed Out", Toast.LENGTH_SHORT).show()
        }



//        update metrics
        holder.binding.updateMetric.setOnClickListener {
            //trigger metrics alert
            metricsAlert(appcontext, binding, holder, userID)
        }

//         disclaimer
        holder.binding.info.setOnClickListener {
            disclaimer(appcontext)
        }

//        edit weight

        holder.binding.weightEditIcon.setOnClickListener {
            editWeight(binding)
        }

        holder.binding.saveWeight.setOnClickListener {

            updateWeight(binding, context, userID)
        }


    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveDailyMetricsData(holder: DashboardViewHolder, userMetrics: MetricsData, userID: String) {


            val prefs = holder.itemView.context.getSharedPreferences("weekPrefs", Context.MODE_PRIVATE)
            val editor = prefs.edit()
            val defaultMetrics = WeeklyMetricsList(
                mutableListOf(),
                mutableListOf(),
                mutableListOf(),
                mutableListOf()
            )
        val lastMetricsDate = prefs.getString("lastMetricsDate", null)

            val retrievedMetricsString = prefs.getString("currentWeeklyMetrics", null)
            val retrievedMetrics = if (retrievedMetricsString != null) {
                Gson().fromJson(retrievedMetricsString, WeeklyMetricsList::class.java)
            } else {
                defaultMetrics
            }

//            val currentMetrics = Gson().fromJson(retrievedMetrics, WeeklyMetricsList::class.java)
            val waterWeeklyData = retrievedMetrics.weeklyML
            val exerciseWeeklyData = retrievedMetrics.weeklyExercise
            val caloryWeeklyData = retrievedMetrics.weeklyKcal
            val stepsWeeklyData = retrievedMetrics.weeklySteps


        if (currentDate.toString() != lastMetricsDate) {
//            it is a new day

            //use one to check for all
            when {
                waterWeeklyData.size == 7 -> {
                    //remove the oldest
                    waterWeeklyData.removeAt(0)
                    exerciseWeeklyData.removeAt(0)
                    caloryWeeklyData.removeAt(0)
                    stepsWeeklyData.removeAt(0)
                }

            }
            waterWeeklyData.add(0)
            exerciseWeeklyData.add(0)
            caloryWeeklyData.add(0)
            stepsWeeklyData.add(0)
        }
             else{ // same day

                 waterWeeklyData[waterWeeklyData.size-1] = userMetrics.water
                 exerciseWeeklyData[exerciseWeeklyData.size-1] = userMetrics.exercise
                 caloryWeeklyData[caloryWeeklyData.size-1] = userMetrics.calories
                 stepsWeeklyData[stepsWeeklyData.size-1] = userMetrics.steps

                 editor.putBoolean("isChange", true)

                 }

//save to database
        val database = FirebaseDatabase.getInstance()
        val insightRef = database.getReference("Insights").child(userID)
        val date = LocalDate.now()
        val xDate = date.format(DateTimeFormatter.ofPattern("dd-MMM"))
        // Save weight to Firebase Realtime Database
        val weeklyMetrics = WeeklyMetricsList(
            waterWeeklyData,
            stepsWeeklyData,
            caloryWeeklyData,
            exerciseWeeklyData,
            xDate

        )
        insightRef.setValue(weeklyMetrics)

            //save to memory

            val json = Gson().toJson(weeklyMetrics)
            editor.putString("currentWeeklyMetrics", json)
            editor.putString("lastMetricsDate", currentDate.toString())

            editor.apply()
        }



    inner class DashboardViewHolder(val binding: DashboardItemsBinding) :
        RecyclerView.ViewHolder(binding.root)

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun updateWeight(binding: DashboardItemsBinding, context: Context, userID: String) {
        val weightET = binding.editWeight
        val weightInput = binding.userWeight
        weightET.visibility = View.GONE
        weightInput.visibility = View.VISIBLE
        binding.saveWeight.visibility = View.GONE
        binding.weightEditIcon.visibility = View.VISIBLE



        when {
            weightET.text.toString().isEmpty() -> {
                Toast.makeText(context, "Invalid Weight!, No changes Made", Toast.LENGTH_LONG)
                    .show()
            }

            weightET.text.toString().toDouble() < 20 -> {
                Toast.makeText(context, "Please enter a weight above 20kg", Toast.LENGTH_LONG)
                    .show()
            }

            else -> {
                weightInput.text = weightET.text.toString()
                val userHeight = (binding.userHeight.text.toString()).toInt()
                val userWeight = weightInput.text.toString().toDouble()
                val decimalFormat = DecimalFormat("#.##")
                 val userWeightF = decimalFormat.format(userWeight).toDouble()



                //save to database
                val database = FirebaseDatabase.getInstance()
                val weightRef = database.getReference("Weight Data/$userID")
                val dateTime = formattedDate(currentDate)
                // Save weight to Firebase Realtime Database
                val weightData = WeightData(
                    dateTime,
                    userWeightF, userHeight
                )
                weightRef.setValue(weightData)

                refreshWeightData(userID, context, binding)

                Toast.makeText(context, "Weight Updated", Toast.LENGTH_SHORT).show()
            }
        }


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private  fun formattedDate(date: LocalDate): String {
        // Convert LocalDate to LocalDateTime
        val localDateTime = LocalDateTime.of(date, LocalTime.now())

        // Convert LocalDateTime to Date
        val dateObj = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())

        // Define date and time format
        val dateFormat = SimpleDateFormat("dd-MM-yy", Locale.getDefault())
        val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

        // Format the date and time
        val formattedDate = dateFormat.format(dateObj)
        val formattedTime = timeFormat.format(dateObj)

        // Return the formatted string
        return "$formattedDate, $formattedTime"
    }

    private fun refreshWeightData(id: String, context: Context, binding: DashboardItemsBinding) {
        // Get a reference to the Firebase database
        val database = FirebaseDatabase.getInstance()
        val databaseReference = database.getReference("/Weight Data/$id")

        var weightData: WeightData
       databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
        // Add a ValueEventListener to listen for changes in the data
                   @SuppressLint("SetTextI18n")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Check if the dataSnapshot contains any data
                if (dataSnapshot.exists()) {
                    Log.i("dd", "dataexist")
                    // Retrieve the data for the given user ID and convert it to a WeightData object
                    weightData = dataSnapshot.getValue(WeightData::class.java)!!
                    Log.i("weightData", weightData.value.toString())
                    binding.userWeight.text = weightData.value.toString()
                    binding.lastUpdateDate.text = "Last Updated ${weightData.date}"

                    val userBMI = calculateBMI(weightData.value, weightData.height)
                    binding.userBMI.text = "$userBMI BMI"
                    binding.BMIscale.text = BMIScale(userBMI)
                    binding.targetWeight.text = targetWeight(weightData.height)
                    Log.i("kim", targetWeight(weightData.height))

                    binding.userWeight.text = "${weightData.value}Kg"

                } else {
                   Toast.makeText(context, "Something unexpected happened, please try again later", Toast.LENGTH_LONG).show()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors that occur during the data retrieval process
                Log.e("weightData", "Data retrieval cancelled: ${databaseError.message}")
            }
        })

    }



    private fun targetWeight(height: Int): String {
        val heightInMeters = height * 0.01
        val targetBMI = heightInMeters * heightInMeters * 24.8
        val decimalFormat = DecimalFormat("#.##")
        val bmi = decimalFormat.format(targetBMI)
        return "Goal Weight: $bmi Kg"

    }


    @SuppressLint("ClickableViewAccessibility")
    private fun editWeight(binding: DashboardItemsBinding){
        val weightET = binding.editWeight
        val weight = binding.userWeight

        weightET.visibility = View.VISIBLE
        weight.visibility = View.GONE
        binding.saveWeight.visibility = View.VISIBLE
        binding.weightEditIcon.visibility = View.GONE

        // Set input type to number
        weightET.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL

        // Set hint
        weightET.hint = binding.root.context.getString(R.string.kg)
        weightET.setBackgroundResource(R.drawable.edittext_border)



    }

    private fun updateMetricsChart(binding: DashboardItemsBinding) {
        binding.waterProgressIndicator.setProgress(waterProgress, true)
        binding.stepsProgressIndicator.setProgress(stepsProgress, true)
        binding.caloryProgressIndicator.setProgress(caloryProgress, true)
        binding.caloryProgressIndicator2.setProgress(caloryProgress, true)
        binding.exerciseProgressIndicator.setProgress(exerciseProgress, true)
        binding.exerciseProgressIndicator2.setProgress(exerciseProgress, true)
    }

    // this function calculates the BMI of the user

    private fun calculateBMI(weight: Double, height: Int): Double {
        val heightInMeters = height / 100.0

        // Calculate BMI using the formula: weight (kg) / height (m)^2
        val bmi = weight / (heightInMeters * heightInMeters)

        // Round BMI to 2 decimal places
        return String.format("%.2f", bmi).toDouble()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun metricsAlert(context: Context, binding: DashboardItemsBinding, holder: DashboardViewHolder, userID: String) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dashboar_update_alert, null)
        val activityCount = dialogView.findViewById<Spinner>(R.id.exerciseCount)
        val stepsCount = dialogView.findViewById<EditText>(R.id.stepsCount)
        val caloriesCount = dialogView.findViewById<EditText>(R.id.caloriesCount)
        val waterCupsCount = dialogView.findViewById<Spinner>(R.id.waterCupsSpinner)
        val buttonSave = dialogView.findViewById<Button>(R.id.buttonSave)
        val buttonCancel = dialogView.findViewById<Button>(R.id.buttonCancel)


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
                steps.toInt(), holder
            )
            updateMetricsChart(binding)
        saveDailyMetricsData(holder, MetricsData( waterInML.toInt(),steps.toInt(),calories.toInt(),exerciseCount.toInt()), userID)

            Toast.makeText(context, "Metrics Updated", Toast.LENGTH_SHORT).show()
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
        waterCupsCount.setSelection(waterCups.indexOf(totalMills/250))
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
//convert cups to milliliter
    private fun cupsToMl(cups: String): String {
        val ml = cups.toInt() * 250
        return ml.toString()
    }
//----------------------------------------------------------------
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun updateIndicatorData(
        binding: DashboardItemsBinding,
        water: Int,
        kcal: Int,
        exercise: Int,
        steps: Int, holder: DashboardViewHolder
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
//----------------------------------------------------------------
    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun dailyDashboardReset(binding: DashboardItemsBinding, holder: DashboardViewHolder) {


        waterProgress = 0
        caloryProgress = 0
        exerciseProgress = 0
        stepsProgress = 0

        updateIndicatorData(binding, waterProgress, caloryProgress, exerciseProgress, stepsProgress, holder)

    }
//---------------------------------------------------------------------------
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
//---------------------------------------------------------------------------
    private fun disclaimer(context: Context) {

        val alertDialogBuilder = AlertDialog.Builder(context)
        alertDialogBuilder.apply {
            setTitle("Disclaimer!")
            setMessage("BMI is a measure of weight relative to height and may not accurately reflect body composition, particularly for muscle mass.\n" +
                    "WHO recommends using BMI alongside other diagnostic tools to assess health risks.")

            setNegativeButton("Ok, Thanks") { dialog, _ ->
                // Dismiss the dialog if "No" is clicked
                dialog.dismiss()
            }
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }


        }
//weightET.visibility = View.GONE
//weight.visibility = View.VISIBLE
