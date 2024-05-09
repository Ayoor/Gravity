package tech.ayodele.gravity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import tech.ayodele.gravity.databinding.ActivityOnBoardingSurveyBinding

// Initial Onboarding survey for the users
class OnBoardingSurvey : AppCompatActivity() {
    // variable declarations
    private lateinit var binding: ActivityOnBoardingSurveyBinding
    private var currentQuestionIndex = 0
    private var allQuestions: List<QuestionModel> = mutableListOf()
    private val PREFS_NAME = "MyPrefs"
    private val PREF_ONBOARDINGSURVEY_COMPLETE = "survey_complete"
    private lateinit var prefs: SharedPreferences
    private lateinit var databaseReference: DatabaseReference
    private val selectedAnswers = mutableListOf<String>()
    private var itIsNewEntry = true



    /*TODO
    *   Highlight Selected Button
    */
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val onboardingSurveyCompleted = prefs.getBoolean(PREF_ONBOARDINGSURVEY_COMPLETE, false)


// set up binding
        binding = ActivityOnBoardingSurveyBinding.inflate(layoutInflater)
        setContentView(binding.root)

// check if the survey has not been completed
        if (!onboardingSurveyCompleted) {
            enableEdgeToEdge()
            ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }

            // an instance of the object of the survey questions
            allQuestions = SurveyData.getSurveyData()
            loadSurveyToScreen()

            //handle skip button
            binding.skip.setOnClickListener {
                    //alert to confirm the user skipping the survey
                    val alertDialogBuilder = AlertDialog.Builder(this)
                    alertDialogBuilder.apply {
                        setTitle("Confirm Skip")
                        setMessage("To get the most of this App, this Survey is necessary. Are you Sure you want to Skip?")
                        setPositiveButton("Yes, I'm Sure") { dialog, _ ->
                            finishSurvey(prefs)
                        }
                        setNegativeButton("Continue Survey") { dialog, _ ->
                            // Dismiss the dialog if "No" is clicked
                            dialog.dismiss()
                        }
                    }
                    val alertDialog = alertDialogBuilder.create()
                    alertDialog.show()
            }
        }
        else { // survey has been completed
            finishSurvey(prefs)
        }
//            button finish the survey
        binding.finish.setOnClickListener {
//            check internet status
            if(isInternetAvailable(this)) {
                submitSurvey(passUserData()?.id.toString())
                finishSurvey(prefs)
            }
            else{
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()
            }
        }

//        handle the previous text click
        binding.previousET.setOnClickListener {
            itIsNewEntry = false
            when {
//                when the current question is not the first question
                currentQuestionIndex > 0 && selectedAnswers.size == currentQuestionIndex  -> {
                    selectedAnswers.removeAt(currentQuestionIndex -1)
                    currentQuestionIndex--
                    binding.finish.visibility = View.GONE
                    loadSurveyToScreen()
                    Log.i("test2", selectedAnswers.toString())
                }
                currentQuestionIndex > 0 && selectedAnswers.size == currentQuestionIndex +1 -> {
                    selectedAnswers.removeAt(currentQuestionIndex)
                    currentQuestionIndex--
                    binding.finish.visibility = View.GONE
                    loadSurveyToScreen()
                    Log.i("test2", selectedAnswers.toString())
                }
//                remove the latest entry from the list to avoid double entry of the same answers
//                  and go to previous questions
                selectedAnswers.size ==1 ->{
                    currentQuestionIndex--
                    loadSurveyToScreen()
                    binding.previousET.visibility = View.GONE

                    selectedAnswers.clear()
                    Log.d("testC", "Contents of selectedAnswers: $selectedAnswers")
                }

            }



        }
    }

    @SuppressLint("ServiceCast")
    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(network) ?: return false

        return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
    }

//method to complete the survey and save the user's preferences
    private fun finishSurvey(preferences: SharedPreferences) {
        val editor = preferences.edit()
        editor.putBoolean(PREF_ONBOARDINGSURVEY_COMPLETE, true)
        editor.apply()

        val intent = Intent(this, Dashboard::class.java)
        intent.putExtra("userData", passUserData())

        startActivity(intent)
        finish()
    }

//    get the user signup data to be used in the dashboard

    private fun passUserData(): UserDetails? {
        val userData: UserDetails? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("userData", UserDetails::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra<UserDetails>("userData")
        }
        return userData
    }

//load the questions to the screen

    @SuppressLint("SetTextI18n")
    fun loadSurveyToScreen() {
        binding.questionIndex.text = "${currentQuestionIndex + 1} / ${allQuestions.size}"
        binding.questionTV.text = allQuestions[currentQuestionIndex].question
        binding.title.text = allQuestions[currentQuestionIndex].title

        val optionsSize = allQuestions[currentQuestionIndex].options.size

        optionButtons(optionsSize, allQuestions[currentQuestionIndex].options, allQuestions.size)

        if (currentQuestionIndex == 0)
            binding.previousET.visibility = View.GONE
    }

