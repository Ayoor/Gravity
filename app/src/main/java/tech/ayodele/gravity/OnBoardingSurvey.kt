package tech.ayodele.gravity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
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
    private val selectedAnswers = mutableListOf<String>()

    /*TODO
    *  Previous Button Functionality
    *   Highlight Selected Button
    *   Save Result in Database*/
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val onboardingSurveyCompleted = prefs.getBoolean(PREF_ONBOARDINGSURVEY_COMPLETE, false)

// set up binding
        binding = ActivityOnBoardingSurveyBinding.inflate(layoutInflater)
        setContentView(binding.root)
// check if the survey has already been completed
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
        } else { // survey has been completed
            finishSurvey(prefs)
        }
//            button finish the survey
        binding.finish.setOnClickListener {
            finishSurvey(prefs)
        }
//        handle the previous text click
        binding.previousET.setOnClickListener {
            when {
//                when the current question is not the first question
                currentQuestionIndex > 0 && selectedAnswers.size == currentQuestionIndex + 1 -> {
                    selectedAnswers.removeAt(currentQuestionIndex)
                    currentQuestionIndex--
                    binding.finish.visibility = View.GONE
                    Log.i("test2", selectedAnswers.toString())
                    loadSurveyToScreen()
                }
//                remove the latest entry from the list to avoid double entry of the same answers
//                  and go to previous questions
                currentQuestionIndex > 2 -> {
                    selectedAnswers.removeAt(currentQuestionIndex - 1)
                    currentQuestionIndex--
                    binding.finish.visibility = View.GONE
                    Log.i("test2", selectedAnswers.toString())
                    loadSurveyToScreen()
                }

            }
            Log.d("testS", "Size of selectedAnswers: ${selectedAnswers.size}")

//            clear the list to avoid double entry of the same answers and go to previous questions
            if (selectedAnswers.size == 2) {
                Log.d("testC", "Contents of selectedAnswers: $selectedAnswers")
                selectedAnswers.removeAt(1)
                selectedAnswers.removeAt(0)
            }

        }
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

            // Set click listener
            if (currentQuestionIndex < questionCount - 1) { // minus one because array starts from 0
                button.setOnClickListener {

                    selectedAnswers.add(button.text.toString())
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
                    currentQuestionIndex > 0 && selectedAnswers.size == currentQuestionIndex
                    if (selectedAnswers.size == questionCount) {
                        selectedAnswers[currentQuestionIndex] = button.text.toString()
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


}