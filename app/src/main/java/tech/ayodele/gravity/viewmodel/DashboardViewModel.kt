package tech.ayodele.gravity.viewmodel

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import tech.ayodele.gravity.Inspirations
import tech.ayodele.gravity.Signin
import tech.ayodele.gravity.model.DashboardData
import tech.ayodele.gravity.model.UserDetails
import tech.ayodele.gravity.view.Dashboard
import java.time.LocalDate

class DashboardViewModel : ViewModel() {

    private lateinit var prefs: SharedPreferences
    private lateinit var userprefs: SharedPreferences
    private var lastDate: LocalDate? = null
    @RequiresApi(Build.VERSION_CODES.O)
    private var currentDate = LocalDate.now()

    fun initSharedPreferences(context: Context) {
        prefs = context.getSharedPreferences("dashboardData", Context.MODE_PRIVATE)
        userprefs = context.getSharedPreferences("saveData", Context.MODE_PRIVATE)
    }

    fun retrieveUserData(context: Context): UserDetails {
        userprefs = context.getSharedPreferences("saveData", Context.MODE_PRIVATE)
        val userDataJson = userprefs.getString("userdata", null)
        val gson = Gson()
        return gson.fromJson(userDataJson, UserDetails::class.java)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun retrieveDashboardData(context: Context): DashboardData? {
        val userData = retrieveUserData(context)
        val weight = userData.weight ?: 0
        val height = userData.height ?: 0
        val name = userData.name.toString()
        val firstName = firstName(name)
        val userID = userData.id.toString()
        val email = userData.email.toString()
        val inspiration = inspirations()
        return inspiration?.let {
            DashboardData(
                name = "Hello $firstName!",
                userWeight = weight,
                userHeight = height,
                inspiration = it,
                email = email,
                userID = userID
            )
        }
    }


    fun logoutUser(context: Context) {
        FirebaseAuth.getInstance().signOut()
        clearUserData(context)
        val intent = Intent(context, Signin::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }

    private fun clearUserData(context: Context) {
        val prefs = context.getSharedPreferences("saveData", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.clear().apply()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun inspirations(): String? {
        return if (currentDate != lastDate) {
            lastDate = currentDate
            val currentInspo = Inspirations.getInspirations().random()
            val editor = prefs.edit()
            editor.putString("lastDate", currentDate.toString())
            editor.putString("lastInspo", currentInspo)
            editor.apply()
            currentInspo
        } else {
            val editor = prefs.edit()
            editor.putString("lastDate", currentDate.toString())
            editor.apply()
            prefs.getString(
                "lastInspo",
                "Every step forward, no matter how small, is a victory on the path to a healthier you"
            )
        }
    }

    //    get user first name
    fun firstName(name: String): String {
        val indexOfSpace = name.indexOf(" ")
        return if (indexOfSpace != -1) {
            name.substring(0, indexOfSpace)
        } else {
            // If no space found, return the entire name as the first name
            name
        }
    }
}