//this method generates predefined options buttons for the survey questions

    private fun optionButtons(buttonCount: Int, options: List<String>, questionCount: Int) {
        val buttonLayout = binding.buttonsLayout
        buttonLayout.removeAllViews()

        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, // Width
            LinearLayout.LayoutParams.WRAP_CONTENT // Height
        )

        for (i in 0 until buttonCount) {
            val button = MaterialButton(this)

            // Set margins programmatically
            val buttonParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            buttonParams.setMargins(0, 0, 0, 15) // left, top, right, bottom

            button.text = options[i]
            button.textSize = 15f
            button.layoutParams = layoutParams
            button.layoutParams = buttonParams

            // Set click listener for the options button
            if (currentQuestionIndex < questionCount - 1) { // minus one because array starts from 0
                button.setOnClickListener {

                    if(currentQuestionIndex == selectedAnswers.size || selectedAnswers.size == 0) {

                        selectedAnswers.add(button.text.toString())
                    }
                    else{
                        selectedAnswers[currentQuestionIndex] = button.text.toString()
                    }

                    if(selectedAnswers.size ==1 && !itIsNewEntry){
                        selectedAnswers[currentQuestionIndex] = button.text.toString()
                    }

                    Log.i("test", selectedAnswers.toString())
                    currentQuestionIndex++
                    loadSurveyToScreen()


                }
                buttonLayout.addView(button)
            }

            // Add finish button and adjust visibility
            if (currentQuestionIndex == questionCount - 1) {
                buttonLayout.addView(button)

                button.setOnClickListener {
                    if (selectedAnswers.size == questionCount) {
                        selectedAnswers[currentQuestionIndex] = button.text.toString() //replace last index rather than add new one
                        binding.finish.visibility = View.VISIBLE
                    } else {
                        selectedAnswers.add(button.text.toString())
                        binding.finish.visibility = View.VISIBLE
                    }

                    Log.i("test", selectedAnswers.toString())


                }
            }

            // Adjust visibility of previous button
            if (currentQuestionIndex > 0) {
                binding.previousET.visibility = View.VISIBLE
            }


        }


    }

    //submit survey to database

    private fun submitSurvey(
        userID: String
    ) {
        val questionList = SurveyData.getSurveyData()
        databaseReference  =  FirebaseDatabase.getInstance().reference
        val userAndResponse = mutableListOf<Any>()

        //add survey question and corresponding answer to list
        for (i in questionList.indices) {
            val question = questionList[i]
            val answer = selectedAnswers.getOrNull(i) ?: "Invalid Answer" // Get the corresponding answer or empty string if not found

            userAndResponse.add(
                SurveyResponse(question.question,answer)
            )



        }
        userAndResponse.add(0, userID)
        val userType = getUserType(selectedAnswers)
        userAndResponse.add( 0, userType)
Log.i("submit", userAndResponse.toString())
//         Save the survey data map to Firebase Realtime Database
        databaseReference.child("Survey Data").child(userID).setValue(userAndResponse)
            .addOnSuccessListener {
                Toast.makeText(
                    this@OnBoardingSurvey,
                    "Survey Submitted",
                    Toast.LENGTH_SHORT
                ).show()

            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this@OnBoardingSurvey,
                    "Sign Up Failed: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }

        val userTypeRef = databaseReference.child("Users").child(userID).child("userType")
        userTypeRef.setValue(userType)

    }
    private fun getUserType(userSurveyResponse: List<String>): String {
        var activeUser = 0
        var passiveUser = 0
        var sedentaryUser = 0
        var userType = ""


        when (userSurveyResponse[0]) {
            "Sedentary" -> sedentaryUser++
            "Lightly active" -> passiveUser++
            "Moderately active" -> passiveUser++
            "Very active" -> activeUser++
        }

        when (userSurveyResponse[1]) {
            "Very Easily" -> activeUser++
            "Easily" -> passiveUser++
            "Normal" -> passiveUser++
            "Difficult" -> sedentaryUser++
            "Very Difficult" -> sedentaryUser++
        }

        when (userSurveyResponse[2]) {
            "Very Comfortable" -> activeUser++
            "Somewhat Comfortable" -> passiveUser++
            "Neutral" -> passiveUser++
            "Somewhat Uncomfortable" -> sedentaryUser++
            "Very Uncomfortable" -> sedentaryUser++
        }

        if (userSurveyResponse[3] == "Yes") {
            sedentaryUser++
        }

        when (userSurveyResponse[4]) {
            "Balanced and healthy" -> activeUser++
            "Excessive consumption of processed foods" -> passiveUser++
            "Regular consumption of energy-dense foods" -> passiveUser++
            "Eating disorders or irregular eating patterns" -> sedentaryUser++
        }

        if (userSurveyResponse[5] == "Yes") {
            sedentaryUser++
        }

        when (userSurveyResponse[6]) {
            "Regularly (several times a week)" -> activeUser++
            "Occasionally (once or twice a week)" -> passiveUser++
            "Rarely (less than once a week)" -> passiveUser++
            "Never" -> sedentaryUser++
        }

        if (userSurveyResponse[7] == "Yes") {
            sedentaryUser++
        }

        // Determine user type based on the counts
        userType = if (activeUser > passiveUser && activeUser > sedentaryUser) {
            "Active User"
        } else if (passiveUser > activeUser && passiveUser > sedentaryUser) {
            "Passive User"
        } else {
            "Sedentary User"
        }

        return userType
    }

}