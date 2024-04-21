package tech.ayodele.gravity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.navigation.NavigationBarView
import com.google.gson.Gson
import papaya.`in`.sendmail.SendMail
import tech.ayodele.gravity.databinding.ActivityLiveHelpBinding

class LiveHelp : AppCompatActivity() {
    private lateinit var binding: ActivityLiveHelpBinding
    private lateinit var userprefs: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(0, 0)
        userprefs = getSharedPreferences("saveData", Context.MODE_PRIVATE)
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
                    R.id.dashboard_home -> {
                        startActivity(Intent(this@LiveHelp, Dashboard::class.java))
                        finish()
                        return true
                    }

                    R.id.community -> {
                        startActivity(Intent(this@LiveHelp, Community::class.java))
                        finish()
                        return true
                    }

                    R.id.insightIcon -> {
                        startActivity(Intent(this@LiveHelp, Insights::class.java))
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
        binding.sendEmail.setOnClickListener {
            val message = binding.mailBody.text.toString()
            val subject = binding.subject.text.toString()
            val userData = retrieveUserData(userprefs)
            val name = userData.name?.let { it1 -> firstName(it1) }
            val fullName = userData.name
            if (message.isEmpty()|| subject.isEmpty()){
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
            else{
                sendMail(message, userData.email.toString(), name?:"User", fullName?:"Invalid", subject)
            }
        }
    }
    private fun retrieveUserData(preferences: SharedPreferences): UserDetails {
        val userDataJson = preferences.getString("userdata", null)
        val gson = Gson()
        return gson.fromJson(userDataJson, UserDetails::class.java)
    }

    private fun firstName(name: String): String {
        val indexOfSpace = name.indexOf(" ")
        return if (indexOfSpace != -1) {
            name.substring(0, indexOfSpace)
        } else {
            // If no space found, return the entire name as the first name
            name
        }
    }

    private fun sendMail(message: String, email: String, name:String, fullname:String, subject:String) {
        val mail = SendMail(
            "gbengajohn4god@gmail.com", "eeoacjjcsbbzfajt",
            "gbengajohn4god@gmail.com", // I should use actual email here
            "Gravity Live Help Assistance",
            "Sender: $email\n" +
                    "Subject: $subject\n" +
                    "Name: $fullname\n" +
                    "Message: $message"
        )
        val confirmationMail = SendMail(
            "gbengajohn4god@gmail.com", "eeoacjjcsbbzfajt",
            email, // I should use actual email here
            "Confirmation!",
            "Hello $name this is to Confirm that we Got your email and will Respond to you as soon as Possible" +
                    "\nWe hope you have a good day and wish you the best in your weight loss journey"
        )
        try {
            mail.execute()
            confirmationMail.execute()
            startActivity(Intent(this@LiveHelp, MailSuccess::class.java))
            finish()
        } catch (e: Exception) {
            Toast.makeText(
                this@LiveHelp,
                "An Error Occurred While sending Mail",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


}